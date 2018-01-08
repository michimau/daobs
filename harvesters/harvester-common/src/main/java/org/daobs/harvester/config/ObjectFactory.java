package org.daobs.harvester.config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.daobs.harvester.config package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private final static QName _Name_QNAME = new QName("http://daobs.org", "name");
  private final static QName _NbOfRecordsPerPage_QNAME = new QName("http://daobs.org", "nbOfRecordsPerPage");
  private final static QName _Tag_QNAME = new QName("http://daobs.org", "tag");
  private final static QName _Harvester_QNAME = new QName("http://daobs.org", "harvester");
  private final static QName _Filter_QNAME = new QName("http://daobs.org", "filter");
  private final static QName _PointOfTruthURLPattern_QNAME = new QName("http://daobs.org", "pointOfTruthURLPattern");
  private final static QName _Folder_QNAME = new QName("http://daobs.org", "folder");
  private final static QName _Scope_QNAME = new QName("http://daobs.org", "scope");
  private final static QName _ServiceMetadata_QNAME = new QName("http://daobs.org", "serviceMetadata");
  private final static QName _Uuid_QNAME = new QName("http://daobs.org", "uuid");
  private final static QName _Url_QNAME = new QName("http://daobs.org", "url");
  private final static QName _HarvesterTask_QNAME = new QName("http://daobs.org", "harvesterTask");
  private final static QName _HarvestedDate_QNAME = new QName("http://daobs.org", "harvestedDate");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.daobs.harvester.config
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link HarvesterTaskType }
   */
  public HarvesterTaskType createHarvesterTaskType() {
    return new HarvesterTaskType();
  }

  /**
   * Create an instance of {@link Harvesters }
   */
  public Harvesters createHarvesters() {
    return new Harvesters();
  }

  /**
   * Create an instance of {@link HarvesterType }
   */
  public HarvesterType createHarvesterType() {
    return new HarvesterType();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "name")
  public JAXBElement<String> createName(String value) {
    return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "nbOfRecordsPerPage")
  public JAXBElement<Integer> createNbOfRecordsPerPage(Integer value) {
    return new JAXBElement<Integer>(_NbOfRecordsPerPage_QNAME, Integer.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "tag")
  public JAXBElement<String> createTag(String value) {
    return new JAXBElement<String>(_Tag_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link HarvesterType }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "harvester")
  public JAXBElement<HarvesterType> createHarvester(HarvesterType value) {
    return new JAXBElement<HarvesterType>(_Harvester_QNAME, HarvesterType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "filter")
  public JAXBElement<Object> createFilter(Object value) {
    return new JAXBElement<Object>(_Filter_QNAME, Object.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "pointOfTruthURLPattern")
  public JAXBElement<String> createPointOfTruthURLPattern(String value) {
    return new JAXBElement<String>(_PointOfTruthURLPattern_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "folder")
  public JAXBElement<String> createFolder(String value) {
    return new JAXBElement<String>(_Folder_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "scope")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  public JAXBElement<String> createScope(String value) {
    return new JAXBElement<String>(_Scope_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "serviceMetadata")
  public JAXBElement<String> createServiceMetadata(String value) {
    return new JAXBElement<String>(_ServiceMetadata_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "uuid")
  public JAXBElement<String> createUuid(String value) {
    return new JAXBElement<String>(_Uuid_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "url")
  public JAXBElement<String> createUrl(String value) {
    return new JAXBElement<String>(_Url_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link HarvesterTaskType }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "harvesterTask")
  public JAXBElement<HarvesterTaskType> createHarvesterTask(HarvesterTaskType value) {
    return new JAXBElement<HarvesterTaskType>(_HarvesterTask_QNAME, HarvesterTaskType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://daobs.org", name = "harvestedDate")
  public JAXBElement<String> createHarvestedDate(String value) {
    return new JAXBElement<String>(_HarvestedDate_QNAME, String.class, null, value);
  }

}
