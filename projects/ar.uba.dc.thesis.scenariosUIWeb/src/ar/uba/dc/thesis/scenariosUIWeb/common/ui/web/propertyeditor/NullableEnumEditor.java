package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.propertyeditor;

import java.beans.PropertyEditorSupport;
import org.springframework.util.StringUtils;

@SuppressWarnings("unchecked")
public class NullableEnumEditor extends PropertyEditorSupport {

	private final Class<? extends Enum> clazz;

	public NullableEnumEditor(Class<? extends Enum> clazz) {
		this.clazz = clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setAsText(String s) throws IllegalArgumentException {
		if (StringUtils.hasText(s)) {
			setValue(Enum.valueOf(clazz, s));
		} else {
			setValue(null);
		}
	}

}