#/bin/bash

cd /tmp 

curl https://raw.githubusercontent.com/doublebyte1/daobs/2.0.x/es/config/records.json --output records.json;
curl https://raw.githubusercontent.com/doublebyte1/daobs/2.0.x/es/config/indicators.json --output indicators.json;

echo -e "\e[91mLoad ES indexes\e[96m"
curl -X PUT http://elasticsearch:9200/records -d @records.json;
curl -X PUT http://elasticsearch:9200/indicators -d @indicators.json;

curl 'elasticsearch:9200/_cat/indices?v'

cd /usr/share/kibana/

/usr/share/kibana/bin/kibana
