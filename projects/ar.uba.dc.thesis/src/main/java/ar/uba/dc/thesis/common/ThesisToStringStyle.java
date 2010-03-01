package ar.uba.dc.thesis.common;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This ToStringStyle is a combination of {@link ToStringStyle#MULTI_LINE_STYLE} and
 * {@link ToStringStyle#SHORT_PREFIX_STYLE}
 * <p>
 * This class is intended to be used as a singleton.
 * 
 */
public class ThesisToStringStyle extends ToStringStyle {

	public static final ThesisToStringStyle THESIS_STYLE = new ThesisToStringStyle();

	private static final long serialVersionUID = 1L;

	private ThesisToStringStyle() {
		super();
		this.setContentStart("[");
		this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
		this.setFieldSeparatorAtStart(true);
		this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
		this.setUseShortClassName(true);
		this.setUseIdentityHashCode(false);
	}
}
