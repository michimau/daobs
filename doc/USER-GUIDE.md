# User guide

The guide for user installing and configuring the application.

## Harvesting catalogs

Daobs support harvesting from [OGC CSW](http://www.opengeospatial.org/standards/cat) services.


Sign in and move to the harvesting section. 
Click on ```+``` button to add a new one.


![Add an harvester]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-add.png)

Click on ```harvest``` button to start harvesting the catalog. During harvesting
the number of records will increase progressively. Harvesting is a background task
that user can follow in the ```monitoring``` tab.

![Harvester status]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-status.png)

Once all records are harvested, analysis tasks may be triggered depending on the 
server configuration. By default, the following tasks are triggered:

* [XSD and INSPIRE validation](#XSD and INSPIRE validation)
* [Service/dataset links](#Service/dataset links)

[ETF validation](#ETF validation)can manually be triggered.

Other tasks are also available:

* [Database validation](#Database validation)
* [Associated resource indexer (experimental)](#Associated resource indexer (experimental))



## Analysis tasks

### XSD and INSPIRE validation

A two steps validation task is defined:

* XML Schema validation
* [INSPIRE validator](http://inspire-geoportal.ec.europa.eu/validator2/#)

The validation result summary is displayed below harvester statistics:

![Harvester validation status]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-validation-status.png)



The results and details of the validation process are stored in the index:

* For INSPIRE validation:
 * isValid: Boolean
 * validDate: Date of validation
 * validReport: XML report returned by the validation service
 * validInfo: Text information about the status
 * completenessIndicator: Completeness indicator reported by the validation tool
 * isAboveThreshold: Boolean. Set to true if the completeness indicator is above a value defined in the validation task configuration
* For XML Schema validation:
 * isSchemaValid: Boolean
 * schemaValidDate: The date of validation
 * schemaValidReport: XSD validation report


### Database validation

By default, this validation is disabled. It allows to connect to a database
containing validation information about metadata records. It can connect for
example to a GeoNetwork database.


### Service/dataset links

A data sets may be accessible through a view and/or download services. This type 
of relation is defined at the service metadata level using the operatesOn element:

* link using the data sets metadata record UUID:

```
<srv:operatesOn uuidref="81aea739-4d21-427d-bec4-082cb64b825b"/>
```

* link using a GetRecordById request:
```
<srv:operatesOn uuidref="BDML_NATURES_FOND"
                xlink:href="http://services.data.shom.fr/csw/ISOAP?service=CSW&version=2.0.2&request=GetRecordById&Id=81aea739-4d21-427d-bec4-082cb64b825b"/>
```

Both type of links are supported. The GetRecordById takes priority. The data sets 
metadata record identifier is extracted from the GetRecordById request.



This task analyze all available services in the index and update associated data 
sets by adding the following fields:

* recordOperatedByType: Contains the type of all services operating the data sets (eg. view, download)
* recordOperatedBy: Contains the identifier of all services operating the data sets. Note: it does not provide information that this service is a download service. User need to get the service record to get this details.

The task also propagate INSPIRE theme from each datasets to the service.


### ETF validation

[ETF](http://www.geostandaarden.nl/validatie/inspire/) is used to validate service.


![ETF tasks menu]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-etfmenu.png)



### Associated resource indexer (experimental)

A metadata record may contain URL to remote resources (eg. PDF document, ZIP files).
This task will retrieve the content of such document using [Tika analysis toolkit](https://tika.apache.org/)
and index the content retrieved. This improve search results has the data related
to the metadata are also indexed.


Associated document URL are stored in the linkUrl field in the index.



## Following background tasks progress


A minimal monitoring console allows to check if any background tasks (harvesting or analysis)
are running.


![Monitor background task]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-monitor.png)


For the time being, no options are available to stop a background process.




## Creating reports

Once metadata records are harvested, user can create reports from the
```Monitoring``` > ```Create monitoring```. A report is composed of
a set of indicators and each indicator are based on query or expression.

By default, 2 reports are available:

![Default reports]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-reports.png)

User can preview report rules by clicking ```View report rules``` button:

![Report rules]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview.png)


First select a report, then:
* Choose a reporting area (ie. one harvester)
* (Optional) Use facets or query filter to subset records
* Click on ```Preview``` to compute indicators for this report


![Report indicator preview]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview-indicator.png)


Report preview allows to quickly filter indicators:
* Search by name or id
* ```Indicator with errors only```
* ```Indicator with non null value```


![Report indicator filter]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-preview-filter.png)

A report allows to save indicator values for a certain date. User can follow
the trend of indicators by generating reports on a regular basis. For example,
for INSPIRE, Member States provide report every year since 2011.


## Downloading reports

Reports can be download in various formats using the ```Download``` button:

![Report download]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-download.png)


For INSPIRE format, it may be relevant to set contact details to populate
report metadata which will be required to create a full reporting:

![Report metadata]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-metadata.png)


## Submitting report

Report in INSPIRE format can be upload to the system from 
```Monitoring``` > ```Submit monitoring``` in order to build
dashboard from indicator values.


![Submit new report]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-submit.png)





## Managing report

When reports are uploaded, user can manage them from 
```Monitoring``` > ```Monitoring summary```.


![Managing report]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/monitoring-report-monitoring.png)




## Building dashboard



### Index fields

