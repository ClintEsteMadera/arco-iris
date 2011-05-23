package commons.datastructures;

import java.util.HashMap;
import java.util.Map;

/**
 * Lazy map. When a key is not found, the associated element is created. This might be useful, for example, to build
 * caches.
 */
public abstract class LazyMap<K, V> {

	private Map<K, V> m_map = new HashMap<K, V>();

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
};
