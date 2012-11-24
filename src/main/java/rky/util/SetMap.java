package rky.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetMap<K,V> {
	Map<K, Set<V>> contents = new HashMap<K, Set<V>>();
	
	public Set<K> keySet() {
		return contents.keySet();
	}
	
	public Set<V> get(K key) {
		return ensureKey(key);
	}
	
	public boolean put(K key, V value) {
		return ensureKey(key).add(value);
	}
	
	private Set<V> ensureKey(K key) {
		Set<V> values = contents.get(key);
		if( values == null ) {
			values = new HashSet<V>();
			contents.put(key, values);
		}
		return values;
	}
	
	public boolean contains(K key) {
		return contents.containsKey(key);
	}
	
	public boolean contains(K key, V value) {
		Set<V> values = contents.get(key);
		if( values == null ) return false;
		return values.contains(value);
	}
	
	public Set<V> remove(K key) {
		return contents.remove(key);
	}
	
	public boolean remove(K key, V value) {
		if( !contents.containsKey(key) )
			return false;
		return get(key).remove(value);
	}
	
	public String toString() {
		return contents.toString();
	}
	
	/**
	 * Takes a SetMap whose keys and values are of the same type, and returns one without reverse mappings (i.e. if there exists a mapping a -> b, there will not exist a mapping b -> a).
	 * @param map
	 * @return
	 */
	static public <T> SetMap<T, T> distinct(SetMap<T, T> map) {
		SetMap<T, T> dist = new SetMap<T, T>();
		for( Map.Entry<T, Set<T>> entry : map.contents.entrySet() ) {
			T key = entry.getKey();
			for( T val : entry.getValue() ) {
				if( !dist.contains(val, key) )
					dist.put(key, val);
			}
		}
		return dist;
	}
}
