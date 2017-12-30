#/bin/bash

curl 'elasticsearch:9200/_cat/indices?v'

# Create index
cd /usr/local/tomcat/daobs/es/config;
curl -X PUT http://elasticsearch:9200/records -d @records.json;
curl -X PUT http://elasticsearch:9200/indicators -d @indicators.json;

cd /usr/local/tomcat/daobs/dashboards/data;
elasticdump \
  --input=index-dashboards-mapping.json \
  --output=http://elasticsearch:9200/.dashboards \
  --type=mapping

elasticdump \
  --input=index-dashboards.json \
  --output=http://elasticsearch:9200/.dashboards

#rm index-dashboards-mapping.json && rm index-dashboards.json;

cp -r $CATALINA_HOME/daobs/web/target/daobs/WEB-INF/datadir/* \
    ${INSTALL_DASHBOARD_PATH}/daobs-data-dashboard

echo -e "\e[96mStart tomcat\e[0m"

cd $CATALINA_HOME
catalina.sh run

