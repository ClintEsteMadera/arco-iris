/**
 * Created January 18, 2007.
 */
package org.sa.rainbow.health;

import org.sa.rainbow.core.Identifiable;

/**
 * A data structure to hold basic information about a Rainbow Cloud.
 * There is specialized info stored for the RainbowDelegate, which should
 * probably be promoted into a subclass for RainbowDelegate at some point. 
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class RainbowCloudRef implements Identifiable {

	private String m_id = null;
	private IRainbowHealthProtocol.CloudType m_type = null;
	private String m_location = null;
	private Beacon m_beacon = null;
	private boolean m_started = false;

	/**
	 * Main Constructor.
	 * @param id  the unique ID of the RainbowCloud
	 * @param type  the cloud type.
	 * @param location  the deployment location, in the form of unique hostname or IP
	 * @param beaconPer  the beacon period
	 */
	public RainbowCloudRef (String id, IRainbowHealthProtocol.CloudType type, String location, long beaconPer) {
		m_id = id;
		m_type = type;
		m_location = location;
		m_beacon = new Beacon(beaconPer);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.Identifiable#id()
	 */
	public String id () {
		return m_id;
	}

	public IRainbowHealthProtocol.CloudType type () {
		return m_type;
	}

	public String location () {
		return m_location;
	}

	public Beacon beacon () {
		return m_beacon;
	}

	public boolean isStarted () {
		return m_started;
	}
	public void setStarted (boolean b) {
		m_started = b;
	}

}
