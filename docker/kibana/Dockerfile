FROM docker.elastic.co/kibana/kibana:5.6.6

MAINTAINER doublebyte <joana.simoes@geocat.net>

ADD docker-entry.sh /docker-entry.sh
ADD wait-for-it.sh /wait-for-it.sh
RUN chmod a+x /wait-for-it.sh /docker-entry.sh

ADD config/kibana.yml /kibana.yml
