<?xml version="1.0" encoding="UTF-8"?>
<stylesheet version="2.0" xmlns="http://www.w3.org/1999/XSL/Transform">
	<output method="xml" indent="yes" />
	<param name="pname" />
	<param name="pvers" />
	<template match="@* | node()">
		<copy>
			<apply-templates select="@* | node()" />
		</copy>
	</template>
	<!-- <template match="book[@id=$pname]"> -->
	<template match="id($pname)">
		<copy>
			<apply-templates select="@* | node() " />
		</copy>
		<copy>
		<attribute name="id">2bis</attribute>
			<apply-templates select="@*[name(.)!='id'] | node() " />
		</copy>
		<message>processing book</message>
	</template>

	<template match="books">
		<copy>
			<apply-templates select="@* | node() " />
		</copy>
		<message>Added du <value-of select="$pname"/></message>
	</template>
</stylesheet>