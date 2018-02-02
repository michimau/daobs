FROM docker.elastic.co/kibana/kibana:5.6.6

MAINTAINER doublebyte <joana.simoes@geocat.net>

ADD docker-entry.sh /
ADD wait-for-it.sh /
USER root
RUN chmod +x /docker-entry.sh /wait-for-it.sh
USER kibana

ADD config/kibana.yml /kibana.yml
