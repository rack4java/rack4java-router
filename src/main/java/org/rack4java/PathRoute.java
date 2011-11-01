package org.rack4java;

import java.util.Map;

public class PathRoute extends AbstractRoute {
	private String pattern;
	
	public PathRoute(String pattern, Rack handler) {
		super(handler);
		this.pattern = pattern;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		if ("*".equals(pattern)) return true;
		
		String path = (String) env.get(Rack.PATH_INFO);
		return path.matches(pattern);
	}
}
