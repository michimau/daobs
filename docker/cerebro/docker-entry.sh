#!/bin/bash

cp /docker-entrypoint.sh /opt/cerebro/conf/application.conf

sed "s#SECRET#$SECRET#g" -i /opt/cerebro/conf/application.conf
