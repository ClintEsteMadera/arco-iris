package commons.gui.model.types;

import java.text.Format;

/**
 * Configuracion para edicion y rendering de Strings.
 * 
 * @author P.Pastorino
 *
 */
public class StringConfiguration implements EditConfiguration {
	
	public StringConfiguration(){
		this(new StringEditParameters());
	}

	public StringConfiguration(StringEditParameters parameters){
		m_parameters=(StringEditParameters)parameters.clone();
		m_format=new StringFormat();
	}
	
	/**
	 * @see sba.ui.edit.types.EditConfiguration#getFormat()
	 */
	public Format getFormat() {
		return m_format;
	}

	/**
	 * @see sba.ui.edit.types.EditConfiguration#getPrototype()
	 */
	public Object getPrototype() {
		if(m_prototype == null){
			m_prototype=m_parameters.getPrototype();
		}
		return m_prototype;
	}
	
	public Object getColumnPrototype() {
		if (m_columnPrototype == null) {
			m_columnPrototype = m_parameters.getColumnPrototype();
			if(m_columnPrototype == null){
				m_columnPrototype=this.getPrototype();
			}
		}
		return m_columnPrototype;
	}

	public boolean isRightAligned(){
		return false;
	}

	public StringEditParameters getParameters(){
		return m_parameters;	
	}
	
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName())
		.append(" {")
		.append(" parameters=[")
		.append(m_parameters)
		.append(" ] prototype=")
		.append(m_prototype)
		.append("}")
		.toString();
	}
 
	private class StringFormat extends DefaultFormat {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Object stringToValue(String str) {
			if(str != null){
				if(StringConfiguration.this.m_parameters.trim){
					str=str.trim();
				}
				if(str.length() == 0 && !StringConfiguration.this.m_parameters.allowEmpty){
					str=null;
				}
			}
			return super.stringToValue(str);
		}
	};

	private StringEditParameters m_parameters;
	private Object m_prototype;
	private  StringFormat m_format;
	private Object m_columnPrototype;
}
