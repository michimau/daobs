## Install, configure and start Kibana

### Manual installation

Download Kibana from https://www.elastic.co/fr/downloads/kibana

Set Kibana base path and index name in config/kibana.yml:

```
server.basePath: "/<webappname>/dashboard"

kibana.index: ".dashboards"

```

Adapt if needed ```elasticsearch.url``` and ```server.host```.

Start Kibana. Kibana should be running from:

```
http://localhost:5601

```

If not starting properly, check Kibana log file (eg. may fail if Elasticsearch version
is not compatible with Kibana version).


Start Kibana manually:

```
cd kibana/bin
./kibana
```

Import dashboard configuration:

```
npm install elasticdump -g

cd data
elasticdump \
  --input=index-dashboards-mapping.json \
  --output=http://localhost:9200/.dashboards \
  --type=mapping

elasticdump \
  --input=index-dashboards.json \
  --output=http://localhost:9200/.dashboards 

```


### Maven installation

Maven could take care of the installation steps:
* download
* initialize collection
* start

Use the following commands:

```
cd dashboards
mvn install -Pkb-download
mvn exec:exec -Dkb-start
```

### Production use

Configure Kibana to start on server startup.


### Backup index

```
npm install elasticdump -g

rm index-dashboards-mapping.json index-dashboards.json
elasticdump \
  --input=http://localhost:9200/.dashboards \
  --output=index-dashboards-mapping.json \
  --type=mapping

elasticdump \
  --input=http://localhost:9200/.dashboards \
  --output=index-dashboards.json  
```
