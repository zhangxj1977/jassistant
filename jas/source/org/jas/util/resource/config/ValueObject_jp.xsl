<?xml version="1.0" encoding="Shift_JIS"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>
<xsl:template match="/root" xml:space="default">/* ====================================================================
 * $Source: C:/work/999_CVSRep/jas/source/org/jas/util/resource/config/ValueObject_jp.xsl,v $
 * $Author: zhangxj $
 * $Revision: 1.1 $
 * $Date: 2007/12/20 01:58:27 $
 * ====================================================================
 *  �t�@�C��  <xsl:value-of select="class_name"/>.java
 *  �@�\��    <xsl:value-of select="class_description"/>
 *  ����      <xsl:value-of select="create_date"/>  �쐬            <xsl:value-of select="author"/>
 *           Copyright 2002 <xsl:value-of select="company"/> All Rights Reserved.
 * ====================================================================
 */
<xsl:value-of select="package_define"/>

<xsl:if test="count(import_class_define/item) &gt; 0" xml:space="preserve">

</xsl:if>

<xsl:for-each select="import_class_define/item" xml:space="preserve"><xsl:value-of select="."/>
</xsl:for-each>
/**
 * <xsl:value-of select="class_description"/>
 *
 * @author     <xsl:value-of select="author"/>
 * @version    <xsl:value-of select="version"/>
 */
public class <xsl:value-of select="class_name"/><xsl:text> </xsl:text><xsl:if test="serializable">implements Serializable</xsl:if> {
<xsl:for-each select="columns/column">
	/**
	 * <xsl:value-of select="comment"/>
	 */
	private <xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/> = <xsl:value-of select="default"/>;
</xsl:for-each>
<xsl:if test="constructor">
	/**
	 * �f�B�t�H���g�R���X�g���N�V����
	 */
	public <xsl:value-of select="class_name"/>() {}

</xsl:if>
<xsl:for-each select="columns/column">
<xsl:if test="getter">
	/**
	 * <xsl:value-of select="comment"/>���擾����
	 *
	 * @return <xsl:value-of select="type"/> <xsl:value-of select="comment"/>
	 */
	public <xsl:value-of select="type"/> get<xsl:value-of select="name"/>() {
		return this.<xsl:value-of select="name"/>;
	}
</xsl:if>
<xsl:if test="setter">
	/**
	 * <xsl:value-of select="comment"/>��ݒ肷��
	 *
	 * @param <xsl:value-of select="name"/> �V����<xsl:value-of select="comment"/>
	 */
	public void set<xsl:value-of select="name"/>(<xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/>) {
		this.<xsl:value-of select="name"/> = <xsl:value-of select="name"/>;
	}
</xsl:if>
</xsl:for-each>
<xsl:if test="tostring">
	/**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
<xsl:for-each select="columns/column">
		sb.append("<xsl:value-of select="name"/>:		" + <xsl:value-of select="name"/> + "\n");</xsl:for-each>

		return sb.toString();
	}
</xsl:if>
}
</xsl:template>

</xsl:stylesheet>
