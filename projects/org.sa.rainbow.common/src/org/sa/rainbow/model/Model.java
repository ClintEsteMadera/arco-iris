/**
 * Created April 7, 2006.
 */
package org.sa.rainbow.model;

import org.sa.rainbow.core.IDisposable;


/**
 * Interface to the RainbowModel, referenced by all the RainbowRunnables.
 * <p>
 * History: <ul>
 *  <li> [Sep 5, 2006] Added get method for retrieving architectural model.
 * </ul>
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface Model extends IDisposable {

	/** The property identifier for obtaining the deployment location of an element */
	public static final String PROPKEY_LOCATION = "deploymentLocation";
	/** The property identifier for determining whether an element is in the architecture or the env't */
	public static final String PROPKEY_ARCH_ENABLED = "isArchEnabled";

	/**
	 * Returns the underlying Acme model object supporting Rainbow adaptation.
	 * @return Object  the Acme model object, should cast to IAcmeModel
	 */
	public Object getAcmeModel ();

	/**
	 * Returns the underlying Acme-based Target Environment model object supporting
	 * Rainbow adaptation.
	 * @return Object  the Acme-based Target Environment model object, should cast to IAcmeModel
	 */
	public Object getEnvModel ();

	/**
	 * Returns the model value of the property named by the identifier {@code id}.
	 * @param id  the fully-qualified property identifier
	 * @return Object  the model value for the property in IAcmeProperty
	 */
	public Object getProperty (String id);

	/**
	 * Returns the predicted model value of the property named by the
	 * identifier {@code id} at some future time point.
	 * @param id  the fully-qualified property identifier
	 * @param dur  the millisecond duration into the future to predict value
	 * @return Object  the predicted model value for the property in IAcmeProperty
	 */
	public Object predictProperty (String id, long dur);

	/**
	 * Returns the model String value of the property named by the identifier {@code id}.
	 * @param id  the fully-qualified property identifier
	 * @return String  the model value for the property as String
	 */
	public String getStringProperty (String id);

	/**
	 * Updates the model value of the property named by the identifier {@code iden}
	 * with the value {@code value}.
	 * @param iden   the fully-qualified property identifier
	 * @param value  the new value of the designated property
	 */
	public void updateProperty (String iden, Object value);

	/**
	 * Returns whether any model property has changed.
	 * @return boolean  <code>true</code> if at least one model property has changed,
	 *     <code>false</code> otherwise.
	 */
	public boolean hasPropertyChanged ();

	/**
	 * Clears the state that model property has changed.
	 */
	public void clearPropertyChanged ();

	/**
	 * Instructs the underlying model to evaluate its model constraints, and
	 * sets an internal flag when any constraint evaluates to false, indicating
	 * a constraint violation.
	 */
	public void evaluateConstraints ();

	/**
	 * Returns whether constraint has been violated.
	 * @return boolean  <code>true</code> if constraint has been violated,
	 *     <code>false</code> otherwise.
	 */
	public boolean isConstraintViolated ();

	/**
	 * Clears the state of constraint violation.
	 */
	public void clearConstraintViolated ();

}
