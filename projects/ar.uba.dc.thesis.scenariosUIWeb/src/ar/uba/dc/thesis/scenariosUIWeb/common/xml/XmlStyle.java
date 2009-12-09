/*
 * $Id: XmlStyle.java,v 1.4 2009/04/27 18:02:00 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.xml;

/**
 * @author H. Adrián Uribe
 * @version $Revision: 1.4 $ $Date: 2009/04/27 18:02:00 $
 */
public class XmlStyle {
	public void setProlog(boolean prolog) {
		this.prolog = prolog;
	}

	public boolean isProlog() {
		return prolog;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setIndentString(String indentString) {
		this.indentString = indentString;
	}

	public String getIndentString() {
		return indentString;
	}

	public void setNewLineString(String newLineString) {
		this.newLineString = newLineString;
	}

	public String getNewLineString() {
		return newLineString;
	}

	public static final XmlStyle DEFAULT = new XmlStyle();
	public static final XmlStyle COMPACT = new XmlStyle();
	public static final XmlStyle MINIMAL = new XmlStyle();
	static {
		COMPACT.setIndentString("");
		COMPACT.setNewLineString("");
		MINIMAL.setProlog(false);
		MINIMAL.setIndentString("");
		MINIMAL.setNewLineString("");
	}

	private boolean prolog = true;
	private String encoding = "iso-8859-1";
	private String indentString = "\t";
	private String newLineString = System.getProperty("line.separator");
}
