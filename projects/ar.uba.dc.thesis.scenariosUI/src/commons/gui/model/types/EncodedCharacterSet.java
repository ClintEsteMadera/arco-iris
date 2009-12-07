/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: EncodedCharacterSet.java,v 1.3 2008/05/14 17:35:37 cvspasto Exp $
 */

package commons.gui.model.types;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import sba.common.validation.string.CharacterSet;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/05/14 17:35:37 $
 */

class EncodedCharacterSet implements CharacterSet {

	CharsetDecoder decoder;

	EncodedCharacterSet(String[] charsetNames) {
		Charset charset = null;

		for (int i = 0; i < charsetNames.length; i++) {
			// se prueba con diferentes nombres porque puede depender del JDK
			try {
				charset = Charset.forName(charsetNames[i]);
				name = charsetNames[i];
				break;
			} catch (Error e) {
				if (i == charsetNames.length - 1) {
					throw e;
				}
			} catch (RuntimeException e) {
				if (i == charsetNames.length - 1) {
					throw e;
				}
			}
		}

		if (charset == null) {
			throw new IllegalArgumentException("No se indicaron los posibles nombres del charset");
		}

		decoder = charset.newDecoder().onUnmappableCharacter(CodingErrorAction.REPORT)
				.onMalformedInput(CodingErrorAction.REPORT);
	}

	public boolean isMember(char c) {
		byte[] bytes = (new Character(c)).toString().getBytes();

		try {
			CharBuffer r = decoder.decode(ByteBuffer.wrap(bytes));
			r.toString();
		} catch (CharacterCodingException e) {
			return false;
		}
		return true;
	}

	public String getName() {
		return this.name;
	}
	

	public Character getReplaceForInvalidCharacter(char c) {
		return null;
	}

	private String name="";
	
}
