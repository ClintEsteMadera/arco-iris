package commons.gui.model.types;

import java.text.Format;

/**
 * Configuracion para edicion y rendering del tipo Date.
 * 
 * @author P.Pastorino E.Calabrese 
 */
public class DateConfiguration implements EditConfiguration {

	public DateConfiguration(){
		m_parameters=new DateEditParameters();
	}

	public DateConfiguration(DateEditParameters parameters){
		m_parameters=(DateEditParameters)parameters.clone();
	}
	
	public DateConfiguration(int mode){
		m_parameters=new DateEditParameters(mode);
	}
	
	public Format getFormat(){
		return m_parameters.getDateFormat();
	}

	public Format getExportableFormat(){
		return m_parameters.getExportableFormat();
	}
		
	public Object getPrototype(){
		return m_parameters.getPrototype();
	}

	public Object getColumnPrototype() {
		return this.getPrototype();
	}
	
	public boolean isRightAligned(){
		return true;
	}

	public	DateEditParameters getParameters(){
		return m_parameters;
	}
	
	private DateEditParameters m_parameters;
}
