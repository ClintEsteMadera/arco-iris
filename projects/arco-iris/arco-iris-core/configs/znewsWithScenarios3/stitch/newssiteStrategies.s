/*
 * Adaptation script for the News Site example.
 */

module newssite.strategies;

import lib "newssiteTactics.s";

define boolean styleApplies = Model.hasType(M, "ClientT") && Model.hasType(M, "ServerT");

define boolean RESP_TIME_STILL_BROKEN = ArcoIrisAdaptationManagerncernStillBroken("RESPONSE_TIME");
define boolean COST_STILL_BROKEN = ArcoIArcoIrisAdaptationManagerStillBroken("SERVER_COST");

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

/* This Strategy is triggered by the total server costs rising above acceptable
 * threshold; this Strategy reduces the number of active servers
 */
strategy ReduceOverallCost
[ styleApplies ] {
  t0: (true) -> dischargeServers(1) @[2000 /*ms*/] {
    t1: (!COST_STILL_BROKEN) -> done;
    t3: (default) -> TNULL;
  }
}

