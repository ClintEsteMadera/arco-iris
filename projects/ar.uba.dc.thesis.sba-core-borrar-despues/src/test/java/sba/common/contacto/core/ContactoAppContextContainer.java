/*
* Licencia de Caja de Valores S.A., Versión 1.0
*
* Copyright (c) 2006 Caja de Valores S.A.
* 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
* Todos los derechos reservados.
*
* Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
* Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
* los términos del acuerdo de licencia que posee con Caja de Valores S.A.
*/

/*
* $Id: ContactoAppContextContainer.java,v 1.1 2007/10/02 20:56:00 cvschioc Exp $
*/

package sba.common.contacto.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;


/**
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2007/10/02 20:56:00 $
 */

public class ContactoAppContextContainer {

	private ContactoAppContextContainer(){
		super();
		List<String> paths = this.getDescriptors();
		paths.add("spring/spring-BaseCrudDAOTest.xml");
		appContext = new ClassPathXmlApplicationContext(paths.toArray(new String[0]));
		StaticApplicationContext staticApplicationContext = new StaticApplicationContext(appContext);
		setAppContext(staticApplicationContext);
	}

	private List<String> getDescriptors() {
		List<String> paths = new ArrayList<String>();
		paths.add("spring/contacto-data.xml");
		paths.add("spring/contacto-datasource.xml");
		return paths;
	}
	
	public Object getBean(String beanName){
		return getAppContext().getBean(beanName);
	}
	
	public static ContactoAppContextContainer getInstance() {
		if (appContextContainer == null){
			appContextContainer = new ContactoAppContextContainer();
		}			
		return appContextContainer;
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}

	private void setAppContext(ApplicationContext newAppContext) {
		appContext = newAppContext;
	}

	private static ContactoAppContextContainer appContextContainer;
	private static ApplicationContext appContext;
}