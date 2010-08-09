package commons.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

public class ObjectSelectionMetainfo {

	public ObjectSelectionMetainfo(Composite parent, EnumProperty label, Binding binding, boolean readOnly) {
		this.parent = parent;
		this.label = label;
		this.readOnly = readOnly;
		this.binding = binding;
	}

	/**
	 * Parent
	 */
	public Composite parent;

	/**
	 * Propiedad del objeto seleccionado que se setea en el modelo
	 */
	public String selectedProperty = "";

	/**
	 * Propiedad del objeto seleccionado que se muestra en el textbox
	 */
	public String descriptionProperty = "";

	/**
	 * Texto para el label
	 */
	public EnumProperty label;

	/**
	 * Indica si se puede seleccionar un valor nulo
	 */
	public boolean nullable = false;

	/**
	 * Indica si se puede crear un nuevo objeto
	 */
	public boolean canCreate = false;

	/**
	 * Indica si se puede navegar (mediante el link) para ver el objeto
	 */
	public boolean canView = true;

	/**
	 * Opciones para la creación de un objeto
	 */
	public Object[] createOptions;

	/**
	 * Indica si el componente está en modo read-only
	 */
	public boolean readOnly = false;

	/**
	 * Indica la cantidad de columnas a las cuales se debe estirar el composite
	 */
	public int columnsToSpan = 2;

	/**
	 * Informacion para binding
	 */
	public Binding binding;
}
