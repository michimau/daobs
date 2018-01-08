/**
 * Copyright 2014-2016 European Environment Agency
 * <p>
 * Licensed under the EUPL, Version 1.1 or – as soon
 * they will be approved by the European Commission -
 * subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 * <p>
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 * <p>
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package org.daobs.harvester.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by juanl on 22/03/2017.
 */
public class FilterHandler implements DomHandler<String, StreamResult> {

  private static final String FILTER_START_TAG =
    "<daobs:filter xmlns:daobs=\"http://daobs.org\">";
  private static final String FILTER_END_TAG = "</daobs:filter>";


  /**
   * When a JAXB provider needs to unmarshal a part of a document into an
   * infoset representation, it first calls this method to create a
   * {@link Result} object.
   *
   * <p>
   * A JAXB provider will then send a portion of the XML
   * into the given result. Such a portion always form a subtree
   * of the whole XML document rooted at an element.
   *
   * @param errorHandler if any error happens between the invocation of this method
   *                     and the invocation of {@link #getElement(Result)}, they
   *                     must be reported to this handler.
   *
   *                     The caller must provide a non-null error handler.
   *
   *                     The {@link Result} object created from this method
   *                     may hold a reference to this error handler.
   * @return null if the operation fails. The error must have been reported
   * to the error handler.
   */
  @Override
  public StreamResult createUnmarshaller(ValidationEventHandler errorHandler) {
    StringWriter xmlWriter = new StringWriter();
    return new StreamResult(xmlWriter);
  }

  /**
   * Once the portion is sent to the {@link Result}. This method is called
   * by a JAXB provider to obtain the unmarshalled element representation.
   *
   * <p>
   * Multiple invocations of this method may return different objects.
   * This method can be invoked only when the whole sub-tree are fed
   * to the {@link Result} object.
   *
   * @param rt The {@link Result} object created by {@link #createUnmarshaller(ValidationEventHandler)}.
   * @return null if the operation fails. The error must have been reported
   * to the error handler.
   */
  @Override
  public String getElement(StreamResult rt) {
    String xml = rt.getWriter().toString();
    if (StringUtils.isEmpty(xml)) {
      return "";
    }

    try {
      return parseAndFormatXmlString(xml, true);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    }
    return "";
  }

  private String parseAndFormatXmlString(String xml, boolean checkDaobsFilterTag) throws ParserConfigurationException, SAXException, IOException, TransformerException {
    StringWriter sw = new StringWriter();
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = db.parse(new InputSource(new StringReader(xml)));
    Transformer t = TransformerFactory.newInstance().newTransformer();
    t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    t.setOutputProperty(OutputKeys.INDENT, "yes");

    Node node = doc.getFirstChild();
    if (node != null) {
      if (checkDaobsFilterTag && node.getNodeName().equals("daobs:filter")) {
        // Skip the <daobs:filter> tag and unmarshall the content
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
          Node child = children.item(i);
          t.transform(new DOMSource(child), new StreamResult(sw));
        }
      } else if (!checkDaobsFilterTag) {
        // unmarshall the node
        t.transform(new DOMSource(node), new StreamResult(sw));
      } else {
        return "";
      }
    }
    return sw.toString();
  }

  /**
   * This method is called when a JAXB provider needs to marshal an element
   * to XML.
   *
   * <p>
   * If non-null, the returned {@link Source} must contain a whole document
   * rooted at one element, which will then be weaved into a bigger document
   * that the JAXB provider is marshalling.
   *
   * @param n
   * @param errorHandler Receives any errors happened during the process of converting
   *                     an element into a {@link Source}.
   *
   *                     The caller must provide a non-null error handler.
   * @return null if there was an error. The error should have been reported
   * to the handler.
   */
  @Override
  public Source marshal(String n, ValidationEventHandler errorHandler) {
    try {
      String filterContent = parseAndFormatXmlString(n, false);


      String xml = FILTER_START_TAG + filterContent.trim() + FILTER_END_TAG;
      StringReader xmlReader = new StringReader(xml);
      return new StreamSource(xmlReader);
    } catch (Exception e) {
      errorHandler.handleEvent(new ValidationEventImpl(ValidationEvent.WARNING,
        "filter is not valid XML: " + e.getMessage(),
        new ValidationEventLocatorImpl(n)));
      throw new RuntimeException(e);
    }
    //return new StreamSource(new StringReader(FILTER_START_TAG + FILTER_END_TAG));
  }
}
