// ============================================================================
// $Id: lib.js,v 1.1 2009/06/10 22:21:48 cvsuribe Exp $
// ============================================================================
/**
 * @author H. Adrián Uribe
 */
// Default window size for openWindow

var WIN_WIDTH = 0.75;
var WIN_HEIGHT = 0.70;

/**
 * Opens a new window and returns its reference.
 *
 * NOTE: null parameters assume default values.
 * NOTE: sizes and coordinates may be specified either in pixels (n > 1), or as
 * a percentage of the screen resolution (0 <= n < 1).
 *
 * @param winName Window name. Default: "NewWindow".
 * @param url URL of the page to display in the window.
 * @param width Window width. Default: 0.75.
 * @param height Window height. Default: 0.70.
 * @param x Window horizontal coordinate. Default: screen centered.
 * @param y Window vertical coordinate. Default: screen centered.
 * @param status Whether to include a status bar in the window. Default: true.
 * @param scrollbars Whether to include (if necessary) scrollbars in the window. Default: true.
 *
 * @return A new Window object.
 */
function openWindow(winName, url, width, height, x, y, status, scrollbars) {
	var SCR_WIDTH = (self.top && self.top.screen && self.top.screen.width)
			? self.top.screen.width : 640;
	// NOTE: screen.height is wrong, instead we use: screen.width * 0.75
	var SCR_HEIGHT = SCR_WIDTH * 0.75;
	var opts = "resizable";
	// Handle default parameter values
	if (! winName) {
		winName = "NewWindow";
	}
	if (width == null) {
		width = WIN_WIDTH;
	}
	if (height == null) {
		height = WIN_HEIGHT;
	}
	if (status == null || status) {
		opts += ",status";
	}
	if (scrollbars == null || scrollbars) {
		opts += ",scrollbars";
	}
	// Convert sizes and coordinates from percentage to pixels when needed
	if (width < 1) {
		width *= SCR_WIDTH;
	}
	if (height < 1) {
		height *= SCR_HEIGHT;
	}
	if (x == null) {
		// center horizontally
		x = (SCR_WIDTH - width) / 2;
	}
	else if (x < 1) {
		x *= SCR_WIDTH;
	}
	if (y == null) {
		// center vertically
		y = (SCR_HEIGHT - height) / 2;
	}
	else if (y < 1) {
		y *= SCR_HEIGHT;
	}
	opts += ",width="  + width;
	opts += ",height=" + height;
	opts += ",left=" + x + ",screenX=" + x;
	opts += ",top="  + y + ",screenY=" + y;
	var win = self.open(url, winName, opts);
	win.focus();
	return win;
}
