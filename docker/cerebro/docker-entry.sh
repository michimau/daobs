#!/bin/bash

cp /application.conf /opt/cerebro/conf/application.conf

sed "s#SECRET#$SECRET#g" -i /opt/cerebro/conf/application.conf
