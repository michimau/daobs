FROM yannart/docker-cerebro:0.7.2
#snuids/cerebro:v0.7.1

MAINTAINER doublebyte <joana.simoes@geocat.net>

ADD config/application.conf /application.conf

ADD docker-entry.sh /docker-entry.sh

RUN chmod a+x /docker-entry.sh

ENTRYPOINT [/docker-entry.sh]
