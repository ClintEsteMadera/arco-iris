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
 * 
 * $Id: CheckBoxesGroup.java,v 1.9 2007/11/30 20:31:08 cvsmarco Exp $
 */

package commons.gui.widget.group;

import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sba.common.properties.EnumProperty;
import sba.common.properties.FakeEnumProperty;

import commons.gui.widget.creation.binding.FakeBinding;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;

/**
 * Modela un grupo con checkboxs.
 * @author Luisina Marconi
 * @version $Revision: 1.9 $ $Date: 2007/11/30 20:31:08 $
 * @param <T>
 *            El tipo de los elementos que formarán parte del Check Boxes Group.
 */

public class CheckBoxesGroup<T> extends SimpleGroup {

	public CheckBoxesGroup(Composite parent, EnumProperty groupName, boolean readOnly,
			int numColumns, final List<T> subconjunto, Iterable<IdentifiableElement<T>> conjunto) {

		super(parent, groupName, readOnly, numColumns);

		for (IdentifiableElement<T> elemento : conjunto) {
			final T element = elemento.getModel();

			FakeBinding binding = new FakeBinding(String.valueOf(subconjunto.contains(element)));
			FakeEnumProperty fakeEnumProperty = new FakeEnumProperty(elemento.getDescription());
			BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(super.getSwtGroup(),
					fakeEnumProperty, binding, readOnly);

			Control control = BooleanFactory.createBoolean(metainfo);

			if (control instanceof Button) {
				final Button button = (Button) control;
				SelectionAdapter selectionListener = new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						if (button.getSelection()) {
							subconjunto.add(element);
						} else {
							subconjunto.remove(element);
						}
					}
				};
				button.addSelectionListener(selectionListener);
			}
		}
	}
}