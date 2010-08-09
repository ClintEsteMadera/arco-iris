package commons.gui.widget.group;

import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.widget.creation.binding.FakeBinding;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

/**
 * Modela un grupo con checkboxs.
 * 
 * @param <T>
 *            El tipo de los elementos que formarán parte del Check Boxes Group.
 */
public class CheckBoxesGroup<T> extends SimpleGroup {

	public CheckBoxesGroup(Composite parent, EnumProperty groupName, boolean readOnly, int numColumns,
			final List<T> subconjunto, Iterable<IdentifiableElement<T>> conjunto) {

		super(parent, groupName, readOnly, numColumns);

		for (IdentifiableElement<T> elemento : conjunto) {
			final T element = elemento.getModel();

			FakeBinding binding = new FakeBinding(String.valueOf(subconjunto.contains(element)));
			FakeEnumProperty fakeEnumProperty = new FakeEnumProperty(elemento.getDescription());
			BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(super.getSwtGroup(), fakeEnumProperty, binding,
					readOnly);

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