/*
 * Adaptation script for the News Site example.
 */

module newssite.strategies;

import lib "newssiteTactics.s";

define boolean styleApplies = Model.hasType(M, "ClientT") && Model.hasType(M, "ServerT");

define boolean COST_STILL_BROKEN = ArcoIrisAdaptationManager.isConcernStillBroken("SERVER_COST");
define boolean RESP_TIME_STILL_BROKEN = ArcoIrisAdaptationManager.isConcernStillBroken("RESPONSE_TIME");

/* This Strategy is simple in that, while it encounters any anomaly in
 * experienced response time, it enlists one new server.
 *
 * Note:  Tested successfully in simulation, znews-varied
 */
strategy EnlistServerResponseTime
[ styleApplies ] {
  t0: (true) -> enlistServers(1) @[5000 /*ms*/] {
	  t1: (!RESP_TIME_STILL_BROKEN) -> done;
	  t2: (default) -> TNULL;
  }
}

/* This Strategy will drop fidelity once, observe, then drop again if necessary.
 *
 * Note:  Tested successfully in simulation, znews-brute
 */
strategy LowerFidelityReduceResponseTime
[ styleApplies ] {
  t0: (true) -> lowerFidelity(2, 100) @[5000 /*ms*/] {
    t1: (!RESP_TIME_STILL_BROKEN) -> done;
    t2: (RESP_TIME_STILL_BROKEN) -> lowerFidelity(2, 100) @[8000 /*ms*/] {
      t2a: (!RESP_TIME_STILL_BROKEN) -> done;
      t2b: (default) -> TNULL;  // in this case, we have no more steps to take
    }
  }
}