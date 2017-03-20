# XSD and INSPIRE validation

A two steps validation task is defined:

* XML Schema validation
* [INSPIRE validator](http://inspire-geoportal.ec.europa.eu/validator2/#)

The validation result summary is displayed below harvester statistics:

![Harvester validation status]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-validation-status.png)


The results and details of the validation process are stored in the index:

* For INSPIRE validation:
  * ```isValid```: Boolean
  * ```validDate```: Date of validation
  * ```validReport```: XML report returned by the validation service
  * ```validInfo```: Text information about the status
  * ```completenessIndicator```: Completeness indicator reported by the validation tool
  * ```isAboveThreshold```: Boolean. Set to true if the completeness indicator is above a value defined in the validation task configuration
* For XML Schema validation:
  * ```isSchemaValid```: Boolean
  * ```schemaValidDate```: The date of validation
  * ```schemaValidReport```: XSD validation report
