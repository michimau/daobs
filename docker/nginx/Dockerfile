FROM nginx:stable-alpine

MAINTAINER doublebyte <joana.simoes@geocat.net>

ADD nginx-ssl.conf /etc/nginx/nginx.conf
ADD entry-point.sh /entry-point.sh
RUN chmod a+x /entry-point.sh

CMD ["/entry-point.sh"]
