<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:wp="http://www.semwebprogramming.net/wordpress/ontology#"
  xmlns:ft="http://semwebprogramming.org:8099/ont/friendtracker-ont#" version="1.0">

  <xsl:output method="xml" version="1.0" encoding="UTF-8"
    indent="yes" />

<!-- match the root node (and add our own RDF root... -->
  <xsl:template match="/rdf:RDF">
    <rdf:RDF
      xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
      xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
      xmlns:owl="http://www.w3.org/2002/07/owl#"
      xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
      xmlns:ft="http://semwebprogramming.org/2009/ftracker#" 
      xmlns:time="http://www.w3.org/2006/time#" 
      xml:base="http://semwebprogramming.org:8099/data/wordpress#"
      >

      <xsl:for-each select="rdf:Description">

<!-- Handle posts -->
	  <xsl:if test="rdf:type/@rdf:resource = 'http://www.semwebprogramming.net/wordpress/ontology#wp_posts'">
		<rdf:Description rdf:about="#post{wp:wp_posts_ID}">
		  <rdf:type rdf:resource="http://semwebprogramming.org:8099/ont/friendtracker-ont#Post" />
		  <ft:hasContent><xsl:value-of select="wp:wp_posts_post_content" /></ft:hasContent>
		  <ft:hasTitle><xsl:value-of select="wp:wp_posts_post_title" /></ft:hasTitle>
		  <ft:occursAt>
                     <time:Instant>
                       <time:inXSDDateTime><xsl:value-of select="wp:wp_posts_post_date" /></time:inXSDDateTime>
                     </time:Instant>
                  </ft:occursAt>
		</rdf:Description>
	  </xsl:if>

<!-- Handle users -->
	  <xsl:if test="rdf:type/@rdf:resource = 'http://www.semwebprogramming.net/wordpress/ontology#wp_users'">
		<rdf:Description rdf:about="#user{wp:wp_users_ID}">
			<rdf:type rdf:resource="http://semwebprogramming.org:8099/ont/friendtracker-ont#Friend" />
			<ft:isNamed><xsl:value-of select="wp:wp_users_user_nicename"/></ft:isNamed>
			<ft:isNamed><xsl:value-of select="wp:wp_users_user_displayname"/></ft:isNamed>
			<ft:hasHandle><xsl:value-of select="wp:wp_users_user_login" /></ft:hasHandle>
			<ft:hasEmailAddress><xsl:value-of select="wp:wp_users_user_email" /></ft:hasEmailAddress>			
		</rdf:Description>
	  </xsl:if>
      </xsl:for-each>

<!--  Associate posts with users -->
      <xsl:for-each select="rdf:Description">
	  <xsl:if test="rdf:type/@rdf:resource = 'http://www.semwebprogramming.net/wordpress/ontology#wp_posts'">
		<rdf:Description rdf:about="#user{wp:wp_posts_post_author}">
		  <ft:hasPost rdf:resource="#post{wp:wp_posts_ID}" />
		</rdf:Description>
	  </xsl:if>

	
      </xsl:for-each>

    </rdf:RDF>
  </xsl:template>

</xsl:stylesheet>  
