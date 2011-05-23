package commons.datastructures;

public class Pair<T1, T2> {

	private T1 first;

	private T2 second;

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
		final Pair<?, ?> p = (Pair<?, ?>) obj;

		if (p == null) {
			return false;
		}
		return (p.first == first || (first != null && first.equals(p.first)))
				&& (p.second == second || (second != null && second.equals(p.second)));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("<");
		str.append(first);
		str.append(", ");
		str.append(second);
		str.append(">");

		return str.toString();
	}
}
