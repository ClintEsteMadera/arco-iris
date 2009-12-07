package sba.common.core;

/**
 * Predicado.
 */
public interface Predicate<T>
{
	public static final Predicate TRUE = new Predicate(){
		public boolean evaluate(Object o) {
			return true;
		}
	};

	public static final Predicate FALSE = new Predicate(){
		public boolean evaluate(Object o) {
			return false;
		}
	};

	public static final Predicate NOT_NULL = new Predicate(){
		public boolean evaluate(Object o) {
			return o != null;
		}
	};
	
	public  boolean evaluate(T o);
}
