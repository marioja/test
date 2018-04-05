<?xml version="1.0" encoding="UTF-8"?>
<!-- This will copy province with id='NB' at then end with a new element name and id value -->
<stylesheet version="1.0" xmlns="http://www.w3.org/1999/XSL/Transform">
	<output method="xml"/>
	<template match="/provinces">
		<copy>
			<apply-templates/>
			<apply-templates select="/provinces/province[@id='NB']"><with-param name="isnew">true</with-param></apply-templates>			
		</copy>
	</template>
	<!-- standard copy template -->
	<template match="@*|node()">
		<copy>
			<apply-templates select="@*|node()" />
		</copy>
	</template>
	<template name="node-type">
		<variable name="typ">
			<choose>
				<when test="self::text()">text(<value-of select="self::text()"/>)</when>
				<when test="self::comment()">comment</when>
				<when test="count(.|../@*)=count(../@*)">attribute(<value-of select="local-name()"/>=<value-of select="."/>)</when>
				<when test="self::processing-instruction()">processing-instruction(<value-of select="local-name()"/>)</when>
				<otherwise><value-of select="local-name()"/></otherwise>
			</choose>
		</variable>
		<message>ln=<value-of select="$typ"/>,p=<value-of select="position()"/></message>
	</template>
	<template match="*[local-name()='province'][@id='NB']">
		<param name="isnew" select="'false'"/>
		<call-template name="node-type"/>
		<message>Found nb <value-of select="local-name()"/>=<value-of select="$isnew"/></message>
			<choose>
				<when test="$isnew='true'">
					<element name="prov2" namespace="">
					<attribute name="id">XX</attribute>
					<apply-templates select="@*[name()!='id']"/>
					<apply-templates select="node()"/>
					</element>
				</when>
				<otherwise>
					<copy><apply-templates select="@*|node()"/></copy>
				</otherwise>
			</choose>
<!-- 		<text disable-output-escaping="yes">&lt;prov2></text> -->
<!-- 		<copy> -->
			<!-- <element name="prov2" namespace="">
			<attribute name="id">
				<choose>
					<when test="$isnew='true'">XX</when>
					<otherwise><value-of select="./@id"/></otherwise>
				</choose>
			</attribute>
			<apply-templates select="@*[name()!='id']"/>
			<apply-templates select="node()"/>
			</element> -->
<!-- 		</copy> -->
<!-- 		<text disable-output-escaping="yes">&lt;/prov2></text> -->
	</template>	
</stylesheet>