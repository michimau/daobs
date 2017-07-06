from eeacms/elk-elasticsearch

MAINTAINER doublebyte <joana.simoes@geocat.net>

RUN echo "Authorizing requests from the dashboard and kibana containers" && \
 sed -i 's#127.0.0.1, localhost#127.0.0.1, localhost, dashboard, kibana#g' /usr/share/elasticsearch/config/elasticsearch.yml && \
 sed -i 's# Accept all requests from localhost# Accept all requests from localhost, dashboard and kibana#g' /usr/share/elasticsearch/config/elasticsearch.yml

#USER root
#RUN yum -y install git

# Cloning the DAOS repo
#WORKDIR /
#RUN git clone https://github.com/INSPIRE-MIF/daobs.git

#USER elasticsearch
#WORKDIR /usr/share/elasticsearch

