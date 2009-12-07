package commons.gui.binding;

public interface ComboModel {
	
	int getItemCount();

	Object getItem(int index);

	Object getItemValue(int index);

	String getItemText(int index);

	String getNullText();
}
