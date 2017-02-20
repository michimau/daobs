# Maintainer guide


## Requirements

* Java 8
* Java servlet container (eg. Tomcat 8+)
* ES 5.x
* ETF (optional)
* INSPIRE validator (optional): if not available use remote service
* A modern web browser. The latest version of Chrome and Firefox have been tested to work. Safari also works, except for the "Export to File" feature for saving dashboards. We recommend that you use Chrome or Firefox while building dashboards. IE10+ should be also supported.



## Installation

Download and install [Elasticsearch](../es/README.md) (See Manual installation).

Download and install [Tomcat](http://tomcat.apache.org/download-80.cgi)

Download daobs.war and deploy it in Tomcat webapps folder.

Open http://localhost:8080/daobs/

(optional) Download and install [ETF](../tasks/etf-validation-checker/README.md).


## User & security configuration

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

## Search engine details

[See Elasticsearch documentation.](../es/README.md)


## Harvesting

[See harvester documentation.](../harvesters/README.md)


## Analysis

[See analysis documentation.](../tasks/README.md)



## Reporting

Report configuration is made web/src/main/webapp/WEB-INF/reporting.
One or more configuration file can be created in this folder. The file name should follow the pattern "config-{{report_id}}.xml".

A report is created from a set of variables and indicators. Variables are defined using query expressions to be computed by the search engine. Indicators are created from mathematical expressions based on variables.

### Creating new reports



