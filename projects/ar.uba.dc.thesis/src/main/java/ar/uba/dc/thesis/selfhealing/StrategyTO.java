package ar.uba.dc.thesis.selfhealing;

import java.util.Collection;

import org.sa.rainbow.stitch.core.StrategyNode;
import org.sa.rainbow.stitch.core.Var;

public class StrategyTO {

	private String name;

	private Collection<StrategyNode> nodes;

	protected Collection<Var> vars;

	public StrategyTO(String name, Collection<StrategyNode> nodes, Collection<Var> vars) {
		super();
		this.name = name;
		this.nodes = nodes;
		this.vars = vars;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<StrategyNode> getNodes() {
		return nodes;
	}

	public void setNodes(Collection<StrategyNode> nodes) {
		this.nodes = nodes;
	}

	public Collection<Var> getVars() {
		return vars;
	}

	public void setVars(Collection<Var> vars) {
		this.vars = vars;
	}

	@Override
	public String toString() {
		String str = "strategy: " + this.name + " {";
		if (!this.vars.isEmpty()) {
			str += "\n\t" + "  vars [";
			for (Var v : this.vars) {
				str += "\n\t\t" + v.toString();
			}
			str += "\n\t  ]";
		}
		for (StrategyNode node : this.nodes) {
			str += "\n\t\t" + node.toString();
		}
		str += "\n\t  ]\n\t}";

		return str;
	}
}
