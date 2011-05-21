package ar.uba.dc.arcoiris.selfhealing;

import java.io.File;

import junit.framework.TestCase;

public class StitchLoaderTest extends TestCase {

	private static final String CORRECT_PATH = "configs/TestCase1/stitch";

	private static final String INCORRECT_PATH = "configs/TestCase1/dao";

	public void testStitchesAreLoadedFromCorrectPath() {
		StitchLoader stitchLoader = new StitchLoader(new File(CORRECT_PATH), false);
		assertFalse(stitchLoader.getParsedStitches().isEmpty());
	}

	public void testNoStitchesLoadedFromWrongPath() {
		StitchLoader stitchLoader = new StitchLoader(new File(INCORRECT_PATH), false);
		assertTrue(stitchLoader.getParsedStitches().isEmpty());
	}
}
