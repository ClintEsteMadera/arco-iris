/**
 * Created June 30, 2006
 */
package org.sa.rainbow.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.sa.rainbow.adaptation.AdaptationManager;
import org.sa.rainbow.core.IDisposable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.ServiceConstants;
import org.sa.rainbow.monitor.SystemDelegate;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.util.Pair;
import org.sa.rainbow.util.Util;

/**
 * The GUI for observing the Rainbow infrastructure.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class RainbowGUI implements IDisposable {
	// Index values
	public static final int ID_MODEL_MANAGER = 0;
	public static final int ID_ARCH_EVALUATOR = 1;
	public static final int ID_ADAPTATION_MANAGER = 2;
	public static final int ID_EXECUTOR = 3;
	public static final int ID_TARGET_SYSTEM = 4;
	public static final int ID_TRANSLATOR = 5;
	public static final int ID_EVENT_BUSES = 6;
	public static final int ID_ORACLE_MESSAGE = 7;
	public static final int ID_FILLER1 = 8;
	public static final int PANEL_COUNT = 9;  // make sure m_colors has same count

	// Layout distances and other constants
	public static final int PANEL_MARGIN = 10;
	public static final int PANEL_PADDING = 8;
	public static final int PANEL_BORDER = 4;
	public static final int PANEL_ROWS = 3;
	public static final int PANEL_COLUMNS = 3;
	public static final int TEXT_ROWS = 10;
	public static final int TEXT_COLUMNS = 40;
	public static final float TEXT_FONT_SIZE = 9.0f;
	public static final int MAX_TEXT_LENGTH = 100000;
	/** Convenience constant: size of text field to set to when Max is exceeded. */
	public static final int TEXT_HALF_LENGTH = 50000;

    public static void main(String[] args) {
        RainbowGUI gui = new RainbowGUI();
        gui.display();
    }

    private JFrame m_frame = null;
    private JTextArea[] m_textAreas = null;
    private JScrollPane[] m_panes = null;
    private Color[] m_colors = {
		/* purple */ new Color(188, 188, 250),
		/* pink */   new Color(255, 145, 255),
		Color.RED,
		/* green */  new Color(0, 255, 64),
		Color.CYAN,
		/* orange */ new Color(255, 128, 64),
		Color.BLUE,
		Color.WHITE,
		Color.GRAY
    };
    private int[] m_order = { 7, 2, 8, 3, 0, 1, 5, 4, 6 };

    public RainbowGUI () {
    }

    /* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose () {
		m_frame.setVisible(false);
		m_frame.dispose();
		m_frame = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed () {
		return m_frame == null;
	}

	public void quit () {
		Rainbow.signalTerminate();
    }

	public void forceQuit () {
		Rainbow.isDone();
		Util.pause(IRainbowRunnable.LONG_SLEEP_TIME);
		System.exit(Rainbow.EXIT_VALUE_DESTRUCT);
    }

    public void display () {
		if (m_frame != null) return;

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	show();
            }
        });
	}

    /**
     * Writes text to the panel without a newline.
     * @param panelID
     * @param text
     */
    public void writeTextSL (int panelID, String text) {
    	if (m_textAreas[panelID] == null) return;
    	m_textAreas[panelID].append(text);
    	m_textAreas[panelID].setCaretPosition(m_textAreas[panelID].getText().length());
    }
    public void writeText (int panelID, String text) {
    	if (m_textAreas[panelID] == null) return;
    	m_textAreas[panelID].append(text + "\n");
    	m_textAreas[panelID].setCaretPosition(m_textAreas[panelID].getText().length());
    	if (m_textAreas[panelID].getText().length() > MAX_TEXT_LENGTH) {
    		m_textAreas[panelID].setText(m_textAreas[panelID].getText().substring(TEXT_HALF_LENGTH));
    	}
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private void show () {
		if (m_frame != null) return;

        // Use Window's default decorations.
        JFrame.setDefaultLookAndFeelDecorated(false);
        JDialog.setDefaultLookAndFeelDecorated(false);

        m_textAreas = new JTextArea[PANEL_COUNT];
        m_panes = new JScrollPane[PANEL_COUNT];

        //Create and set up the window.
        m_frame = new JFrame("Rainbow Framework GUI - Target " + Rainbow.property(Rainbow.PROPKEY_TARGET_NAME));
        m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        m_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
        });

        //Create the menu bar with light gray background.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.lightGray);

        JMenu menu = new JMenu("Oracle");
        menu.setMnemonic(KeyEvent.VK_O);
        createOracleMenu(menu);
        menuBar.add(menu);

        menu = new JMenu("Delegate");
        menu.setMnemonic(KeyEvent.VK_D);
        createDelegateMenu(menu);
        menuBar.add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        createHelpMenu(menu);
        menuBar.add(menu);

        //Set the menu bar
        m_frame.setJMenuBar(menuBar);

        // add text areas to the content pane.
        Container contentPane = m_frame.getContentPane();
        contentPane.getInsets().set(10, 10, 10, 10);
        GridBagLayout gridBag = new GridBagLayout();
        contentPane.setLayout(gridBag);
        for (int i : m_order) {
        	m_panes[i] = createTextArea(i);
            if (m_colors[i] == Color.GRAY) {
            	m_panes[i].setVisible(false);
            }
        	contentPane.add(m_panes[i]);
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(1,1,1,1);

        /* Proceed to set constraints in the following order:
         *  | 1 2 3 |
         *  | 4 5 6 |
         *  | 7 8 9 |
         */
        for (int i=0; i < m_order.length; ++i) {
        	// anchor WEST constraint against edge to the left
        	int x = i % 3;
        	int y = i / 3;
        	c.gridx = x;
        	c.gridy = y;
        	gridBag.setConstraints(m_panes[m_order[i]], c);
        }

        // Display the window.
        m_frame.pack();
        m_frame.setVisible(true);
    }

    private JScrollPane createTextArea(int area) {
		m_textAreas[area] = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
		m_textAreas[area].setFont(m_textAreas[area].getFont().deriveFont(TEXT_FONT_SIZE));
        m_textAreas[area].setEditable(false);
        m_textAreas[area].setLineWrap(true);
        m_textAreas[area].setWrapStyleWord(true);
        m_textAreas[area].setAutoscrolls(true);
        m_panes[area] = new JScrollPane(m_textAreas[area],
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        m_panes[area].setAutoscrolls(true);
        Border border = BorderFactory.createMatteBorder(
        		PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER,
        		m_colors[area]);
        m_panes[area].setBorder(border);
		return m_panes[area];
	}

	/**
	 * Creates a series of Oracle-specific menu items.
	 * @param menu  the menu on which to create items.
	 */
	private void createOracleMenu(JMenu menu) {
		JMenuItem item = null;

        // Management menu item
        item = new JMenuItem("Toggle adaptation switch");
        item.setMnemonic(KeyEvent.VK_A);
        item.setToolTipText("Toggles whether self-adaptation is enabled, default is ON");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		boolean b = !((AdaptationManager )Oracle.instance().adaptationManager()).adaptationEnabled();
        		((AdaptationManager )Oracle.instance().adaptationManager()).setAdaptationEnabled(b);
        		writeText(ID_ORACLE_MESSAGE, "Adaptation switched " + (b?"ON":"OFF"));
        	}
        });
        menu.add(item);
        item = new JMenuItem("Clear consoles");
        item.setMnemonic(KeyEvent.VK_C);
        item.setToolTipText("Clears all the GUI consoles");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		for (JTextArea textArea : m_textAreas) {
        			textArea.setText("");
        		}
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());
        // Termination menu item
        item = new JMenuItem("Sleep Master+Delegate");
        item.setMnemonic(KeyEvent.VK_S);
        item.setToolTipText("Signals Oracle and all Delegates to terminate, then sleep");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		quit();
        	}
        });
        menu.add(item);
        item = new JMenuItem("Restart Master+Delegate");
        item.setMnemonic(KeyEvent.VK_R);
        item.setToolTipText("Signals Oracle and all Delegates to terminate, then restart");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		Rainbow.signalTerminate(Rainbow.ExitState.RESTART);
        	}
        });
        menu.add(item);
        item = new JMenuItem("Destroy Master+Delegate");
        item.setMnemonic(KeyEvent.VK_D);
        item.setToolTipText("Signals Oracle and all Delegates to terminate, then self-destruct");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		Rainbow.signalTerminate(Rainbow.ExitState.DESTRUCT);
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());
        // Quit menu item
        item = new JMenuItem("Force Quit");
        item.setMnemonic(KeyEvent.VK_Q);
        item.setToolTipText("Forces the Oracle component to quit immediately");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		forceQuit();
        	}
        });
        menu.add(item);
	}

	/**
	 * Creates a series of Delegate-specific menu items.
	 * @param menu  the menu on which to create items.
	 */
	private void createDelegateMenu(JMenu menu) {
		JMenuItem item = null;

        // Probe start menu item
        item = new JMenuItem("Start Probes");
        item.setMnemonic(KeyEvent.VK_P);
        item.setToolTipText("Signals all Delegates to start the probes (key: rainbow.delegate.startProbesOnInit)");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		if (! Rainbow.inSimulation()) {
                	((SystemDelegate )Oracle.instance().targetSystem()).signalStartProbes();
        		}
        	}
        });
        menu.add(item);
        // Probe kill menu item
        item = new JMenuItem("Kill Probes");
        item.setMnemonic(KeyEvent.VK_K);
        item.setToolTipText("Signals all Delegates to kill the probes");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		if (! Rainbow.inSimulation()) {
                	((SystemDelegate )Oracle.instance().targetSystem()).signalKillProbes();
        		}
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());

        // Test Effector 1 menu item
        item = new JMenuItem("T1 KillDelegate Effector");
        item.setMnemonic(KeyEvent.VK_1);
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		String hostname = JOptionPane.showInputDialog(m_frame, "Please provide hostname of Delegate to kill");
        		if (hostname != null && hostname.length() > 0) {
        			String[] args = { "kill" };
        			testEffector(hostname, "KillDelegate", args);
        		}
        	}
        });
        menu.add(item);
        // Test Effector 2 menu item
        item = new JMenuItem("T2 Test An Effector");
        item.setMnemonic(KeyEvent.VK_2);
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		String effID = JOptionPane.showInputDialog(m_frame, "Please identify Effector to test: 'name@location' (or just 'name' for localhost)");
        		if (effID == null || effID.length() == 0)
        			writeText(ID_ORACLE_MESSAGE, "Sorry, Oracle needs to know what effector to invoke!");
        		Pair<String,String> namePair = Util.decomposeID(effID);
        		if (namePair.secondValue() == null) {  // default to localhost
        			namePair.setSecondValue("localhost");
        		}
        		String argStr = JOptionPane.showInputDialog(m_frame, "Please provide String arguments, separated by '|'");
        		String[] args = null;
        		if (argStr == null || argStr.length() == 0) {
        			args = new String[0];
        		} else {
        			args = argStr.split("\\s*\\|\\s*");
        		}
        		// run the test
        		testEffector(namePair.secondValue(), namePair.firstValue(), args);
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());

        // Delegate control menu item
        item = new JMenuItem("Restart Delegates");
        item.setMnemonic(KeyEvent.VK_R);
        item.setToolTipText("Signals all the Delegates to terminate and restart");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		// issues restart to all delegates
        		signalDelegates(ServiceConstants.SVC_CMD_RESTART);
        	}
        });
        menu.add(item);
        item = new JMenuItem("Sleep Delegates");
        item.setMnemonic(KeyEvent.VK_S);
        item.setToolTipText("Signals all the Delegates to terminate and sleep");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		// issues sleep to all delegates
        		signalDelegates(ServiceConstants.SVC_CMD_SLEEP);
        	}
        });
        menu.add(item);
        item = new JMenuItem("Destroy Delegates");
        item.setMnemonic(KeyEvent.VK_D);
        item.setToolTipText("Signals all the Delegates to terminate and the self-destruct");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		// issues destroy to all delegates
        		signalDelegates(ServiceConstants.SVC_CMD_STOP);
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());

        // RainDropD control
        item = new JMenuItem("Awaken RainDropD...");
        item.setMnemonic(KeyEvent.VK_A);
        item.setToolTipText("Given a hostname, awakens the Delegate RainDrop Daemon on that host");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		String hostname = JOptionPane.showInputDialog(m_frame, "Please provide a hostname with a sleeping RainDropD");
        		if (hostname != null && hostname.length() > 0) {
        			RemoteControl.waker(hostname, RemoteControl.WAKER_RESTART);
        		}
        	}
        });
        menu.add(item);
        item = new JMenuItem("Kill RainDropD...");
        item.setMnemonic(KeyEvent.VK_L);
        item.setToolTipText("Given a hostname, kills the Delegate RainDropD on that host");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		String hostname = JOptionPane.showInputDialog(m_frame, "Please provide a hostname with a sleeping RainDropD");
        		if (hostname != null && hostname.length() > 0) {
        			RemoteControl.waker(hostname, RemoteControl.WAKER_KILL);
        		}
        	}
        });
        menu.add(item);
        
	}

	/**
	 * Creates the help menu items.
	 * @param menu  the menu on which to create items.
	 */
	private void createHelpMenu (JMenu menu) {
		JMenuItem item = null;

        item = new JMenuItem("Software Update...");
        item.setMnemonic(KeyEvent.VK_U);
        item.setToolTipText("Allows the update of the Oracle and RainbowDelegate software components");
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		String hostname = JOptionPane.showInputDialog(m_frame,
        				"Please provide a hostname to send update software.\n'*' for all delegate hosts.");
        		if (hostname != null && hostname.length() > 0) {
            		final JFileChooser fc = new JFileChooser(Rainbow.instance().getBasePath());
            		int rv = fc.showDialog(m_frame, "Select File");
            		if (rv == JFileChooser.APPROVE_OPTION) {
            			File file = fc.getSelectedFile();
        				writeText(ID_ORACLE_MESSAGE, "Attempting remote update on " + hostname + " with " + file.getAbsolutePath());
                		RemoteControl.updater(hostname, file.getParentFile(), file.getName());
            		}
        		}
        	}
        });
        menu.add(item);
        menu.add(new JSeparator());
        item = new JMenuItem("About");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) {
        		JOptionPane.showMessageDialog(m_frame, "Will be available soon...", "No Help", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        menu.add(item);
	}

	private void signalDelegates (String cmd) {
		if (! Rainbow.inSimulation()) {
			String[] locs = ((SystemDelegate )Oracle.instance().targetSystem()).delegateLocations();
			for (String loc : locs) {
				if (loc.equals(Rainbow.property(Rainbow.PROPKEY_MASTER_LOCATION))) continue;
				writeText(ID_ORACLE_MESSAGE, "Signalling RainbowDelegate@" + loc + " to " + cmd);
				RemoteControl.restarter(loc, cmd);
			}
		}
	}

	// GUI invoked test methods
	private void testEffector (String target, String effName, String[] args) {
		writeText(ID_EXECUTOR, "Testing Effector " + effName + Arrays.toString(args));
		IEffector.Outcome outcome = Rainbow.instance().sysOpProvider().execute(effName, target, args);
		writeText(ID_EXECUTOR, "  - outcome: " + outcome);
	}

}
