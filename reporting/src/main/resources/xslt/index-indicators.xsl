<?xml version="1.0" encoding="UTF-8"?>

<!--
  ~ Copyright 2014-2016 European Environment Agency
  ~
  ~ Licensed under the EUPL, Version 1.1 or – as soon
  ~ they will be approved by the European Commission -
  ~ subsequent versions of the EUPL (the "Licence");
  ~ You may not use this work except in compliance
  ~ with the Licence.
  ~ You may obtain a copy of the Licence at:
  ~
  ~ https://joinup.ec.europa.eu/community/eupl/og_page/eupl
  ~
  ~ Unless required by applicable law or agreed to in
  ~ writing, software distributed under the Licence is
  ~ distributed on an "AS IS" basis,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  ~ either express or implied.
  ~ See the Licence for the specific language governing
  ~ permissions and limitations under the Licence.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:monitoring="http://inspire.jrc.ec.europa.eu/monitoringreporting/monitoring"
                xmlns:daobs="http://daobs.org"
                xmlns:saxon="http://saxon.sf.net/"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                extension-element-prefixes="saxon"
                exclude-result-prefixes="#all"
                version="2.0">

  <xsl:output indent="yes"/>

  <xsl:include href="constant.xsl"/>
  <xsl:include href="metadata-inspire-constant.xsl"/>

  <xsl:variable name="isDaobsFormat"
                select="count(daobs:reporting) > 0"
                as="xs:boolean"/>

  <!-- Compute ISO date from a INSPIRE date node
    eg. <monitoringDate>
          <day>08</day>
          <month>04</month>
          <year>2016</year>
        </monitoringDate>
    return 2016-04-08T12:00:00
  -->
  <xsl:function name="daobs:get-date" as="xs:string">
    <xsl:param name="dateNode" as="node()"/>
    <xsl:variable name="year"
                  select="$dateNode/year"/>
    <!-- Format date properly. Sometimes month is written
