package commons.core;

/**
 * This is similar to {@link Cloneable} but removing information about the underlying persistence framework, such as
 * Hibernate.
 * <p>
 * This interface might be useful when a persisten object belonging to a system A must be passed over as a parameter to
 * another system B, without information about persistence.
 */
public interface CopyConstructible<T> {

	/**
	 * Similar to {@link Object#clone()} but without information about the underlying persistence framework, such as
	 * Hibernate.
	 */
	T copyConstructor();

}
