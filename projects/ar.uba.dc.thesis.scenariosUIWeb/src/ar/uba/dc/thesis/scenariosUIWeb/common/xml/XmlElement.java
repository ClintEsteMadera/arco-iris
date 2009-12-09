/*
 * $Id: XmlElement.java,v 1.3 2009/04/27 18:02:00 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.xml;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import ar.uba.dc.thesis.scenariosUIWeb.common.util.DateUtils;

/**
 * @author H. Adri�n Uribe
 * @version $Revision: 1.3 $ $Date: 2009/04/27 18:02:00 $
 */
public final class XmlElement {
	public XmlElement(String name, XmlElement[] subelements) {
		this.name = name;
		this.subelements = subelements;
	}

	public void set(String name, String value) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (value != null) {
			if (attributes == null) {
				attributes = new LinkedHashMap<String,String>();
			}
			attributes.put(name, value);
		}
	}

	public void set(String name, Number value) {
		set(name, value.toString());
	}

	public void setDate(String name, Calendar value) {
		set(name, DateUtils.fastDateFormat(value, DateUtils.FORMAT_DATE));
	}

	public void setTime(String name, Calendar value) {
		set(name, DateUtils.fastDateFormat(value, DateUtils.FORMAT_TIME));
	}

	public void setDateTime(String name, Calendar value) {
		set(name, DateUtils.fastDateFormat(value, DateUtils.FORMAT_DATE_TIME));
	}

	public void setTimestamp(String name, Calendar value) {
		set(name, DateUtils.fastDateFormat(value, DateUtils.FORMAT_TIMESTAMP));
	}

	public StringBuilder toXml() {
		return toXml(null);
	}

	public StringBuilder toXml(XmlStyle style) {
		StringBuilder buf = new StringBuilder(64);
		if (style == null) {
			style = XmlStyle.DEFAULT;
		}
		if (style.isProlog()) {
			buf.append("<?xml version=\"1.0\" encoding=\"");
			buf.append(style.getEncoding());
			buf.append("\"?>");
			buf.append(style.getNewLineString());
		}
		toXml(style, buf, (byte) 0);
		return buf;
	}

	private void toXml(XmlStyle style, StringBuilder buf, byte level) {
		for (int i = 0; i < level; ++i) {
			buf.append(style.getIndentString());
		}
		buf.append('<');
		buf.append(name);
		if (attributes != null) {
			for (Map.Entry<String,String> entry : attributes.entrySet()) {
				buf.append(' ');
				buf.append(entry.getKey());
				buf.append("=\"");
				escape(buf, entry.getValue());
				buf.append('"');
			}
		}
		if (subelements == null || subelements.length == 0) {
			buf.append("/>");
			return;
		}
		buf.append('>');
		buf.append(style.getNewLineString());

		byte subLevel = (byte) (level + 1);
		for (XmlElement element : subelements) {
			element.toXml(style, buf, subLevel);
			buf.append(style.getNewLineString());
		}

		for (int i = 0; i < level; ++i) {
			buf.append(style.getIndentString());
		}
		buf.append("</");
		buf.append(name);
		buf.append('>');
	}

	private static void escape(StringBuilder buf, String value) {
		final int len = value.length();
		char ch;
		for (int i = 0; i < len; i++) {
			ch = value.charAt(i);
			switch (ch) {
			case '"':
				buf.append("&quot;");
				break;
			case '&':
				buf.append("&amp;");
				break;
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			default:
				buf.append(ch);
				break;
			}
		}
	}

	private final String name;
	private final XmlElement[] subelements;
	private Map<String,String> attributes;
}
