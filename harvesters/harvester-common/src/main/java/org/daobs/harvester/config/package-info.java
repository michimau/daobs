@XmlSchema(
  namespace = "http://daobs.org",
  xmlns = {
    @XmlNs(prefix = "daobs", namespaceURI = "http://daobs.org")
  },
  elementFormDefault = XmlNsForm.QUALIFIED
)
package org.daobs.harvester.config;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
