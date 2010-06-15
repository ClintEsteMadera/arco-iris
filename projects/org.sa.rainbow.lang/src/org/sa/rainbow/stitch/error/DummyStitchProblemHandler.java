/**
 * Created August 16, 2006
 */
package org.sa.rainbow.stitch.error;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as the default problem handler when the language module
 * is used alone, rather than by the Editor, to parse a Stitch script. 
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class DummyStitchProblemHandler implements StitchProblemHandler {

	private ArrayList<StitchProblem> m_problems = null;

	/**
	 * Default constructor
	 */
	public DummyStitchProblemHandler() {
		m_problems = new ArrayList<StitchProblem>();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.error.StitchProblemHandler#setProblem(org.sa.rainbow.stitch.error.StitchProblem)
	 */
	public void setProblem(StitchProblem problem) {
		m_problems.add(problem);
	}

	public List<StitchProblem> getProblems () {
		return m_problems;
	}
	public void clearProblems () {
		m_problems.clear();
	}
}
