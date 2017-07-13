#/bin/bash

curl 'elasticsearch:9200/_cat/indices?v'

cd /usr/local/tomcat/daobs/dashboards/data/;
#rm index-dashboards-mapping.json && rm index-dashboards.json;

elasticdump \
  --input=http://elasticsearch:9200/.dashboards \
  --output=index-dashboards-mapping.json \
  --type=mapping

elasticdump \
  --input=http://elasticsearch:9200/.dashboards \
  --output=index-dashboards.json

cp -r $CATALINA_HOME/daobs/web/target/daobs/WEB-INF/datadir/* ${INSTALL_DASHBOARD_PATH}/daobs-data-dashboard

echo -e "\e[96mStart tomcat\e[0m"

cd $CATALINA_HOME
catalina.sh run
