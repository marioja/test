<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/TR/xhtml1/strict">
	<xsl:template match="book">
		<HTML>
			<BODY>
				<xsl:apply-templates />
			</BODY>
		</HTML>
	</xsl:template>
	<xsl:template match="title">
		<H1>
			<xsl:apply-templates />
		</H1>
	</xsl:template>
	<xsl:template match="author">
		<H3>
			<xsl:apply-templates />
		</H3>
	</xsl:template>
	<xsl:template match="publisher">
		<P>
			<I>
				<xsl:apply-templates />
			</I>
		</P>
	</xsl:template>
</xsl:stylesheet>