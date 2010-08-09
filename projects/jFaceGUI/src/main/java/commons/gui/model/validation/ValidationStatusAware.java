package commons.gui.model.validation;

/**
 * 
 * 
 */

public interface ValidationStatusAware {

	public void setOkStatus();

	public void setErrorStatus(String message);

	public boolean isOkStatus();
}
