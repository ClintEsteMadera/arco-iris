/**
 * Created December 19, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.List;

import org.sa.rainbow.core.Identifiable;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * The interface of a Gauge Manager.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IGaugeManager extends Identifiable {

	public boolean managesType (String gaugeType);
	public String gaugeType ();

	public IGauge createGauge (String gaugeName, TypeNamePair modelDesc,
			List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings);

	public boolean deleteGauge (String id);

	/**
	 * Instructs all known gauge instances of this IGaugeManager to reconfigure
	 * on their own existing config settings.
	 * @return boolean  <code>true</code> if all reconfigurations succeed, <code>false</code> otherwise.
	 */
	public boolean reconfigureGauges ();

	public boolean queryGaugeMetaInfo (List<TypeNamePair> configParamsMeta,
			List<TypeNamePair> valuesMeta);

}
