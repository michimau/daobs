# Maintainer guide


## Requirements

* Java 8
* Java servlet container (eg. Tomcat 8+)
* Solr 6.x
* ETF (optional)
* INSPIRE validator (optional): if not available use remote service
* A modern web browser. The latest version of Chrome and Firefox have been tested to work. Safari also works, except for the "Export to File" feature for saving dashboards. We recommend that you use Chrome or Firefox while building dashboards. IE10+ should be also supported.



## Installation

Download and install [Solr](../solr/README.md) (See Manual installation).

Download and install [Tomcat](http://tomcat.apache.org/download-80.cgi)

Download daobs.war and deploy it in Tomcat webapps folder.

Open http://localhost:8080/daobs/

### (optional) Install ETF for service validation

Download and install [ETF](../tasks/etf-validation-checker/README.md).


## User configuration


Administration pages are accessible only to non anonymous users.

By default, only one user is defined with username "admin" and password "admin". 

To add more user, configuration is made in ```WEB-INF/config-security-ba.xml```.

LDAP is also supported. In ```WEB-INF/web.xml```, switch to ```ldap``` profile:

```$xml

  <context-param>
    <param-name>spring.profiles.active</param-name>
    <param-value>ldap</param-value>
  </context-param>
```


## Search engine architecture

2 Solr cores are created:
* one for storing dashboards
* one for storing metadata records and indicators


## Importing data
 
2 types of information can be loaded into the system:

* Metadata records following the standard for metadata on geographic information ISO19139/119
* Indicators in [INSPIRE monitoring reporting format](http://inspire-geoportal.ec.europa.eu/monitoringreporting/monitoring.xsd)


### Harvesting records

#### Harvester configuration

An harvester engine provides the capability to harvest metadata records from discovery service (CSW end-point).
The list of nodes to harvest is configured in harvester/csw-harvester/src/main/resources/WEB-INF/harvester/config-harvester.xml.


The configuration parameters are:
* territory: A representative geographic area for the node
* folder: The folder name where harvested records are stored
* name: The name of the node
* url: The server URL to request (should provide GetCapabilities and GetRecords operations)
* filter: (Optional) A OGC filter to restrict the search to a subset of the catalog


Example:

```
<harvester>
  <territory>de</territory>
  <folder>de</folder>
  <name>GeoDatenKatalog.De</name>
  <url>http://ims.geoportal.de/inspire/srv/eng/csw</url>
  <filter>
    <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
      <ogc:PropertyIsLike escapeChar="\" singleChar="?" wildCard="*">
        <ogc:PropertyName>AnyText</ogc:PropertyName>
        <ogc:Literal>inspireidentifiziert</ogc:Literal>
      </ogc:PropertyIsLike>
    </ogc:Filter>
  </filter>
</harvester>
```

#### Running harvester

Harvesting records from a CSW end-point:

```
cd harvesters/csw-harvester
mvn camel:run
```

#### Harvester details

[Apache Camel](http://camel.apache.org/) integration framework based on Enterprise Integration Patterns is used to easily create configurable harvesters by defining routing and mediation rules.

The CSW harvester strategy is the following:
* GetRecords to retrieve the number of metadata to be harvested
* Compute paging information
* GetRecords for each pages and index the results in Solr.


While harvesting the GetRecords query and response are saved on disk. A log file return detailed information about the current process.


Harvesting is multithreaded on endpoint basis. By default, configuration is 11 threads across harvesters which means no multithreaded requests on the same server but 11 nodes could be harvested in parallel. 



### Indexing ISO19139 records

Metadata records and indicators could be manually loaded using Solr API by importing XML files.
First you need to apply the following XSL transformation to convert ISO metadata
to Solr XML transaction document.

After the transformation, you can then load the output documents using:

```
# Load files in all subfolders
find . -name *.xml -type f |
while read f
do
  echo "importing '$f' file..";
  curl "http://localhost:8983/data/update?commit=true" \
    -u admin:admin \
    -H "Content-Type: text/xml; charset=utf-8" \
    --data-binary @$f
done
```


### Indexing INSPIRE indicators


Manually indexing INSPIRE monitoring:

```
for f in *.xml; do
  echo "importing '$f' file..";
  curl "http://localhost:8983/data/update?commit=true&tr=inspire-monitoring-reporting.xsl" -u admin:admin -H "Content-Type: text/xml; charset=utf-8" --data-binary @$f
done
```

To import monitoring with ancillary information:
```
for f in *.xml; do
  echo "importing '$f' file..";
  curl "http://localhost:8983/data/update?commit=true&tr=inspire-monitoring-reporting-with-ai.xsl" -u admin:admin -H "Content-Type: text/xml; charset=utf-8" --data-binary @$f
done
```




## Removing documents


Manually dropped all records:
```
curl http://localhost:8983/data/update \
    --data '<delete><query>documentType:*</query></delete>' \
    -u admin:admin \
    -H 'Content-type:text/xml; charset=utf-8'

curl http://localhost:8983/data/update \
    --data '<commit/>' \
    -u admin:admin \
    -H 'Content-type:text/xml; charset=utf-8'
```

The search query could be adapted to restrict to a subset of documents:
* reportingYear:2014 for removing reporting for 2014





## Analysis tasks

A set of background tasks could be triggered on the content of the index and 
improve or add information to the index.

### Validation task


To trigger the validation:

```
cd tasks/validation-checker
mvn camel:run
```

By default, the task validates all records which have not been validated before (ie. +documentType:metadata -isValid:[* TO *]). A custom set of records could be validated by changing the solr.select.filter in the config.properties file.


### Services and data sets link

To trigger the validation:

```
cd tasks/service-dataset-indexer
mvn camel:run -Pcli
```

By default, the task analyze all services.




### Associated resource indexer

To trigger the data analysis:

```
cd tasks/data-indexer
mvn camel:run -Pcli
```



## Dashboard


Access the dashboard page, click load and choose dashboard configuration from the list. 
If no dashboards are available sample dashboard are available here: dashboard/src/app/dashboards

* Browse: Search for metadata records and filter your search easily (facets, INSPIRE themes and annexes charts).
* INSPIRE-Dashboard: Home page
* default: Monitoring reporting 2013 dashboard

By default no dashboard are loaded.

User can load a set of dashboards using the /daobs/samples/dashboard service.

Eg. http://localhost:8983/daobs/samples/dashboard/INSPIRE.json will load all INSPIRE specific dashboards.

2 sets of dashboards are available:
* INSPIRE* about INSPIRE monitoring
* CATALOG* for dashboards on harvested records.


## Reporting

Report configuration is made web/src/main/webapp/WEB-INF/reporting.
One or more configuration file can be created in this folder. The file name should follow the pattern "config-{{report_id}}.xml".

A report is created from a set of variables and indicators. Variables are defined using query expressions to be computed by the search engine. Indicators are created from mathematical expressions based on variables.

### Creating new reports



