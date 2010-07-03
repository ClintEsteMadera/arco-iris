package commons.pref.domain;


import commons.gui.widget.Alignment;
import commons.properties.EnumProperty;

public class ColumnInfo {

	public ColumnInfo(EnumProperty label, String fieldName, Alignment alignment, Integer width) {
		this(fieldName, alignment, width);
		this.label = label;
	}

	public ColumnInfo(String fieldName, Alignment alignment, Integer width) {
		this.fieldName = fieldName;
		this.alignment = alignment;
		this.width = width;
	}

	public EnumProperty getLabel() {
		return this.label;
	}

	public void setLabel(EnumProperty label) {
		this.label = label;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Alignment getAlignment() {
		return this.alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj instanceof ColumnInfo) {
			iguales = this.equalsTo((ColumnInfo) obj);
		}
		return iguales;
	}

	private boolean equalsTo(ColumnInfo columnInfo) {
		return columnInfo.fieldName.equals(this.fieldName);
	}

	@Override
	public int hashCode() {
		int result = 17;
		if (this.fieldName != null) {
			result = 37 * result + this.fieldName.hashCode();
		}
		return result;
	}

	/**
	 * Este método está sobreescrito únicamente a fines de debugging.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("<column label=\"").append(this.label).append("\" ");
		sb.append("name=\"" + this.fieldName).append("\" ");
		sb.append("alignment=\"" + this.alignment).append("\" ");
		sb.append("width=\"" + this.width).append("\" />");
		return sb.toString();
	}

	private EnumProperty label;

	private String fieldName;

	private Alignment alignment;

	private Integer width;
}