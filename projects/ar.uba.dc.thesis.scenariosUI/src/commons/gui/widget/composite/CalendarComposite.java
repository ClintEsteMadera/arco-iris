/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: CalendarComposite.java,v 1.20 2008/03/18 17:43:31 cvspasto Exp $
 */

package commons.gui.widget.composite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sba.common.utils.DateUtils;
import sba.common.validation.string.CharacterSet;
import sba.common.validation.string.CompoundCharacterSet;
import sba.common.validation.string.LiteralCharacterSet;

import commons.gui.GuiStyle;
import commons.gui.model.types.StringConfiguration;
import commons.gui.model.types.StringEditParameters;
import commons.gui.swt.text.FilteredTextFormatter;
import commons.gui.swt.text.StringTextFilter;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.SWTCalendarDialog;
import commons.gui.widget.factory.LabelFactory;

/**
 * Modela un calendario.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.20 $ $Date: 2008/03/18 17:43:31 $
 */

public class CalendarComposite extends SimpleComposite {

	public CalendarComposite(Composite parent, boolean readOnly) {
		this(parent, readOnly, DEFAULT_DATE_FORMAT);
	}

	public CalendarComposite(Composite parent, boolean readOnly, DateFormat dateFormat) {
		super(parent, readOnly, 2);

		if (readOnly) {
			this.calendarControl = LabelFactory.createLabel(this);
		} else {
			this.createTextBox(this);
			this.createChangeDateButton(this);
		}
		this.setDateFormat(dateFormat);
	}

	public Control getCalendarControl() {
		return this.calendarControl;
	}

	public void cleanText() {
		if (this.readOnly) {
			((Label) this.calendarControl).setText("");
		} else {
			((Text) this.calendarControl).setText("");
		}
	}

	public Calendar getSelectionAsCalendar() {
		try {
			final String text = ((Text) calendarControl).getText();
			if (text == null || text.length() == 0) {
				return null;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateFormat.parse(text));
			return cal;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Obtiene el formato de fecha
	 * @return
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * Setea el formato de fecha
	 * @param dateFormat
	 */
	public void setDateFormat(DateFormat dateFormat) {
		if(dateFormat == this.dateFormat){
			return;
		}
		
		this.dateFormat = dateFormat;

		if (this.formatter != null) {
			if (this.calendarControl instanceof Text) {
				this.formatter.unInstall((Text) this.calendarControl);
			}
			this.formatter = null;
		}

		if (dateFormat instanceof SimpleDateFormat) {
			this.formatter = createFormatter((SimpleDateFormat)dateFormat);

			if (this.calendarControl instanceof Text) {
				this.formatter.install((Text) this.calendarControl);
				((Text) this.calendarControl).setToolTipText(((SimpleDateFormat)dateFormat).toPattern());
			}
		}
	}
	
	private FilteredTextFormatter createFormatter(SimpleDateFormat sdf){
		final String pattern=sdf.toPattern();
		
		final StringEditParameters p = new StringEditParameters();
		p.validCharacters = new CompoundCharacterSet(CharacterSet.DIGIT,
				new LiteralCharacterSet(pattern));
		p.invalidCharacters = CharacterSet.LETTER;
		
		final StringTextFilter filter = new StringTextFilter(new StringConfiguration(p));

		return new FilteredTextFormatter(filter);
	}

	private void createTextBox(SimpleComposite parent) {
		this.calendarControl = new Text(parent, GuiStyle.DEFAULT_TEXTBOX_STYLE);
		this.calendarControl.setBackground(PageHelper.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		GridData gridData = new GridData();
		gridData.widthHint = PageHelper.getCantidadDePixels(10);
		gridData.horizontalAlignment = SWT.CENTER;
		this.calendarControl.setLayoutData(gridData);
	}

	private void createChangeDateButton(SimpleComposite parent) {
		this.changeDateButton = new Button(parent, SWT.ARROW | SWT.RIGHT);

		this.changeDateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Point dialogLocation = PageHelper.getDisplay().map(changeDateButton, null, 0, 0);
				Calendar today = DateUtils.getCurrentDatetimeAsCalendar();
				Calendar selectionAsCalendar = getSelectionAsCalendar();
				calendarDialog = new SWTCalendarDialog(getShell(), dialogLocation,
						selectionAsCalendar, today);

				if (calendarDialog.open() == Window.OK) {
					final Calendar calendar = calendarDialog.getSelectedCalendar();
					if (calendar != null) {
						String s = dateFormat.format(calendar.getTime());
						((Text) calendarControl).setText(s);
					} else {
						cleanText();
					}
				}
			}
		});
	}

	@Override
	protected void applyLayout() {
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		super.setLayoutData(gridData);
		GridLayout layout = new GridLayout(3, false);
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		super.setLayout(layout);
	}

	private SWTCalendarDialog calendarDialog;

	private Control calendarControl;

	private Button changeDateButton;

	private DateFormat dateFormat;

	private FilteredTextFormatter formatter;

	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

}