package ar.uba.dc.arcoiris.util.sim.graphics;

public class GraphicPoint {
	private String property;
	private Number avgValue;

	public GraphicPoint(String property, Number avgValue) {
		this.property = property;
		this.avgValue = avgValue;
	}

	public String getProperty() {
		return property;
	}

	public Number getAvgValue() {
		return avgValue;
	}
}