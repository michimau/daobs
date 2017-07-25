/**
 * Copyright 2014-2016 European Environment Agency
 *
 * Licensed under the EUPL, Version 1.1 or – as soon
 * they will be approved by the European Commission -
 * subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.daobs.index;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetAddress;

/**
 * Create a bean providing a connection to ES
 * Created by francois on 30/09/14.
 */
public class EsClientBean implements InitializingBean {

  private static EsClientBean instance;

  private TransportClient client;
  private String serverUrl;
  private String collection;
  private String username;
  private String password;

  public String getServerHost() {
    return esHost;
  }

  public void setServerHost(String serverHost) {
    this.esHost = serverHost;
  }

  private String esHost;

  /**
   * Get Solr server.
   * @return Return the bean instance
   */
  public static EsClientBean get() {
    return instance;
  }

  /**
   * The first time this method is called, ping the
   * client to check connection status.
   *
   * @return The index instance.
   */
  public TransportClient getClient() throws Exception {
    return client;
  }


  /**
   * Connect to the index, ping the client
   * to check connection and set the instance.
   *
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (serverUrl != null) {
      Settings settings = Settings.builder()
          .put("client.transport.sniff", false)
          .put("client.transport.ignore_cluster_name", true).build();

      // Set nodes to localhost or docker elasticsearch container
      client = new PreBuiltTransportClient(settings)
          .addTransportAddress(new InetSocketTransportAddress(
          InetAddress.getByName(esHost), 9300));

      synchronized (EsClientBean.class) {
        instance = this;
      }
    } else {
      throw new Exception(String.format("No index URL defined in %s. "
          + "Check bean configuration.", this.serverUrl));
    }
  }

  /**
   * Return the index URL.
   */
  public String getServerUrl() {
    return serverUrl;
  }

  /**
   * The index URL.
   */
  public EsClientBean setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
    return this;
  }

  /**
   * Return index username for credentials.
   */
  public String getUsername() {
    return username;
  }

  /**
   * The index credentials username.
   */
  public EsClientBean setUsername(String username) {
    this.username = username;
    return this;
  }

  /**
   * Return index password for credentials.
   */
  public String getPassword() {
    return password;
  }

  /**
   * The index credentials password.
   */
  public EsClientBean setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }
}
