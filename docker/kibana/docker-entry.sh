#/bin/bash

#echo -e "\e[91mLoad defaults for Kibana\e[96m"

/kibana/bin/kibana && \
cd /kibana/data && \
elasticdump \
  --input=http://elasticsearch:9200/.dashboards \
  --output=index-dashboards-mapping.json \
  --type=mapping && \
elasticdump \
  --input=http://elasticsearch:9200/.dashboards \
  --output=index-dashboards.json

#curl 'elasticsearch:9200/_cat/indices?v'

