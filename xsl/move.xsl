<?xml version="1.0" encoding="UTF-8"?>
<!-- This will copy province id='NB' at the end with a different id -->
<stylesheet version="1.0" xmlns="http://www.w3.org/1999/XSL/Transform">
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
	<template match="province[@id='NB']">
		<param name="isnew" select="'false'"/>
			<message>Found nb <value-of select="local-name()"/></message>
		<copy>
			<attribute name="id">
				<choose>
					<when test="$isnew='true'">XX</when>
					<otherwise><value-of select="./@id"/></otherwise>
				</choose>
			</attribute>
			<apply-templates select="@*[name()!='id']|node()"/>
		</copy>
	</template>	
</stylesheet>