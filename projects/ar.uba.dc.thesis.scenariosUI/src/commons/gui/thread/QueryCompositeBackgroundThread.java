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
 * $Id: QueryCompositeBackgroundThread.java,v 1.4 2007/11/12 21:08:34 cvschioc Exp $
 */
package commons.gui.thread;

import java.util.List;

import org.eclipse.swt.widgets.Display;

import commons.gui.util.PageHelper;
import commons.gui.widget.composite.QueryComposite;

/**
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.4 $ $Date: 2007/11/12 21:08:34 $
 */
public abstract class QueryCompositeBackgroundThread extends BackgroundThread {

	public QueryCompositeBackgroundThread(Display display, QueryComposite queryComposite) {
		super(display);
		this.queryComposite = queryComposite;
	}

	protected abstract List doQuery();
	
	@Override
	protected void performBackgroundOperation() {
		items = doQuery();
	}

	@Override
	protected void updateUI() {
		queryComposite.getTable().setInput(items);
		queryComposite.getTable().getTable().setEnabled(true);
		PageHelper.getMainWindow().setDefaultStatusMessage();
	}

	private List items;
	
	private QueryComposite queryComposite;

}

