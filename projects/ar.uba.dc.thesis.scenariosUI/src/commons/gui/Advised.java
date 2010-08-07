package commons.gui;

import commons.gui.widget.composite.QueryComposite;

/**
 * 
 * 
 */

public interface Advised<T extends QueryComposite> {

	void rowSelected(T queryComposite);

	void rowDoubleClicked(T queryComposite);
}
