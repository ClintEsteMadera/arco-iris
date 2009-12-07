package commons.gui.model.table;

/**
 * Extensi�n de {@link sba.ui.model.table.TableRowAdapter} que informa
 * si la columna es una columna "formateada"
 * 
 * @author ppastorino
 *
 */
public interface FormattedTableRowAdapter extends TableRowAdapter {
	
	/**
	 * Informa si la columan es "formateada", es decir si el valor
	 * que provee el m�todo {@link sba.ui.model.table.TableRowAdapter#getValueAt(Object, int)}
	 * no es el valor original sino un valor que ha sido transformado.
	 *   
	 * @param column �ndice de la columna.
	 * @return
	 */
	public boolean isFormatted(int column);
}
