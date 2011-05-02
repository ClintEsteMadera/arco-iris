package ar.uba.dc.arcoiris.common;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This ToStringStyle is a combination of {@link ToStringStyle#MULTI_LINE_STYLE} and
 * {@link ToStringStyle#SHORT_PREFIX_STYLE}
 * <p>
 * This class is intended to be used as a singleton.
 * 
 */
public final class ArcoIrisToStringStyle extends ToStringStyle {

	private static final long serialVersionUID = 1L;

	public static final ArcoIrisToStringStyle ARCO_IRIS_STYLE = new ArcoIrisToStringStyle();

	private ArcoIrisToStringStyle() {
		super();
		this.setContentStart("[");
		this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
		this.setFieldSeparatorAtStart(true);
		this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
		this.setUseShortClassName(true);
		this.setUseIdentityHashCode(false);
	}
}
