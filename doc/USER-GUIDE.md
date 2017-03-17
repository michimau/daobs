# User guide

The guide for user installing and configuring the application.

[See daobs overview description](../README.md).

## Requirements

* Java 8
* Java servlet container (eg. Tomcat 8+)
* Solr 6.x
* ETF (optional)
* INSPIRE validator (optional): if not available use remote service
* A modern web browser. The latest version of Chrome and Firefox have been tested to work. Safari also works, except for the "Export to File" feature for saving dashboards. We recommend that you use Chrome or Firefox while building dashboards. IE10+ should be also supported.

## Installation

Download and install [Solr](solr/README.md) (See Manual installation).

Download and install [Tomcat](http://tomcat.apache.org/download-80.cgi)

Download daobs.war and deploy it in Tomcat webapps folder.

Open http://localhost:8080/daobs/

## Build the application from the source code

Requirements

* Git
* Maven 3.1.0+

Get the source code with

```
git clone --recursive https://github.com/INSPIRE-MIF/daobs.git
cd daobs
git checkout 1.0.x
```


Compile the application running maven

```
mvn clean install -P web
```

or for a quicker build

```
mvn clean install -DskipTests -Drelax -P web
```

## Install and configure Solr

See [Solr installation](solr/README.md).


## Run the application

2 options:

* Deploy the WAR file in a servlet container (eg. tomcat).
* Start the web application using maven.


### Using maven

```
cd web
mvn tomcat7:run-war
```

Access the home page from http://localhost:8983.


### Build a custom WAR file

In order to build a custom WAR file, update the following properties which are defined in the root pom.xml:
* war.name
* webapp.context
* webapp.url
* webapp.username
* webapp.password
* solr.core.data: Define the data core name (useful if more than one daobs instance use the same Solr)
* solr.core.dashboard: Define the dashboard core name


Run the following command line and copy the WAR which is built in web/target/{{war.name}}.war.
```
mvn clean install -Dwebapp.context=/dashboard \
                  -Dwebapp.rootUrl=/dashboard/ \
                  -Dwebapp.url=http://www.app.org \
                  -Dwebapp.username=admin \
                  -Dwebapp.password=secret
```




### Deploy a WAR file

Create a custom data directory.
```
mkdir /usr/dashboard/data
```

Unzip the WAR and check that the WEB-INF/config.properties point to this new directory.
Copy the defaults datadir from WEB-INF/datadir to the custom data directory:

```
# If using the source code
cp -fr web/target/solr/WEB-INF/datadir/* /usr/dashboard/data/.

# If using the WAR file
unzip dashboard.war
cp -fr WEB-INF/datadir/* /usr/dashboard/data/.
```



Deploy the WAR file in Tomcat (or any Java container).

```
cp web/target/dashboard.war /usr/local/apache-tomcat/webapps/.
```

Run the container.

Access the home page from http://localhost:8080/dashboard.

If the Solr URL needs to be updated, look into the WEB-INF/config.properties file.



## Configuration

### Configure security

Administration pages are accessible only to non anonymous users.

By default, only one user is defined with username "admin" and password "admin". To add more user, configuration is made in WEB-INF/config-security-ba.xml.

### Other build options

#### Building the application in debug mode

For developers, the application could be built in debug mode in order to have the banana project installed without Javascript minification. For this disable the production profile:

```
mvn clean install -P\!production
```

#### Building the application without test

The tests rely on some third party application (eg. INSPIRE validator). It may be useful to build the application without testing:

```
mvn clean install -DskipTests
```


## Search engine architecture


2 Solr cores are created:
* one for storing dashboards
* one for storing metadata records and indicators


## Importing data
 
2 types of information can be loaded into the system:

* Metadata records following the standard for metadata on geographic information ISO19139/119
* Indicators in [INSPIRE monitoring reporting format](http://inspire-geoportal.ec.europa.eu/monitoringreporting/monitoring.xsd)

## Harvesting catalogs

Daobs support harvesting from [OGC CSW](http://www.opengeospatial.org/standards/cat) services.
The application support ISO19139 and ISO19115-3 standards.


Sign in and move to the harvesting section. 
Click on ```+``` button to add a new one.


![Add an harvester](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-add.png)

Click on ```harvest``` button to start harvesting the catalog. During harvesting
the number of records will increase progressively. Harvesting is a background task
that user can follow in the ```monitoring``` tab.

![Harvester status](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-status.png)

Once all records are harvested, analysis tasks may be triggered depending on the 
server configuration. By default, the following tasks are triggered:

* [XSD and INSPIRE validation](../tasks/validation-checker/README.md)
* [Service/dataset links](../tasks/service-dataset-indexer/README.md)

[ETF validation](../tasks/etf-validation-checker/README.md)can manually be triggered.

Other tasks are also available:

* [Database validation](../tasks/db-validation-checker/README.md)
* [Associated resource indexer (experimental)](../tasks/data-indexer/README.md)


## Following background tasks progress

A minimal monitoring console allows to check if any background tasks (harvesting or analysis)
are running.


![Monitor background task](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-monitor.png)


For the time being, no options are available to stop a background process.


## Creating reports

Once metadata records are harvested, user can create reports from the
```Monitoring``` > ```Create monitoring```. A report is composed of
a set of indicators and each indicator are based on query or expression.

By default, 2 reports are available:

![Default reports](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-reports.png)

User can preview report rules by clicking ```View report rules``` button:

![Report rules](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview.png)


First select a report, then:
* Choose a reporting area (ie. one harvester)
* (Optional) Use facets or query filter to subset records
* Click on ```Preview``` to compute indicators for this report


![Report indicator preview](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview-indicator.png)


Report preview allows to quickly filter indicators:
* Search by name or id
* ```Indicator with errors only```
* ```Indicator with non null value```


![Report indicator filter](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview-filter.png)

A report allows to save indicator values for a certain date. User can follow
the trend of indicators by generating reports on a regular basis. For example,
for INSPIRE, Member States provide report every year since 2011.


## Downloading reports

Reports can be download in various formats proposed in the ```Download``` dropdown button:

![Report download](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-download.png)


For INSPIRE format, it may be relevant to set contact details to populate
report metadata which will be required to create a full reporting:

![Report metadata](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-metadata.png)


## Submitting report

Report in INSPIRE format can be upload to the system from 
```Monitoring``` > ```Submit monitoring``` in order to build
dashboard from indicator values.


![Submit new report](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-submit.png)



While uploading, import status is reported after each file processing.

![Submit report status](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-submit-status.png)





## Managing report

When reports are uploaded, user can manage them from 
```Monitoring``` > ```Monitoring summary```.


![Managing report](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-monitoring.png)






## Analyzing using dashboards

### Default dashboards

By default, the application provides a set of dashboards for:

* INSPIRE (to follow INSPIRE monitoring)
* Metadata records (to analyze catalog content)


![Default dashboards](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/dashboard-default.png)

Access the dashboard page, click load and choose dashboard configuration from the list
if default dashboard are not available. 

Default dashboard are available here ```dashboard/src/app/dashboards```.

By default no dashboard are loaded.

User can load a set of dashboards using the ```/daobs/samples/dashboard``` API.

Eg. http://localhost:8983/daobs/samples/dashboard/INSPIRE.json will load all INSPIRE specific dashboards.



### Creating your own dashboard

Sign in first. Then use the dashboard creation page to create new one using
the [Banana application](https://github.com/lucidworks/banana/wiki/Tutorials)


#### Index fields

Dashboards are based on index fields. It is usefull to well know the content of
the index to easily build new dashboard.

3 types of documents are available in the index.

* metadata
* indicators
* monitoringMetadata (ie. raw data)

Main fields for metadata document are the following:

* ```documentType```: Fixed value ```metadata```
* ```metadataIdentifier```: Metadata UUID
* ```dateStamp```: Metadata date stamp (should be creation date but is usually storing last update date)
* ```mainLanguage```: Metadata language
* ```otherLanguage```: Other language for multilingual metadata
* ```codelist_*```: All codelists values. * is replaced by the codelist element name
* ```resourceTitle```: Resource title
* ```resourceAltTitle```: Resource alternative title
* ```resourceAbstract```: Resource title
* ```*DateForResource```: Creation, publication or revision date. * is replaced by the type of date
* ```resourceCredit```: Resource credit
* ```resourceLanguage```: Resource language
* ```inspireTheme_syn```: INSPIRE theme values 
* ```inspireTheme```: INSPIRE theme id
* ```inspireAnnex```: INSPIRE annex
* ```inspireThemeFirst_syn```: First INSPIRE theme values 
* ```inspireThemeFirst```: First INSPIRE theme id
* ```inspireAnnexForFirstTheme```: INSPIRE annex for the first INSPIRE theme
* ```numberOfInspireTheme```: Number of INSPIRE theme
* ```isOpenData```: true if contains opendata tag in keywords
* ```topic```: ISO topic category
* ```resolutionScaleDenominator```: Scale denominator
* ```useLimitation```: Use limitation
* ```otherConstraints```: Other constraints
* ```geoTag```: Geographic description
* ```geom```: Extent polygon
* ```serviceType```: Service type
* ```coordinateSystem```: Coordinate system code
* ```inspireConformResource```: Value of data quality report regarding EU 1089/2010 rules for datasets and 976/2009 rules for services

* See more in https://github.com/INSPIRE-MIF/daobs/blob/1.0.x/harvesters/harvester-common/src/main/resources/xslt/metadata-iso19139.xsl



Main fields for indicator document are the following:

* ```id```: A unique identifier based on the territory and date
* ```documentType```: Fixed value ```indicator```
* ```indicatorName```: Indicator name
* ```indicatorValue```: Indicator value
* ```reportingDateSubmission```: Reporting submission date
* ```reportingDate```: Reporting date
* ```reportingYear```: Reporting date year only
* ```territory```: The member state code
* ```contact```: The contact information
* ```contact```: The contact information

Main fields for monitoringMetadata document are the following:

* See https://github.com/INSPIRE-MIF/daobs/blob/1.0.x/reporting/src/main/resources/xslt/inspire-monitoring-reporting.xsl#L169


