package sba.common.anotaciones.domain;

import java.io.Serializable;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateComponent;
import sba.common.annotations.ValidateRequired;
import sba.common.annotations.ValidationCondition;
import sba.common.validation.CommonDataTypes;


public class Domain implements Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected String description;
    protected Empleado empleado;
    protected String[] tiposDeEmpleadoValidos;
    
    protected String validacionCruzada;
    
    public Domain() {
    	this.empleado=new Empleado();
    }
    
    @PropertyDescription(value="DESCRIPCION")
    @ValidateRequired
    @DataTypeAnnotation(typeId=CommonDataTypes.DESCRIPCION)
    public String getDescripcion() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
    @ValidateComponent(condition=ValidationCondition.IF_NOT_EMPTY)
	public Empleado getEmpleado() {
		return empleado;
	}

	public String[] getTiposDeEmpleadoValidos() {
		return tiposDeEmpleadoValidos;
	}

	public void setTiposDeEmpleadoValidos(String[] tiposDeEmpleadoValidos) {
		this.tiposDeEmpleadoValidos = tiposDeEmpleadoValidos;
	}

	public String getValidacionCruzada() {
		return validacionCruzada;
	}

	public void setValidacionCruzada(String validacionCruzada) {
		this.validacionCruzada = validacionCruzada;
	}

}	

