package commons.gui.widget.factory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import commons.gui.util.PageHelper;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;

public final class LabelFactory {

	/**
	 * This class is not meant to be instantiated.
	 */
	private LabelFactory() {
		super();
	}

	/**
	 * Crea un campo read-only que consta de un Label y un <i>value</i> de sólo lectura. El binding se utiliza para que
	 * dicho valor cambie de acuerdo al cambio del modelo.
	 * 
	 * @param composite
	 *            el composite donde se inserta el campo.
	 * @param binding
	 *            el objeto que contiene el valor que tendrá el campo <i>value</i> y la información de binding para
	 *            dicho campo.
	 * @param enumProp
	 *            la key property para reemplazar en la creación del campo <i>label</i>.
	 * @return un campo read-only.
	 */
	public static Label createReadOnlyField(Composite composite, Binding binding, EnumProperty enumProp) {
		if (!enumProp.equals(CommonLabels.NO_LABEL)) {
			createLabel(composite, enumProp, false, true);
		}
		return createValue(composite, binding, true);
	}

	/**
	 * Crea un campo <i>label</i> cuyo texto es suministrado mediante un EnumProperty.
	 * 
	 * @param composite
	 *            el composite donde se inserta el Label.
	 * @param enumProp
	 *            el property enumerado, cuyo <code>toString()</code> provee el String del Label. El mismo <b>NO PUEDE
	 *            SER NULO</b>.
	 * @param bold
	 *            especifica si se desea que el Label tenga formato en negrita.
	 * @param colonSuffix
	 *            especifica si se desea que se agregue como sufijo el caracter ":" (dos puntos).
	 * @return un Label.
	 */
	public static Label createLabel(Composite composite, EnumProperty enumProp, boolean bold, boolean colonSuffix) {
		Label label = new Label(composite, SWT.LEFT);
		if (bold) {
			label.setFont(PageHelper.getValueLabelsFont());
		}
		String text = enumProp.toString();
		if (colonSuffix) {
			text = text + ":";
		}
		label.setText(text);

		return label;
	}

	/**
	 * Crea un control del tipo Label con seteos de fuente específicos.
	 * 
	 * @param composite
	 *            el composite dónde se crea el Label
	 * @return un control de tipo Label.
	 */
	public static Label createLabel(Composite composite) {
		Label label = new Label(composite, SWT.LEFT);
		if (composite.getLayoutData() instanceof GridData) {
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		label.setFont(PageHelper.getValueLabelsFont());
		return label;
	}

	/**
	 * Crea un campo del tipo "value", es decir, un simple texto de sólo lectura.
	 * 
	 * @param composite
	 *            el composite donde se inserta el campo.
	 * @param binding
	 *            el objeto que contiene el valor que tendrá el campo <i>value</i> y la información de binding para
	 *            dicho campo.
	 * @param dashIfBlank
	 *            especifica si el <i>value</i> debe crearse con un guión ("-") en el caso de ser blanco el texto
	 *            especificado en el parametro <code>value</code>.
	 * @return un campo de texto de sólo lectura con el valor especificado por parámetro. Si el mismo fuera
	 *         <code>null</code> o de longitud cero, dicho valor se fija en el caracter guión ("-") <i>siempre y cuando
	 *         se haya especificado en <code>true</code> el parámetro <code>dashIfBlank</code></i>
	 */
	public static Label createValue(Composite composite, Binding binding, boolean dashIfBlank) {
		final Label label = createLabel(composite);

		binding.bind(label);

		final String text = label.getText();

		if (StringUtils.isBlank(text) && dashIfBlank) {
			label.setText("-");
		}
		return label;
	}
}