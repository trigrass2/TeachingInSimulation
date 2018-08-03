package com.cas.sim.tis.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6493451731335264727L;

	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	@Override
	public Enumeration<Object> keys() {
		return Collections.<Object> enumeration(keys);
	}

	@Override
	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);

	}

	@Override
	public Set<Object> keySet() {
		return keys;
	}

	@Override
	public Set<String> stringPropertyNames() {
		Set<String> set = new LinkedHashSet<String>();
		for (Object key : this.keys) {
			set.add((String) key);
		}
		return set;
	}

}