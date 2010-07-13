/*
 * Adaptation script for the News Site example.
 */

module newssite.strategies;

import lib "newssiteTactics.s";

define boolean styleApplies = Model.hasType(M, "ClientT") && Model.hasType(M, "ServerT");
define boolean cViolation = exists c : T.ClientT in M.components | c.experRespTime > M.MAX_RESPTIME;

define set servers = {select s : T.ServerT in M.components | true};
define set unhappyClients = {select c : T.ClientT in M.components | c.experRespTime > M.MAX_RESPTIME};
define int numClients = Set.size({select c : T.ClientT in M.components | true});
define int numUnhappy = Set.size(unhappyClients);
define float numUnhappyFloat = 1.0*numUnhappy;

define boolean hiLoad = exists s : T.ServerT in M.components | s.load > M.MAX_UTIL;
define boolean hiRespTime = exists c : T.ClientT in M.components | c.experRespTime > M.MAX_RESPTIME;
define boolean lowRespTime = exists c : T.ClientT in M.components | c.experRespTime < M.MIN_RESPTIME;

define float totalCost = Model.sumOverProperty("cost", servers);
define boolean hiCost = totalCost >= M.THRESHOLD_COST;

/*TO-DO: agregar logica en el ResponseMeasure para que soporte esto*/
define float avgFidelity = Model.sumOverProperty("fidelity", servers) / Set.size(servers);
define boolean lowFi = avgFidelity < M.THRESHOLD_FIDELITY;

define boolean CONCERN_STILL_BROKEN = AdaptationManagerWithScenarios.isConcernStillBroken("RESPONSE_TIME");

/* This Strategy is simple in that, while it encounters any anomaly in
 * experienced response time, it firsts enlists one new server, then lowers
 * fidelity one step, and quits
 *
 * Note:  Tested successfully in simulation, znews-varied
 */
strategy EnlistServersResponseTime
[ styleApplies ] {
  t0: (true) -> enlistServers(1) @[5000 /*ms*/] {
	  t1: (!CONCERN_STILL_BROKEN) -> done;
	  t2: (true) -> enlistServers(1) @[5000 /*ms*/] {
		t3: (default) -> done;
	  }
	  t4: (default) -> TNULL;
  }
}

/* This Strategy is triggered by the total server costs rising above acceptable
 * threshold; this Strategy reduces the number of active servers
 *
 * Note:  Tested successfully in simulation, znews-reducecost + znews-improvefidelity
 */
strategy ReduceOverallCost
[ styleApplies ] {
  t0: (true) -> dischargeServers(1) @[2000 /*ms*/] {
    t1: (!CONCERN_STILL_BROKEN) -> done;
    t2: (default) -> TNULL;
  }
}
