package org.daobs.harvester.config;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Created by juanl on 23/03/2017.
 */
public class HarvesterJaxbTest {

  @Test
  public void testHarvesterSerialization() throws JAXBException {
    String filterString = "<test>test-filter</test>";
    Harvesters harvesters = new Harvesters();
    HarvesterType harvester = new HarvesterType();
    harvester.setFilter(filterString);
    harvester.setFolder("folder");
    harvester.setName("harvester name");
    harvester.setNbOfRecordsPerPage(10);
    harvester.setPointOfTruthURLPattern("http://example.com/register/{{uuid}}");
    harvester.setServiceMetadata("http://example.com/record");
    harvester.setScope("ES");
    harvester.setUuid(UUID.randomUUID().toString());
    harvester.setUrl("http://example.com/service/csw");
    harvesters.getHarvester().add(harvester);

    JAXBContext jaxbContext = JAXBContext.newInstance(Harvesters.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    StringWriter sw = new StringWriter();
    jaxbMarshaller.marshal(harvesters, sw);

    assertTrue(sw.toString().contains("<daobs:filter>" + filterString + "</daobs:filter>"));
  }
}
