# Harvesting records


[Apache Camel](http://camel.apache.org/) integration framework based on 
Enterprise Integration Patterns is used to easily create configurable harvesters 
by defining routing and mediation rules.

The CSW harvester strategy is the following:
* GetRecords to retrieve the number of metadata to be harvested
* Compute paging information
* GetRecords for each pages and index the results in Solr.


While harvesting the GetRecords query and response are saved on disk. 
A log file return detailed information about the current process.


Harvesting is multithreaded on endpoint basis. By default, configuration 
is 11 threads across harvesters which means no multithreaded requests 
on the same server but 11 nodes could be harvested in parallel. 


## Harvester configuration

An harvester engine provides the capability to harvest metadata records 
from discovery service (CSW end-point).
The list of nodes to harvest is configured in 
```harvester/csw-harvester/src/main/resources/WEB-INF/harvester/config-harvester.xml```
and can be configured through the admin interface.


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

## Running harvester from the command line


Harvesting records from a CSW end-point:

```
cd harvesters/csw-harvester
mvn camel:run -Pcli
```
