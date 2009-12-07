/*
* Licencia de Caja de Valores S.A., Versi�n 1.0
*
* Copyright (c) 2006 Caja de Valores S.A.
* 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
* Todos los derechos reservados.
*
* Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
* Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
* los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
*/

/*
* $Id: Pair.java,v 1.1 2008/02/14 19:22:52 cvschioc Exp $
*/

package sba.common.dataestructures;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/02/14 19:22:52 $
 */

public class Pair<T1,T2> {

		
	public Pair(T1 first, T2 second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object obj) {
		final Pair p=(Pair)obj;
		
		if(p == null){
			return false;
		}
		
		return (p.first == first || (first != null && first.equals(p.first))) && 
			(p.second == second || (second != null && second.equals(p.second)));					
	}
	
	@Override
	public int hashCode() {
		int h1=first == null ? 0 : first.hashCode();
		int h2=second == null ? 0 : second.hashCode();
		return h1 + 31 * h2;
	}
	
	@Override
	public String toString() {
		StringBuffer str=new StringBuffer();
		str.append("{ first=");
		str.append(first);

		str.append(", second=");
		str.append(second);
		str.append("}");

		return str.toString();
	}
	private T1 first;
	private T2 second;
}
