# Service/dataset links

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

Both type of links are supported. The ```GetRecordById``` takes priority. The data sets 
metadata record identifier is extracted from the ```GetRecordById``` request.



This task analyze all available services in the index and update associated data 
sets by adding the following fields:

* ```recordOperatedByType```: Contains the type of all services operating the data sets (eg. view, download)
* ```recordOperatedBy```: Contains the identifier of all services operating the data sets. Note: it does not provide information that this service is a download service. User need to get the service record to get this details.

The task also propagate INSPIRE theme from each datasets to the service.

