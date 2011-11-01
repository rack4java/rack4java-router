package org.rack4java;

import java.util.Map;

public class PathPrefixRoute extends AbstractRoute {
	private String prefix;
	
	public PathPrefixRoute(Rack handler, String prefix) {
		super(handler);
		this.prefix = prefix;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path || null == prefix) return false;
		return path.startsWith(prefix);
	}
}
