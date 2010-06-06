/**
 * Created October 12, 2006.
 */
package org.sa.rainbow.monitor.sim;

import org.sa.rainbow.util.Util;

/**
 * Library of functions for calculations in the queueing model.
 * The queuing model is M/M/k/1.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class QueuingTheoryUtil {

	/**
	 * Calculates the server response time for some replication count,
	 * utilization, arrival rate, and service time
	 * @param repl    the server replication count
	 * @param util    the server utilization
	 * @param arrRate the request arrival rate to the server
	 * @param svcTime the server's service time
	 * @return the projected response time
	 */
	public static double responseTime (int repl, double util, int arrRate, double svcTime) {
		if (util >= 1.0) {
			util = 0.999;  // pick a high value less than 1
		}
		double rv = svcTime + probBusy(repl, util) * util / (arrRate * (1 - util));
		return rv;
	}
	
	/**
	 * Calculates the job population in server
	 * @param repl  the server replication count 
	 * @param util  the server utilization
	 * @return the projected job population
	 */
	public static double population (int repl, double util) {
		return probBusy(repl, util) * util / (1-util) + repl * util;
	}

	/**
	 * Calculates the probability that an arriving job finds all servers busy
	 * @param repl  the server replication count 
	 * @param util  the server utilization
	 * @return the probability of servers all busy
	 */
	public static double probBusy (int repl, double util) {
		return distribP0(repl, util) * Math.pow(repl * util, repl) / (Util.factorial(repl) * (1 - util));
	}
	
	/**
	 * Calculates the base probability that there is no job in the system
	 * @param repl  the server replication count 
	 * @param util  the server utilization
	 * @return the base probability of no job in the system
	 */
	public static double distribP0 (int repl, double util) { 
		double rv = 0.0;
		if (util > 0) {
			double sum = 0.0;
			for (int i=0; i <= repl; i++) {
				sum += Math.pow(repl * util, i) / Util.factorial(i);
			}
			rv = 1 / (sum + (util * Math.pow(repl * util, repl) / (Util.factorial(repl) * (1 - util))));
		}
		return rv;
	}
	
}
