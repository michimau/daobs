# Analysis tasks

A set of background tasks could be triggered on the content of the index and 
improve or add information to the index.


* [XSD and INSPIRE validation](validation-checker/README.md)
* [Service/dataset links](service-dataset-indexer/README.md)
* [ETF validation](etf-validation-checker/README.md)
* [Database validation](db-validation-checker/README.md)
* [Associated resource indexer (experimental)](data-indexer/README.md)



# Run validation task from the command line


To trigger the validation:

```
cd tasks/validation-checker
mvn camel:run -Pcli
```

By default, the task validates all records which have not been validated 
before (ie. +documentType:metadata -isValid:[* TO *]). A custom set of 
records could be validated by changing the solr.select.filter in the config.properties file.

