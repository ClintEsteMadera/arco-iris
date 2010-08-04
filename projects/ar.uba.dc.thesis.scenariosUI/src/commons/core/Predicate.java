package commons.core;

@SuppressWarnings("unchecked")
public interface Predicate<T> {

	public static final Predicate TRUE = new Predicate<Object>() {
		public boolean evaluate(Object o) {
			return true;
		}
	};

	public static final Predicate FALSE = new Predicate<Object>() {
		public boolean evaluate(Object o) {
			return false;
		}
	};

	public static final Predicate NOT_NULL = new Predicate<Object>() {
		public boolean evaluate(Object o) {
			return o != null;
		}
	};

	public boolean evaluate(T o);
}
