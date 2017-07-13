FROM ubuntu:wily

MAINTAINER doublebyte <joana.simoes@geocat.net>

ARG logstash_url
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends apache2 wget cron apt-transport-https apt-utils

#COPY ./apache2.conf /etc/apache2/apache2.conf

RUN echo "ServerName localhost" | tee /etc/apache2/conf-available/servername.conf && \
    a2enconf servername

#Piping apache logs to the console
RUN ln -sf /dev/stdout /var/log/apache2/access.log
RUN ln -sf /dev/stderr /var/log/apache2/error.log

CMD ["/usr/sbin/apache2ctl", "-D", "FOREGROUND"]
