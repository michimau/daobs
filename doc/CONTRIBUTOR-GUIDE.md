# Contributor guide



## Build the application from the source code

Requirements

* Git
* Maven 3.1.0+

Get the source code with

```
git clone --recursive https://github.com/INSPIRE-MIF/daobs.git
cd daobs
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

See [Solr installation](../solr/README.md).


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
