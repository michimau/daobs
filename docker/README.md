DAOBS orchestration
===================
This project orchestrates four containers, in order to serve the DAOBS dasboard application:
* [Dashboard](https://github.com/INSPIRE-MIF/daobs/): web app which collects information and configures indicators to generate reporting.
* [Elasticsearch](https://github.com/INSPIRE-MIF/daobs/tree/2.0.x/docker/elasticsearch): ElasticSearch image, with the readonlyrest plugin installed.
* [Kibana](https://github.com/elastic/kibana): Kibana 5 - official image from elastic.
* [Nginx](https://hub.docker.com/_/nginx/): web server; configured as a reverse proxy.
* [Cerebro](https://github.com/lmenezes/cerebro): tool to monitor the health of an elasticsearch cluster.

In order to optimize image size, whenever possible [alpine](https://alpinelinux.org/) based images are used as base images. The only image we build is the `dashboard`; all other images are pulled from the relevant repositories, and configured using configuration files on mounted volumes; the `kibana` image is pulled from the official elastic repositories, while `nginx` is pulled from the docker official repositories.

Both `elasticsearch` and `kibana` read their configurations from files generated by the top-level build. **Before running this composition, make sure to build this project** and pass it the relevant flags (by default using docker profile).

The containers are launched sequentially:
1. The first to start is `elasticsearch`.
2. The `kibana` container waits for ES, in order to start. It inserts the indexes, before launching the kibana app.
3. The `dashboard` container waits for kibana, in order to start. It backs up the indexes with ElasticDump, before launching the webserver.

The `nginx` container is not dependent from other containers and it is launched at the start.

The dashboard image
-------------------
The dashboard image is based on a maven-alpine linux image.
* It installs npm, tomcat and the ETF application.
* It builds the [daobs] project, using the src code on the top-level repository (`../`).
* It deploys the resulting war on Tomcat, using the application path as build argument.

The elasticsearch image
-----------------------
The elasticsearch image is based on the [official elasticsearch image](https://github.com/elastic/elasticsearch-docker/tree/5.5). It adds the [readonlyrest](https://readonlyrest.com/) plugin for supporting security & access control in Elasticsearch and Kibana.

INSTALL & RUN
=============
This is the configuration used to build and run the INSPIRE dashboards available at EEA. The process creates 2 images for the 2 types of dashboard app (sandbox and official), the build elasticsearch image with readonlyrest plugin and then run the composition.

```bash


# Download ETF
cd tasks/etf-validation-checker
mvn install -Drelax -DskipTests -Petf-download
cd ../..


# Build image for sandbox dashboard
mvn clean install -Peea-inspire-dashboard -Drelax -DskipTests

cd docker
docker build --build-arg WEBAPP_NAME=dashboard -t inspiremif/daobs-eea-dashboard-sandbox:latest .
cd ..


# Build image for official dashboard
mvn clean install -Peea-inspire-official -Drelax -DskipTests

cd docker
docker build --build-arg WEBAPP_NAME=official -t inspiremif/daobs-eea-dashboard-official:latest .


# Build elasticsearch image
cd elasticsearch
docker build -t inspiremif/elasticsearch:latest .
cd ..


# Start composition
sudo sysctl -w vm.max_map_count=262144
docker-compose -p dashboard-sandbox -f docker-compose-canonical.yml -f docker-compose-eea-dashboard-sandbox.yml up
docker-compose -p dashboard-official -f docker-compose-canonical.yml -f docker-compose-eea-dashboard-official.yml up


# publish images
docker push inspiremif/elasticsearch:latest
docker push inspiremif/daobs-eea-dashboard-sandbox:latest
docker push inspiremif/daobs-eea-dashboard-official:latest


```
If you just want to build the images locally, you can use the [provided convenience script](https://github.com/INSPIRE-MIF/daobs/blob/2.0.x/docker/build_images.sh):

```bash
./build_images.sh
```

In alternative if you don't want to build anything, you may just use the images wich are hosted on docker hub. To run the `sandbox dashboard` orchestration:

```bash
docker-compose -p dashboard-sandbox -f docker-compose-canonical.yml -f docker-compose-eea-dashboard-sandbox.yml up -d
```

To run the `official dashboard` orchestration:

```bash
docker-compose -p dashboard-official -f docker-compose-canonical.yml -f docker-compose-eea-dashboard-official.yml up -d
```

Or just use the provided convenience scripts [run_dashboard_sandbox.sh](https://github.com/INSPIRE-MIF/daobs/blob/2.0.x/docker/run_dashboard_sandbox.sh) and [run_dashboard_official.sh](https://github.com/INSPIRE-MIF/daobs/blob/2.0.x/docker/run_dashboard_official.sh).

Then open the applications with:

* https://localhost:81/dashboard
* https://localhost/official

Then for the official node, change the default account in user.properties in the dashboard-official volume and change it also for the kibana_rw user in readonlyrest.yml.

The two orchestrations can be run, side-by-side in the same machine, as they have different namespaces for container, volumes and networks names.

Advanced Configuration
----------------------
Nginx is running on port 80|81 (it uses different ports in the official and sandbox applications, in order to enable both of them to bind to the localhost). It can be configured as a proxy, using `nginx/nginx.conf`. The current configuration forwards all requests to `/daobs`, on port 80|81 to the dashboard container. For instance in the official application:

```
server {
  listen          80;
  server_name     daobs;

  location /daobs {
     proxy_pass http://dashboard:8080/daobs/;
  }
}
```

Elasticsearch is configured with three nodes, and two `discovery.zen.minimum_master_nodes`, to avoid the [split brain effect]( https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-node.html#split-brain). As by the default configuration, these nodes can all act as master, data and ingest nodes. When the cluster starts, we set the `elasticsearch` node as master node.

You can check the status of the elastic search cluster with:
```bash
curl http://[ES_IP]:9200/_cluster/health?pretty
```

`ES_IP` should be replaced by the IP address of the elasticsearch container. You can get the IPv4 addresses of the containers in a docker network with:

```bash
docker network inspect [NETWORK] | jq .[].Containers
```

Where `[NETWORK]` should be replaced by the network name, for instance, "dashboardofficial_network-dashboard-official".

In alternative, check the cerebro monitoring page at:

```
http://[CEREBRO_IP]:9000
```

When setting an elasticsearch host on cerebro, *make sure use the container name (for instance "official-es0"), or to its IP address*:
```
http://[ES_IP]:9200
```

Persisted Volumes
-----------------
The folder `/usr/share/elasticsearch/data` is persisted to a named volume, whose name depends on the node and orchestration. Unless explicitly removed, this volume will be persisted on the host folder: `var/lib/docker/volumes/[NAMED_VOLUME]/_data`, where [NAMED_VOLUME] should be replaced by the actual volume name.
The folder `/daobs-data-dir/`, which is mapped in the dockerfile to environmental variable `INSTALL_DASHBOARD_PATH`, is persisted in a volume whose name depends on the orchestration (e.g.: "dashboardsandbox_dashboard-sandbox-dir", "dashboardofficial_dashboard-official-dir")`. If you change `INSTALL_DASHBOARD_PATH` on the dockerfile, remember to also change the mapping on docker-compose, or your data directory won't be persisted.

Security
--------
Only the web container (nginx) publishes its ports (either 80 or 443). All other containers communicate *only* using docker's internal network.

On this orchestration, **SSL is enabled** by default.
In order to setup SSL with your own certificates you need to export some environment variables, with the **location** and **name** of your private and public keys:

* `SSL_CERTS_DIR`: path on disk of the public key (without a trailing `/` on the end).
* `SSL_PUB`: name of the file which stores the public key.
* `SSL_KEY_DIR`: path on disk of the private key (without a trailing `/` on the end).
* `SSL_PRIV`: name of the file which stores the private key.

If you don't have any keys, you may leave these variables empty: a runtime script will generate **self-signed certificates**, which will enable you to use SSL on a development environment. Self-signed certificates will issue an warning in the browser and need to be trusted by the user. It is not recommended to use self-signed certificates in production environments.

![Generated self-signed certificate](https://raw.githubusercontent.com/INSPIRE-MIF/daobs/2.0.x/docker/ssl.png)

License
========
View [license information](https://github.com/INSPIRE-MIF/daobs/blob/2.0.x/LICENCE.md) for the software contained in this image.
