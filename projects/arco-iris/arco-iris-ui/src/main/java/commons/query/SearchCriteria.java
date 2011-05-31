package commons.query;

import java.io.Serializable;

/**
 * Declares the functionality that all search criteria objects must implement.
 * 
 * @param <T>
 *            The underlying object that is supposed to be returned in a search using this criteria.
 */
public interface SearchCriteria<T> extends Serializable {

	public Long getId();

	public void setId(Long id);
}