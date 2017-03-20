# ETF Validation Checker

This tasks process the links in the service metadata to validate them with ETF 1.5 (http://www.geostandaarden.nl/validatie/inspire/versies/1.5/ETF1.5.zip). 
The validation information is stored in the index.


[ETF tool](http://www.geostandaarden.nl/validatie/inspire/) is used to validate service.


![ETF tasks menu]
(https://raw.githubusercontent.com/INSPIRE-MIF/daobs/1.0.x/doc/img/harvesting-etfmenu.png)



## Requirements
The following components are required by this task:

* Java 8
* ETF 1.5.x
* Ant 1.8.2 (provided in ETF)


Solr MUST be running in port 8984, with metadata records. See [solr/README.md](../../solr/README.md) for configuration.

## ETF Installation

### Manual installation

Unzip the downloaded file `ETF1.5.1.zip` to a folder, for example:

```
wget http://www.geostandaarden.nl/validatie/inspire/versies/1.5/ETF1.5.1.zip
unzip ETF1.5.1.zip -d /opt
```
That creates the following folder `/opt/ETF1.5.1`

### Installation with maven

```
cd tasks/etf-validation-checker
mvn install -Petf-download
```


## Configuration
The configuration is done in the file `eft-validation-checker/src/main/resources/WEB-INF/config.properties`:

* task.validation-etf-checker.validator.path: Path to ETF tool, for example:

```
task.validation-etf-checker.validator.path=/opt/ETF1.5/ETF
```

* task.validation-etf-checker.filter: Filter to query the metadata to process. Configured by default to retrieve the service metadata:


```
task.validation-etf-checker.filter=+documentType:metadata +resourceType:service
```

## Execution

To run the task from the command line:

```
cd eft-validation-checker
mvn camel:run
```

From the webapplication, run validation on all none validated records:

```
http://localhost:8080/daobs/etf-validator?fq=-etfCompletenessIndicator:[*%20TO%20*]
http://localhost:8080/daobs/etf-validator?fq=id:0847036d-cff2-4ee9-a7f5-d8c01d322d11
```

## SSL certificates

For services that require https connections, it's required to install the certificate in the JVM keystore, to be 
available to the ETF Validation Checker in daobs and to the ETF tool.

To install the certificate:

1. Download your certificate. Assume a file `nl.cer`.

2. Verify the root certificate content:

    ```
    $ keytool -v -printcert -file nl.cer
    ```

3. Import the root certificate into a trust store, setting the `alias` value to your preference and the `storepass` 
to the proper value (default value is `changeit`):

    ```
    $ sudo keytool -importcert -alias nlrootssl \
                   -keystore $JAVA_HOME/jre/lib/security/cacerts \
                   -storepass changeit -file nl.cer
    ```
    
    Output:
    
    ```
    ...
    
    #7: ObjectId: 2.5.29.14 Criticality=false
    SubjectKeyIdentifier [
    KeyIdentifier [
    0000: 69 CB 7F 50 76 00 86 53   95 79 12 C1 58 76 1F 13  i..Pv..S.y..Xv..
    0010: EF F2 4D A3                                        ..M.
    ]
    ]

    Trust this certificate? [no]:  yes
    ```
    
4. Verify that the root certificate has been imported (change the `grep` string 
to the value defined for the `alias in previous step):

    ````
    $ keytool -keystore "$JAVA_HOME/jre/lib/security/cacerts" \
              -storepass changeit -list | grep nlrootssl
    ```
