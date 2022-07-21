<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:w="http://www.semwebprogramming.org/2009/04/weather-ont#" 
  xml:base="http://www.semwebprogramming.org/weather"
  version="1.0">

  <xsl:output method="xml" version="1.0" encoding="UTF-8"
    indent="yes" />

  <xsl:template match="/">
    <rdf:RDF
      xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
      xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
      xmlns:owl="http://www.w3.org/2002/07/owl#"
      xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
      xml:base="http://www.semwebprogramming.org/weather#"
      xmlns = "http://www.semwebprogramming.org/weather#"
      >
      <xsl:apply-templates />
    </rdf:RDF>
  </xsl:template>

  <xsl:template match="current_observation">
    <w:WeatherObservation>

      <w:source rdf:resource="{credit_URL}"/>
      <w:time>
        <xsl:value-of select="observation_time_rfc822"/>
      </w:time>
      <w:location><xsl:value-of select="location"/></w:location>
      <w:latitude><xsl:value-of select="latitude"/></w:latitude>
      <w:longitude><xsl:value-of select="longitude"/></w:longitude>

      <w:temperature_f>
        <xsl:value-of select="temp_f "/>
      </w:temperature_f>
      <w:windDirection rdf:resource="#{wind_dir}"/>
      <w:wind_mph><xsl:value-of select="wind_mph"/></w:wind_mph>

      <xsl:if test="wind_gust_mph != 'NA'">
        <w:wind_gust_mph>
          <xsl:value-of select="wind_gust_mph"/>
        </w:wind_gust_mph>
      </xsl:if>

      <xsl:if test="weather">
        <w:weatherDescription>
          <xsl:value-of select="weather"/>
        </w:weatherDescription>
      </xsl:if>

      <w:copyright rdf:resource="{copyright_url}"/>
    </w:WeatherObservation>
  </xsl:template>
</xsl:stylesheet>  