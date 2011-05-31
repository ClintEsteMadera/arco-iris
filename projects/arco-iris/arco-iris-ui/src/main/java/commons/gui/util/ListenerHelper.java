package commons.gui.util;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import commons.gui.GuiStyle;
import commons.gui.validators.Validations;
import commons.utils.ClassUtils;
import commons.utils.StringUtilities;

public abstract class ListenerHelper {

	public static void addFocusListener(Text textBox) {
		FocusListener listener = new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent event) {
				Text text = (Text) event.getSource();
				text.selectAll();
			}

		};
		textBox.addFocusListener(listener);
	}

	public static <T> void addNumberFieldFocusListener(Text textBox,
			final T instance, final String labelKey) {
		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				String text = ((Text) event.getSource()).getText();
				text = text.trim().replace(',', '.');
				BigDecimal num;
				try {
					num = new BigDecimal(text);
				} catch (NumberFormatException exc) {
					num = BigDecimal.ZERO;
				}
				if (num.scale() < GuiStyle.DEFAULT_SCALE) {
					num = num.setScale(GuiStyle.DEFAULT_SCALE);
				}
				((Text) event.getSource()).setText(num.toPlainString());
				ClassUtils.setValueByReflection(instance, labelKey, text);
			}
		};

		textBox.addFocusListener(focusListener);
	}

	public static void addIntegerFieldKeyListener(final Text textBox) {
		KeyListener listener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				boolean isNumeric = StringUtils.isNumeric(String
						.valueOf(event.character));
				if (!isNumeric
						&& !StringUtilities.isSpecialCharacter(event.character)
						&& !StringUtilities.isSpecialKey(event.keyCode)) {
					event.doit = false;
				} else {
					if (isNumeric) {
						try {
							Integer.parseInt(textBox.getText()
									+ event.character);
						} catch (NumberFormatException ex) {
							event.doit = false;
						}
					}
				}
			}
		};
		textBox.addKeyListener(listener);
	}

	public static void addIntegerFieldModifyListener(final Text textBox) {
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String text = org.apache.commons.lang.StringUtils
						.trimToEmpty(textBox.getText());
				try {
					Integer.parseInt(text);
					textBox.setToolTipText(null);
					textBox.setForeground(PageHelper.getValidColor());
				} catch (NumberFormatException ex) {
					if (org.apache.commons.lang.StringUtils.isNotBlank(text)) {
						textBox.setForeground(PageHelper.getInvalidColor());
						textBox.setToolTipText("Número inválido");
					}
				}
			}
		};
		textBox.addModifyListener(listener);
	}

	public static <T> void addIntegerFieldFocusListener(Text textBox) {
		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				Text textField = (Text) event.getSource();
				String text = (textField).getText();
				Integer num;
				try {
					num = Integer.valueOf(text);
					textField.setText(num.toString());
					textField.setForeground(PageHelper.getValidColor());
					textField.setToolTipText(null);
				} catch (NumberFormatException exc) {
					if (org.apache.commons.lang.StringUtils.isNotBlank(text)) {
						textField.setForeground(PageHelper.getInvalidColor());
						textField.setToolTipText("Número inválido");
					}
				}
			}
		};
		textBox.addFocusListener(focusListener);
	}

	public static void addIntegerRangeValidationListener(final Text textBox,
			final int infimo, final int supremo, final PreferencePage page) {
		textBox.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				final List<String> mensajesDeValidacion = Validations.rango(
						textBox.getText(), infimo, supremo);
				boolean vacio = StringUtils.isBlank(textBox.getText());
				boolean fueraDeRango = !mensajesDeValidacion.isEmpty();
				if (!vacio && fueraDeRango) {
					textBox.setToolTipText(mensajesDeValidacion.get(0));
					textBox.setForeground(PageHelper.getInvalidColor());
				}
			}

			public void focusLost(FocusEvent event) {
				textBox.setToolTipText(null);
				textBox.setForeground(PageHelper.getValidColor());
			}
		});

		textBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				boolean vacio = StringUtils.isBlank(textBox.getText());
				final List<String> mensajesDeValidacion = Validations.rango(
						textBox.getText(), infimo, supremo);
				boolean fueraDeRango = !mensajesDeValidacion.isEmpty();
				if (!vacio && fueraDeRango) {
					textBox.setToolTipText(mensajesDeValidacion.get(0));
					textBox.setForeground(PageHelper.getInvalidColor());
				} else {
					textBox.setToolTipText(null);
					textBox.setForeground(PageHelper.getValidColor());
				}
			}
		});
	}

	public static Text addMultilineFieldTabListener(Text textBox) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_TAB_NEXT) {
					event.doit = true;
				}
			}
		};
		textBox.addListener(SWT.Traverse, listener);
		return textBox;
	}

	public static void addNotAsciiPrintableCharacterListener(final Text textBox) {
		final String errMsg = "El campo no puede contener caracteres no imprimibles";
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				boolean condicion = StringUtilities.isPrintable(textBox
						.getText())
						|| StringUtilities.containsSpecialCharacters(textBox
								.getText())
						|| StringUtilities
								.containsSpecialKey(textBox.getText());

				if (condicion) {
					textBox.setToolTipText(null);
					textBox.setForeground(PageHelper.getValidColor());
				} else {
					textBox.setForeground(PageHelper.getInvalidColor());
					textBox.setToolTipText(errMsg);
				}
			}
		};
		textBox.addModifyListener(listener);
	}

	public static void addNotNullValidationListener(final Text textBox,
			final PreferencePage page) {
		final String errMsg = "El campo no puede ser nulo";
		textBox.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				if (StringUtils.isBlank(textBox.getText())) {
					textBox.setToolTipText(errMsg);
				}
			}

			public void focusLost(FocusEvent event) {
				textBox.setToolTipText(null);
			}
		});

		textBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if (StringUtils.isBlank(textBox.getText())) {
					textBox.setToolTipText(errMsg);
				} else {
					textBox.setToolTipText(null);
				}
			}
		});
	}

	public static void addEmailValidationListener(final Text textBox,
			final PreferencePage page) {
		textBox.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent event) {
				List<String> result = Validations.email(textBox.getText());
				if (!result.isEmpty()) {
					textBox.setToolTipText(result.get(0));
					textBox.setForeground(PageHelper.getInvalidColor());
				}
			}

			public void focusLost(FocusEvent event) {
				List<String> result = Validations.email(textBox.getText());
				if (result.isEmpty()) {
					textBox.setForeground(PageHelper.getValidColor());
					textBox.setToolTipText(null);
				}
			}
		});

		textBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				List<String> result = Validations.email(textBox.getText());
				if (result.isEmpty()) {
					textBox.setForeground(PageHelper.getValidColor());
					textBox.setToolTipText(null);
				} else {
					textBox.setToolTipText(result.get(0));
					textBox.setForeground(PageHelper.getInvalidColor());
				}
			}
		});
	}

}