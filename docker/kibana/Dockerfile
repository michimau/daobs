from  eeacms/elk-kibana

MAINTAINER doublebyte <joana.simoes@geocat.net>

#RUN echo "Updating Kibana configuration" && \
# sed -i 's@#server.basePath: ""@server.basePath: "/daobs"@g' /kibana/config/kibana.yml  && \
# sed -i 's@#kibana.index: ".kibana"@kibana.index: ".dashboards"@g' /kibana/config/kibana.yml && \
# sed -i 's@#elasticsearch.url: "http://localhost:9200@elasticsearch.url: "http://elasticsearch:9200@g' /kibana/config/kibana.yml  && \
# sed -i 's@echo "server.host: '0.0.0.0'"@server.host: "dashboard"@g' /kibana/config/kibana.yml 

COPY ./kibana.yml /kibana/config/kibana.yml

COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

COPY ./docker-entry.sh /docker-entry.sh
RUN chmod +x /docker-entry.sh
