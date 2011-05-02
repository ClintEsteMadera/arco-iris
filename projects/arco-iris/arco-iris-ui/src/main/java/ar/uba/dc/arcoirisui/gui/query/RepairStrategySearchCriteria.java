package ar.uba.dc.arcoirisui.gui.query;

import ar.uba.dc.arcoiris.selfhealing.StrategyTO;

import commons.query.BaseSearchCriteria;

public class RepairStrategySearchCriteria extends BaseSearchCriteria<StrategyTO> {

	private static final long serialVersionUID = 1L;

	private String stitchDirectory;

	public String getStitchDirectory() {
		return this.stitchDirectory;
	}

	public void setStitchDirectory(String stitchDirectory) {
		this.stitchDirectory = stitchDirectory;
	}

}
