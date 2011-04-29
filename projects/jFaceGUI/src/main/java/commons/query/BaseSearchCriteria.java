package commons.query;

/**
 * Basic Search Criteria containing only an opaque identifier of type Long.
 */
public class BaseSearchCriteria<T> implements SearchCriteria<T> {

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
