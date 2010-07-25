package commons.gui;

public interface Openable<T> {

	boolean open(T element);

	T getModel();
}
