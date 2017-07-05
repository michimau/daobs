from eeacms/elk-elasticsearch

MAINTAINER doublebyte <joana.simoes@geocat.net>

RUN echo "Authorizing requests from the dashboard container" && \
 sed -i 's#127.0.0.1, localhost#127.0.0.1, localhost, dashboard#g' /usr/share/elasticsearch/config/elasticsearch.yml && \
 sed -i 's# Accept all requests from localhost# Accept all requests from localhost and dashboard#g' /usr/share/elasticsearch/config/elasticsearch.yml



