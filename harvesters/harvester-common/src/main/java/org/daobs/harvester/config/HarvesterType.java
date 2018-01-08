package org.daobs.harvester.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for harvesterType complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="harvesterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://daobs.org}uuid"/>
 *         &lt;element ref="{http://daobs.org}scope"/>
 *         &lt;element ref="{http://daobs.org}folder"/>
 *         &lt;element ref="{http://daobs.org}tag" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://daobs.org}name"/>
 *         &lt;element ref="{http://daobs.org}url"/>
 *         &lt;element ref="{http://daobs.org}filter" minOccurs="0"/>
 *         &lt;element ref="{http://daobs.org}nbOfRecordsPerPage" minOccurs="0"/>
 *         &lt;element ref="{http://daobs.org}pointOfTruthURLPattern"/>
 *         &lt;element ref="{http://daobs.org}serviceMetadata"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "harvesterType", namespace = "http://daobs.org", propOrder = {
  "uuid",
  "scope",
  "folder",
  "tag",
  "name",
  "url",
  "filter",
  "nbOfRecordsPerPage",
  "pointOfTruthURLPattern",
  "serviceMetadata"
})
@XmlSeeAlso({
  HarvesterTaskType.class
})
@XmlRootElement(name = "harvester", namespace = "http://daobs.org")
public class HarvesterType implements Serializable {
  private static final long serialVersionUID = 7526471155622776147L;

  @XmlElement(namespace = "http://daobs.org", required = true)
  protected String uuid;
  @XmlElement(namespace = "http://daobs.org", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String scope;
  @XmlElement(namespace = "http://daobs.org", required = true)
  protected String folder;
  @XmlElement(namespace = "http://daobs.org")
  protected List<String> tag;
  @XmlElement(namespace = "http://daobs.org", required = true)
  protected String name;
  @XmlElement(namespace = "http://daobs.org", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String url;
//  @XmlElement(namespace = "http://daobs.org")
  @XmlAnyElement(value = FilterHandler.class)
  protected String filter;
  @XmlElement(namespace = "http://daobs.org")
  protected Integer nbOfRecordsPerPage;
  @XmlElement(namespace = "http://daobs.org", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String pointOfTruthURLPattern;
  @XmlElement(namespace = "http://daobs.org", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String serviceMetadata;

  /**
   * Gets the value of the uuid property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Sets the value of the uuid property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setUuid(String value) {
    this.uuid = value;
  }

  /**
   * Gets the value of the scope property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getScope() {
    return scope;
  }

  /**
   * Sets the value of the scope property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setScope(String value) {
    this.scope = value;
  }

  /**
   * Gets the value of the folder property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getFolder() {
    return folder;
  }

  /**
   * Sets the value of the folder property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setFolder(String value) {
    this.folder = value;
  }

  /**
   * Gets the value of the tag property.
   * <p>
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the tag property.
   * <p>
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getTag().add(newItem);
   * </pre>
   * <p>
   * <p>
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   */
  public List<String> getTag() {
    if (tag == null) {
      tag = new ArrayList<String>();
    }
    return this.tag;
  }

  /**
   * Sets the value of the tag property.
   */
  public void setTag(List<String> tags) {
    this.tag = tags;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the value of the url property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the value of the url property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setUrl(String value) {
    this.url = value;
  }

  /**
   * Gets the value of the filter property.
   *
   * @return possible object is
   * {@link Object }
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Sets the value of the filter property.
   *
   * @param value allowed object is
   *              {@link Object }
   */
  public void setFilter(String value) {
    this.filter = value;
  }

  /**
   * If not provided, the harvester config parameter is used.
   *
   * @return possible object is
   * {@link Integer }
   */
  public Integer getNbOfRecordsPerPage() {
    return nbOfRecordsPerPage;
  }

  /**
   * Sets the value of the nbOfRecordsPerPage property.
   *
   * @param value allowed object is
   *              {@link Integer }
   */
  public void setNbOfRecordsPerPage(Integer value) {
    this.nbOfRecordsPerPage = value;
  }

  /**
   * Gets the value of the pointOfTruthURLPattern property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getPointOfTruthURLPattern() {
    return pointOfTruthURLPattern;
  }

  /**
   * Sets the value of the pointOfTruthURLPattern property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setPointOfTruthURLPattern(String value) {
    this.pointOfTruthURLPattern = value;
  }

  /**
   * Gets the value of the serviceMetadata property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getServiceMetadata() {
    return serviceMetadata;
  }

  /**
   * Sets the value of the serviceMetadata property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setServiceMetadata(String value) {
    this.serviceMetadata = value;
  }

}
