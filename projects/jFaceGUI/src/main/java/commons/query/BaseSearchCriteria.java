package commons.query;

import java.io.Serializable;

/**
 * Basic Search Criteria where all search criteria must inherit from.
 */

public abstract class BaseSearchCriteria<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}