<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
<xsl:strip-space elements="*" />

<xsl:template match="*">
    <xsl:element name="{local-name()}">
        <xsl:copy-of select="@*" />
        <xsl:apply-templates />
    </xsl:element>
</xsl:template>

<xsl:template match="/*">
    <books>
        <xsl:apply-templates />
    </books>
</xsl:template>

<xsl:template match="*[local-name()='books']">
    <books_record>
        <xsl:apply-templates />
    </books_record>
</xsl:template>

</xsl:transform>