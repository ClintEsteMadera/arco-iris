module newssite.tactics;

import model "ZNewsSys.acme" { ZNewsSys as M, ZNewsFam as T };
import op "znews0.operator.EffectOp" { EffectOp as S };
import op "org.sa.rainbow.stitch.lib.Set";
import op "org.sa.rainbow.stitch.lib.Model";
import op "org.sa.rainbow.adaptation.ArcoIrisAdaptationManager";

define boolean CONCERN_STILL_BROKEN = ArcoIrisAdaptationManager.isConcernStillBroken("RESPONSE_TIME");

/**
 * Enlist n free servers into service pool.
 * Utility: [v] R; [^] C; [<>] F
 */
tactic enlistServers (int n) {
	condition {
		// some client should be experiencing high response time
		// commented out since this is not valid for a scenario-centry solution
		// exists c : T.ClientT in M.components | c.experRespTime > M.MAX_RESPTIME;
		// there should be enough available server resources
		Model.availableServices(T.ServerT) >= n;
	}
	action {
		set servers = Set.randomSubset(Model.findServices(T.ServerT), n);
		for (T.ServerT freeSvr : servers) {
			S.activateServer(freeSvr);
		}
	}
	effect {
		// response time decreasing below threshold should result
		!CONCERN_STILL_BROKEN;
	}
}

/**
 * Deactivate n servers from service pool into free pool.
 * Utility: [^] R; [v] C; [<>] F
 */
tactic dischargeServers (int n) {
	condition {
		// there should be NO client with high response time
		// commented out since this is not valid for a scenario-centry solution
		// forall c : T.ClientT in M.components | c.experRespTime <= M.MAX_RESPTIME;
		// there should be enough servers to discharge
		// Tactic condition of applicability NOT met!
		// Set.size({ select s : T.ServerT in M.components | s.load < M.MIN_UTIL }) >= n;
	}
	action {
        set lowUtilSvrs = { select s : T.ServerT in M.components | s.load < 1000 };
		set subLowUtilSvrs = Set.randomSubset(lowUtilSvrs, n);
		for (T.ServerT s : subLowUtilSvrs) {
			S.deactivateServer(s);
		}
	}
	effect {

	}
}
