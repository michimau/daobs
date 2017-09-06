DAOBS orchestration
===================
This project orchestrates four containers, in order to serve the DAOBS dasboard application:
* [Dashboard](https://github.com/INSPIRE-MIF/daobs/): web app which collects information and configures indicators to generate reporting.
* [Elasticsearch](https://github.com/elastic/elasticsearch-docker/tree/5.5): ElasticSearch 5 - official image from elastic.
* [Kibana](https://github.com/elastic/kibana): Kibana 5 - official image from elastic.
* [Nginx](https://hub.docker.com/_/nginx/): web server; configured as a reverse proxy.

In order to optimize image size, whenever possible [alpine](https://alpinelinux.org/) based images are used as base images. The only image we build is the `dashboard`; all other images are pulled from the relevant repositories, and configured using configuration files on mounted volumes; the `elasticsearch` and `kibana` images are pulled from the official elastic repositories, while `nginx` is pulled from the docker official repositories.

INSTALL & RUN
-------------
The composition can be run, by typing at the root of this project:
```bash
docker-compose up -d
```
The containers are launched sequentially:
1. The first to start is `elasticsearch`.
2. The `kibana` container waits for ES, in order to start. It inserts the indexes, before launching the kibana app.
3. The `dashboard` container waits for kibana, in order to start. It backs up the indexes with ElasticDump, before launching the webserver.

The `nginx` container is not dependent from other containers and it is launched at the start.

The dashboard application is available at:
```bash
http://localhost/daobs
```

The dashboard container
-----------------------
The dashboard container is based on a maven-alpine linux image.
* It installs npm, tomcat and the ETF application.
* It builds the [daobs] project, using the src code on the top-level repository (`../`).
*  It deploys the resulting war on Tomcat. The application is served at `/usr/local/tomcat/webapps/daobs`.

Configuration
-------------
The dashboard Dockerfile contains two environmental variables: `INSTALL_ETF_PATH` and `INSTALL_DASHBOARD_PATH`. The war is built with these parameters:
```java
RUN mvn install \
 -DskipTests -Drelax -gs /usr/share/maven/ref/settings-docker.xml \
 -Dwebapp.context=/daobs  \
 -Dwebapp.rootUrl=/daobs/ \
 -Des.host=elasticsearch \
 -Des.url=http://elasticsearch:9200 \
 -Dkb.url=http://kibana:5601 \
 -Ddata.dir=${INSTALL_DASHBOARD_PATH}/daobs-data-dashboard \
 -Detf.installation.path=${INSTALL_ETF_PATH}
```

Elasticsearch reads its configuration from `./elasticsearch/elasticsearch.yml`.

Kibana reads its configuration from `./kibana/kibana.yml`. XPack is disabled in both services. Additionally, these settings are relevant on `kibana.yml`:
```json
server.basePath: "/daobs/dashboard"
kibana.index: ".dashboards"
```

Nginx is running on port 80. It can be configured as a proxy, using `nginx/nginx.conf`. The current configuration forwards all requests to `/daobs`, on port 80 to the dashboard container.

```
server {
  listen          80;
  server_name     daobs;

  location /daobs {
     proxy_pass http://dashboard:8080/daobs/;
  }
}
```

Persisted Volumes
-----------------
The folder `/usr/share/elasticsearch/data` is persisted to a named volume called `esdata`. Unless explicitly removed, this volume will be persisted on the host folder: `var/lib/docker/volumes/docker_esdata/_data`.
The folder `/daobs-data-dir/`, which is mapped in the dockerfile to environmental variable `INSTALL_DASHBOARD_PATH`, is persisted in a volume called: `daobsdatadir`. If you change `INSTALL_DASHBOARD_PATH` on the dockerfile, remember to also change the mapping on docker-compose, or your data directory won't be persisted.
Depending on your docker configuration, the volumes are stored on the host on this path `/var/lib/docker/volumes/`.

Security
--------
Only the web container (nginx) publishes its ports (either 80 or 443). All other containers communicate *only* using docker's internal network. If you need to use kibana or elasticsearch **directly**, you just need to uncomment the exposed ports on docker-compose.

To enable SSL, you need to export some environment variables, with the **location** and **name** of your private and public keys:

* `SSL_CERTS_DIR`: path on disk of the public key (without a trailing `/` on the end).
* `SSL_PUB`: name of the file which stores the public key.
* `SSL_KEY_DIR`: path on disk of the private key (without a trailing `/` on the end).
* `SSL_PRIV`: name of the file which stores the private key.

You will also need to point nginx to the SSL enabled configuration file. The nginx section of docker-compose should look like this:

```json
nginx:
  hostname: nginx
  image: nginx:stable-alpine
  ports:
    - "80:80"
    - "443:443"
  volumes:
    - ${SSL_CERTS_DIR}/${SSL_PUB}:/etc/nginx/certs/cert.crt
    - ${SSL_KEY_DIR}/${SSL_PRIV}:/etc/nginx/private/priv.key
    #- ./nginx/nginx.conf:/etc/nginx/nginx.conf
    - ./nginx/nginx-ssl.conf:/etc/nginx/nginx.conf
    - ./nginx/wait-for-it.sh:/wait-for-it.sh
  depends_on:
    - "dashboard"
```

The default configuration, does **not** enable SSL:

```json
nginx:
  hostname: nginx
  image: nginx:stable-alpine
  ports:
    - "80:80"
    - "443:443"
  volumes:
    - ${SSL_CERTS_DIR}/${SSL_PUB}:/etc/nginx/certs/cert.crt
    - ${SSL_KEY_DIR}/${SSL_PRIV}:/etc/nginx/private/priv.key
    - ./nginx/nginx.conf:/etc/nginx/nginx.conf
    #- ./nginx/nginx-ssl.conf:/etc/nginx/nginx.conf
    - ./nginx/wait-for-it.sh:/wait-for-it.sh
  depends_on:
    - "dashboard"
```

Known Issues
------------
When loading the dashboard, there is this message, which means kibana does not detect any indexes: *There is currently no dashboard available in the system. Use the above menu to load some or move to the dashboard configuration to upload some new ones.*

License
========
#TODO
