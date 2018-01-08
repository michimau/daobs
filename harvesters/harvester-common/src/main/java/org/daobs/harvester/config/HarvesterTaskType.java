package org.daobs.harvester.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for harvesterTaskType complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="harvesterTaskType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://daobs.org}harvesterType">
 *       &lt;sequence>
 *         &lt;element ref="{http://daobs.org}harvestedDate"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "harvesterTaskType", namespace = "http://daobs.org", propOrder = {
  "harvestedDate"
})
public class HarvesterTaskType
  extends HarvesterType {

  @XmlElement(namespace = "http://daobs.org", required = true)
  protected String harvestedDate;

  /**
   * Gets the value of the harvestedDate property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getHarvestedDate() {
    return harvestedDate;
  }

  /**
   * Sets the value of the harvestedDate property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setHarvestedDate(String value) {
    this.harvestedDate = value;
  }

}
