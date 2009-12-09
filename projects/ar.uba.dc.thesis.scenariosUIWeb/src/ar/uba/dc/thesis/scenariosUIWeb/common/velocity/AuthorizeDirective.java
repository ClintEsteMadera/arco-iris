/*
 * $Id: AuthorizeDirective.java,v 1.6 2009/04/30 18:19:02 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;

import ar.uba.dc.thesis.scenariosUIWeb.common.util.ClassUtils;

/**
 * @author Rodrigo A. Peinado
 * @version $Revision: 1.6 $ $Date: 2009/04/30 18:19:02 $
 */
public abstract class AuthorizeDirective extends Directive {
	@Override
	public String getName() {
		return AuthorizeDirective.DIRECTIVE_NAME;
	}

	@Override
	public int getType() {
		return DirectiveConstants.LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
	        throws IOException, ResourceNotFoundException, ParseErrorException,
	        MethodInvocationException {
		StringWriter internalwriter = new StringWriter();
		node.jjtGetChild(0).render(context, internalwriter);
		String text = internalwriter.toString();
		boolean authOK = this.isAuthorized(text);
		if (log.isDebugEnabled()) {
			log.debug("isAuthorized? " + text + " -> " + authOK);
		}
		writer.write(Boolean.toString(authOK));
		return true;
	}

	protected abstract boolean isAuthorized(String text);

	public static final String DIRECTIVE_NAME = "auth";
	private static final Log log = LogFactory.getLog(ClassUtils.getCurrentClass());
}
