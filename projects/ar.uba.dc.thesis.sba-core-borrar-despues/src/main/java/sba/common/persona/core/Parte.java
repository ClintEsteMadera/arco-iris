
/*
 * $Id: Parte.java,v 1.1 2007/10/02 20:55:59 cvschioc Exp $
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362 Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de
 * Caja de Valores S.A. ("Informaci�n Confidencial"). Usted no
 * divulgar� tal Informaci�n Confidencial y la usar� solamente
 * de acuerdo a los t�rminos del acuerdo de licencia que Ud. posee
 * con Caja de Valores S.A.
 */
package sba.common.persona.core;

import sba.common.persistence.BasePersistentObject;

/**
 * 
 *
 * @author Miguel D�az
 * @version $Revision: 1.1 $ - $Date: 2007/10/02 20:55:59 $
 */
public abstract class Parte extends BasePersistentObject {

    /**
     * Constructor default.
     */
    protected Parte() {
        super();
    }
    
    /**
     * @return la denominaci�n (o similar) de la Parte.
     */
    public abstract String getIdentificadorUnivoco();
    
    /**
     * @return el tipo de parte asociado.
     */
    public abstract String getDescripcionTipoParte();

}
