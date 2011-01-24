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

define boolean RESP_TIME_STILL_BROKEN = AdaptationManagerWithScenarios.isConcernStillBroken("RESPONSE_TIME");

/* Esta estrategia repara el response time pero es muy costosa,
 * no deberia ser elegida ya que existe un escenario de Server Cost que se rompería.
 */
strategy ExpensiveReduceResponseTime
[ styleApplies ] {
  t0: (true) -> enlistServers(4) @[2000 /*ms*/]{
  	t1: (default) -> TNULL;
  }
}

/* This Strategy will drop fidelity once, observe, then drop again if necessary.
 *
 * Note:  Tested successfully in simulation, znews-brute
 */
strategy BruteReduceResponseTime
[ styleApplies ] {
  t0: (true) -> lowerFidelity(2, 100) @[5000 /*ms*/] {
    t1: (!RESP_TIME_STILL_BROKEN) -> done;
    t2: (RESP_TIME_STILL_BROKEN) -> lowerFidelity(2, 100) @[8000 /*ms*/] {
      t2a: (!RESP_TIME_STILL_BROKEN) -> done;
      t2b: (default) -> TNULL;  // in this case, we have no more steps to take
    }
  }
}