#!/bin/bash

cd /usr/share/kibana/

/usr/share/kibana/bin/kibana-plugin remove x-pack

/usr/share/kibana/bin/kibana

cp /kibana.yml /usr/share/kibana/config/kibana.yml

sed "s#BASEPATH#$BASEPATH#g" -i /usr/share/kibana/config/kibana.yml
sed "s#KIBANAUSERNAME#$KIBANAUSERNAMEO#g" -i /usr/share/kibana/config/kibana.yml
sed "s#KIBANAPASSWORD#$KIBANAPASSWORD#g" -i /usr/share/kibana/config/kibana.yml
