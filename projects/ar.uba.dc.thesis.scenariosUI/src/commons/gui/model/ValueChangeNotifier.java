package commons.gui.model;

/**
* Interfaz para aquellos objetos que notifican un cambio de valor en un modelo.
* 
*/
public interface ValueChangeNotifier
{
	/**
	* Agrega un listener para el evento 'cambio de valor' 
	*/
	public void addValueChangeListener(ValueChangeListener listener);

	/**
	* Elimina un listener 
	*/
	public void removeValueChangeListener(ValueChangeListener listener);
};
