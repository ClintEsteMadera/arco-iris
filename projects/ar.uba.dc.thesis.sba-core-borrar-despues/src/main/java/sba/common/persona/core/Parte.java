
/*
 * $Id: Parte.java,v 1.1 2007/10/02 20:55:59 cvschioc Exp $
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362 Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de
 * Caja de Valores S.A. ("Información Confidencial"). Usted no
 * divulgará tal Información Confidencial y la usará solamente
 * de acuerdo a los términos del acuerdo de licencia que Ud. posee
 * con Caja de Valores S.A.
 */
package sba.common.persona.core;

import sba.common.persistence.BasePersistentObject;

/**
 * 
 *
 * @author Miguel Díaz
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
     * @return la denominación (o similar) de la Parte.
     */
    public abstract String getIdentificadorUnivoco();
    
    /**
     * @return el tipo de parte asociado.
     */
    public abstract String getDescripcionTipoParte();

}
