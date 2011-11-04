package org.rack4java.route;

import java.util.Map;

import org.rack4java.Rack;

public class PathPrefixRoute extends AbstractRoute {
	private String prefix;
	private boolean remove;
	
	public PathPrefixRoute(Rack handler, String prefix, boolean remove) {
		super(handler);
		this.prefix = prefix;
		this.remove = remove;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path || null == prefix) return false;
		return path.startsWith(prefix);
	}
}
