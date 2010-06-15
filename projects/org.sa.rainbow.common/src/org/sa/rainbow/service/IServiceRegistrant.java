/**
 * Created March 21, 2007.
 */
package org.sa.rainbow.service;

import java.net.Socket;

/**
 * This interface defines a registrant who has registered with Rainbow to
 * accept a new service port command.  When Rainbow's Service Port receives
 * such a command, it hands the connected socket object to the registrant.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IServiceRegistrant {

	public void connected (String cmd, Socket socket, StringBuffer buf);

}
