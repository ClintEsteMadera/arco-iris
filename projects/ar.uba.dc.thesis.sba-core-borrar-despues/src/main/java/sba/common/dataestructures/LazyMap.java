package sba.common.dataestructures;

import java.util.HashMap;
import java.util.Map;

/**
 * Map que se carga en modalidad "lazy". Cuando no encuentra una clave, crea el elemento asociado
 * (es util para construir caches.)
 */
public abstract class LazyMap<K, V> {

	public V get(K key) {
		V ret = m_map.get(key);
		if (ret == null) {
			ret = createElement(key);
			m_map.put(key, ret);
		}
		return ret;
	}

	public void put(K key, V object) {
		m_map.put(key, object);
	}

	public abstract V createElement(K key);

	private Map<K, V> m_map = new HashMap<K, V>();

};
