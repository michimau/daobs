from  eeacms/elk-kibana

MAINTAINER doublebyte <joana.simoes@geocat.net>

RUN echo "Updating Kibana configuration" && \
 #sed -i 's@#server.basePath: ""@server.basePath: "http://dashboard:8080/daobs/dashboard"@g' /kibana/config/kibana.yml  && \
 sed -i 's@#server.basePath: ""@server.basePath: "/daobs/dashboard"@g' /kibana/config/kibana.yml  && \
 sed -i 's@#kibana.index: ".kibana"@kibana.index: ".kibana"@g' /kibana/config/kibana.yml 

RUN npm install elasticdump -g

COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

COPY ./docker-entry.sh /docker-entry.sh
RUN chmod +x /docker-entry.sh
