/**
 * Created April 4, 2006.
 */
package org.sa.rainbow.stitch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acmestudio.acme.model.IAcmeModel;
import org.sa.rainbow.stitch.visitor.Stitch;

import antlr.collections.AST;


/**
 * Represents a parsed Stitch Script scoped object.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class StitchScript extends ScopedEntity implements IScope {

	public List<Import> imports = null;
	/**
	 * Stores a mapping of renamed string to the original name string.
	 */
	public Map<String,String> renames = null;

	public List<Tactic> tactics = null;
	public List<Strategy> strategies = null;
	public List<Class> ops = null;
	public List<IAcmeModel> models = null;

	private List<IAcmeModel> m_snapshotModels = null;  // for tactic eval

	/**
	 * Main Constructor for a new StitchScript object.
	 * @param parent  the parent scope
	 * @param name    the name of this scope
	 * @param stitch  the Stitch evaluation context object
	 */
	public StitchScript(IScope parent, String name, Stitch stitch) {
		super(parent, name, stitch);

		imports = new ArrayList<Import>();
		renames = new HashMap<String,String>();
		tactics = new ArrayList<Tactic>();
		strategies = new ArrayList<Strategy>();
		ops = new ArrayList<Class>();
		models = new ArrayList<IAcmeModel>();
		m_snapshotModels = new ArrayList<IAcmeModel>();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.core.ScopedEntity#lookup(java.lang.String)
	 */
	@Override
	public Object lookup(String name) {
		if (name == null) return null;

		Object obj = super.lookup(name);
		if (obj == null) {  // try list of tactics
			for (Tactic t : tactics) {
				if (name.equals(t.getName())) {
					obj = t;
					break;
				}
			}
		}
		// TODO: search root scope for tactic!
		if (obj == null) {  // try looking up model reference
			List<IAcmeModel> lookupModels = null;
			if (m_snapshotModels.size() > 0) {  // use snapshots
				lookupModels = m_snapshotModels;
			} else {
				lookupModels = models;
			}
			for (IAcmeModel model : lookupModels) {
				// replace renames first
				int dotIdx = name.indexOf(".");
				String rootName = null;
				if (dotIdx > -1) {  // look at first segment only
					rootName = name.substring(0, dotIdx);
				} else {  // look at entire label for rename
					rootName = name;
				}
				if (renames.containsKey(rootName)) {  // grab replacement
					rootName = renames.get(rootName);
				}
				// substitute
				name = rootName + (dotIdx > -1 ? name.substring(dotIdx) : "");
				obj = model.lookupName(name, true);
				if (obj != null) {
					break;
				}
			}
		}

		return obj;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.core.ScopedEntity#toString()
	 */
	@Override
	public String toString() {
		return "script: name \"" + m_name + "\", renames " + renames.toString();
	}
	
	public void addRename (AST fidAST, AST tidAST) {
		renames.put(fidAST.getText(), tidAST.getText());
	}

	public boolean isApplicableForModel (IAcmeModel model) {
		return models.contains(model);
	}

	/**
	 * Creates a snapshot of the current Acme model, and causes all ensuing
	 * access of model properties to go against snapshot.  This is effective
	 * until model is "unfrozen."
	 */
	public void freezeModel () {
		/*
		m_snapshotModels.clear();
		for (IAcmeModel model : models) {
			IAcmeModel snapshot = (IAcmeModel )Ohana.instance().modelRepository().getSnapshotModel(model);
			if (snapshot != null) {
				m_snapshotModels.add(snapshot);
			}
		}
		*/
	}

	/**
	 * Discards the model snapshots, which amounts to clearing the snapshot list.
	 */
	public void unfreezeModel () {
		/*
		m_snapshotModels.clear();
		*/
	}

}