using one character or two. Prepend 0 when needed. -->
    <xsl:variable name="month"
                  select="if ($dateNode/month = '00' or $dateNode/month = '0') then '12'
                        else if (string-length($dateNode/month) = 1)
                        then concat('0', $dateNode/month)
                        else if (string-length($dateNode/month) = 2)
                        then $dateNode/month
                        else '12'"/>
    <xsl:variable name="day"
                  select="if ($dateNode/day = '00' or $dateNode/day = '0') then '31'
                        else if (string-length($dateNode/day) = 1)
                        then concat('0', $dateNode/day)
                        else if (string-length($dateNode/day) = 2)
                        then $dateNode/day
                        else '31'"/>
    <xsl:value-of select="concat(
                            $year, '-', $month, '-', $day,
                            'T12:00:00')"/>
  </xsl:function>


  <xsl:variable name="reportingDate"
                select="if ($isDaobsFormat)
                        then xs:dateTime(
                          if (contains(/daobs:reporting/@dateTime, '.'))
                          then substring-before(/daobs:reporting/@dateTime, '.')
                          else /daobs:reporting/@dateTime)
                        else daobs:get-date(/monitoring:Monitoring/documentYear)"/>
  <xsl:variable name="reportingDateSubmission"
                select="if ($isDaobsFormat)
                        then xs:dateTime(
                          if (contains(/daobs:reporting/@dateTime, '.'))
                          then substring-before(/daobs:reporting/@dateTime, '.')
                          else /daobs:reporting/@dateTime)
                        else daobs:get-date(/monitoring:Monitoring/MonitoringMD/monitoringDate)"/>

  <xsl:variable name="reportingYear"
                select="if ($isDaobsFormat)
                        then format-dateTime($reportingDate, '[Y0001]')
                        else /monitoring:Monitoring/documentYear/year"/>

  <xsl:variable name="reportingTerritory"
                select="if ($isDaobsFormat)
                        then /daobs:reporting/@scopeId
                        else /monitoring:Monitoring/memberState"/>

  <xsl:variable name="reportIdentifier"
                select="if ($isDaobsFormat)
                        then /daobs:reporting/@id
                        else 'inspire'"/>


  <xsl:template match="/">
    <add>
      <xsl:message>
        <xsl:text>Indexing indicators for </xsl:text>
        <xsl:value-of select="$reportingTerritory"/>
        <xsl:value-of select="concat(' Report: ', $reportIdentifier, ' (', $reportingDate, ') - DAOBS format: ', $isDaobsFormat)"/>
      </xsl:message>

      <xsl:apply-templates select="
                //daobs:reporting|
                //daobs:reporting/daobs:variables/daobs:variable|
                //daobs:reporting/daobs:indicators/daobs:indicator|
                //MonitoringMD|//Indicators/*|
                //RowData/SpatialDataService/NetworkService/userRequest|
                //RowData/SpatialDataSet/Coverage/(relevantArea|actualArea)|
                //RowData/SpatialDataService|
                //RowData/SpatialDataSet"/>
    </add>
  </xsl:template>

  <xsl:template match="MonitoringMD|daobs:reporting">
    <doc>
      <field name="id">
        <xsl:value-of
          select="concat('monitoring', $reportIdentifier, $reportingTerritory, $reportingDate)"/>
      </field>
      <field name="documentType">monitoring</field>
      <field name="territory">
        <xsl:value-of select="$reportingTerritory"/>
      </field>
      <field name="reportingDateSubmission">
        <xsl:value-of select="$reportingDateSubmission"/>
      </field>
      <field name="reportingDate">
        <xsl:value-of select="$reportingDate"/>
      </field>
      <field name="reportingYear">
        <xsl:value-of select="$reportingYear"/>
      </field>
      <field name="contact">{
        "org": "<xsl:value-of select="replace(organizationName,
                                        $doubleQuote, $escapedDoubleQuote)"/>",
        "email": "<xsl:value-of select="email"/>"
        }
      </field>

      <xsl:message>Report</xsl:message>
      <xsl:apply-templates mode="indicatorValue"
                           select="
                daobs:variables/daobs:variable|
                daobs:indicators/daobs:indicator|
                //Indicators/*|
                //RowData/SpatialDataService/NetworkService/userRequest|
                //RowData/SpatialDataSet/Coverage/(relevantArea|actualArea)"/>

      <field name="isOfficial">true</field>
    </doc>
  </xsl:template>

  <xsl:template match="Indicators/*">
    <xsl:variable name="indicatorType" select="local-name()"/>
    <xsl:for-each select="descendant::*[count(*) = 0 and text() != '']">
      <xsl:variable name="indicatorIdentifier" select="local-name()"/>
      <doc>
        <field name="id">
          <xsl:value-of
            select="concat('indicator', $reportIdentifier, $indicatorIdentifier,
                $reportingDate, $reportingTerritory)"/>
        </field>
        <field name="documentType">indicator</field>
        <field name="indicatorType">
          <xsl:value-of select="$indicatorType"/>
        </field>
        <field name="indicatorName">
          <xsl:value-of select="$indicatorIdentifier"/>
        </field>
        <xsl:if test="text() != ''">
          <field name="indicatorValue">
            <xsl:value-of select="text()"/>
          </field>
        </xsl:if>
        <field name="territory">
          <xsl:value-of select="$reportingTerritory"/>
        </field>
        <field name="reportingDateSubmission">
          <xsl:value-of select="$reportingDateSubmission"/>
        </field>
        <field name="reportingDate">
          <xsl:value-of select="$reportingDate"/>
        </field>
        <field name="reportingYear">
          <xsl:value-of select="$reportingYear"/>
        </field>
      </doc>
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="daobs:variable|daobs:indicator"
                mode="indicatorValue">
    <xsl:variable name="indicatorType" select="local-name()"/>
    <xsl:variable name="indicatorIdentifier" select="@id"/>

    <field name="indicator{$indicatorIdentifier}">
      <xsl:value-of select="daobs:value"/>
    </field>
  </xsl:template>

  <xsl:template match="*"
                mode="indicatorValue">
  </xsl:template>

  <xsl:template match="daobs:variable|daobs:indicator">
    <xsl:variable name="indicatorType" select="local-name()"/>
    <xsl:variable name="indicatorIdentifier" select="@id"/>

    <!--<xsl:message>  Indicator <xsl:value-of select="concat($indicatorIdentifier, ': ', daobs:value)"/></xsl:message>-->
    <doc>
      <field name="id">
        <xsl:value-of
          select="concat('indicator', $reportIdentifier, $indicatorIdentifier,
              $reportingDate, $reportingTerritory)"/>
      </field>
      <field name="documentType">indicator</field>
      <field name="indicatorType">
        <xsl:value-of select="$indicatorType"/>
      </field>
      <field name="indicatorName">
        <xsl:value-of select="$indicatorIdentifier"/>
      </field>
      <xsl:if test="daobs:value != ''">
        <field name="indicatorValue">
          <xsl:value-of select="daobs:value"/>
        </field>
      </xsl:if>
      <field name="territory">
        <xsl:value-of select="$reportingTerritory"/>
      </field>
      <field name="reportingDateSubmission">
        <xsl:value-of select="$reportingDateSubmission"/>
      </field>
      <field name="reportingDate">
        <xsl:value-of select="$reportingDate"/>
      </field>
      <field name="reportingYear">
        <xsl:value-of select="$reportingYear"/>
      </field>
    </doc>
  </xsl:template>

  <!-- Index row data like metadata records -->
  <xsl:template match="SpatialDataService">
    <doc>
      <xsl:variable name="uuid"
                    select="if (uuid != '') then uuid else 'nouuid'"/>
      <field name="id">
        <xsl:value-of
          select="concat('monitoring',
                        $reportingTerritory, $reportingDate,
                        $uuid, '-', position())"/>
      </field>
      <field name="metadataIdentifier">
        <xsl:value-of select="$uuid"/>
      </field>
      <field name="documentType">monitoringMetadata</field>
      <field name="resourceType">service</field>
      <field name="territory">
        <xsl:value-of select="$reportingTerritory"/>
      </field>
      <field name="reportingDateSubmission">
        <xsl:value-of select="$reportingDateSubmission"/>
      </field>
      <field name="reportingDate">
        <xsl:value-of select="$reportingDate"/>
      </field>
      <field name="reportingYear">
        <xsl:value-of select="$reportingYear"/>
      </field>
      <field name="resourceTitle">
        <xsl:value-of select="name"/>
      </field>
      <field name="custodianOrgForResource">
        <xsl:value-of select="respAuthority"/>
      </field>
      <xsl:for-each-group select="Themes/*" group-by="name()">
        <field name="inspireAnnex">
          <xsl:value-of
            select="if (name() = 'AnnexIII') then 'iii' else if (name() = 'AnnexII') then 'ii' else 'i'"/>
        </field>
      </xsl:for-each-group>
      <xsl:for-each select="Themes/*">
        <xsl:variable name="themeKey" select="."/>
        <field name="inspireTheme">
          <xsl:value-of
            select="$inspireThemesMap/map[@monitoring = $themeKey]/@theme"/>
        </field>
      </xsl:for-each>


      <field name="isAboveThreshold">
        <xsl:value-of select="MdServiceExistence/mdConformity = 'true'"/>
      </field>
      <field name="harvesterUuid">
        <xsl:value-of select="MdServiceExistence/discoveryAccessibilityUuid"/>
      </field>
      <field name="linkUrl">
        <xsl:value-of select="NetworkService/URL"/>
      </field>
      <field name="serviceType">
        <xsl:value-of select="NetworkService/NnServiceType"/>
      </field>
      <field name="inspireConformResource">
        <xsl:value-of select="NetworkService/nnConformity = 'true'"/>
      </field>
    </doc>
  </xsl:template>


  <xsl:template match="SpatialDataSet">
    <doc>
      <xsl:variable name="uuid"
                    select="if (uuid != '') then uuid else 'nouuid'"/>
      <!-- Append position to all uuids to make them unique -->
      <field name="id">
        <xsl:value-of
          select="concat('monitoring',
                        $reportingTerritory, $reportingDate,
                        $uuid, '-', position())"/>
      </field>
      <field name="metadataIdentifier">
        <xsl:value-of select="$uuid"/>
      </field>
      <field name="documentType">monitoringMetadata</field>
      <field name="resourceType">dataset</field>
      <field name="territory">
        <xsl:value-of select="$reportingTerritory"/>
      </field>
      <field name="reportingDateSubmission">
        <xsl:value-of select="$reportingDateSubmission"/>
      </field>
      <field name="reportingDate">
        <xsl:value-of select="$reportingDate"/>
      </field>
      <field name="reportingYear">
        <xsl:value-of select="$reportingYear"/>
      </field>
      <field name="resourceTitle">
        <xsl:value-of select="name"/>
      </field>
      <field name="custodianOrgForResource">
        <xsl:value-of select="respAuthority"/>
      </field>
      <xsl:for-each-group select="Themes/*" group-by="name()">
        <field name="inspireAnnex">
          <xsl:value-of
            select="if (name() = 'AnnexIII') then 'iii' else if (name() = 'AnnexII') then 'ii' else 'i'"/>
        </field>
      </xsl:for-each-group>
      <xsl:for-each select="Themes/*">
        <xsl:variable name="themeKey" select="."/>
        <field name="inspireTheme">
          <xsl:value-of
            select="$inspireThemesMap/map[@monitoring = $themeKey]/@theme"/>
        </field>
      </xsl:for-each>


      <xsl:for-each
        select="MdDataSetExistence/MdAccessibility/(discovery|view|download|viewDownload)[. = 'true']">
        <field name="recordOperatedByType">
          <xsl:value-of select="name()"/>
        </field>
      </xsl:for-each>

      <field name="isAboveThreshold">
        <xsl:value-of select="count(MdDataSetExistence/IRConformity) > 0"/>
      </field>
      <field name="harvesterUuid">
        <xsl:value-of
          select="MdDataSetExistence/MdAccessibility/discoveryUuid"/>
      </field>
      <field name="inspireConformResource">
        <xsl:value-of
          select="MdDataSetExistence/IRConformity/structureCompliance = 'true'"/>
      </field>
    </doc>
  </xsl:template>

</xsl:stylesheet>
