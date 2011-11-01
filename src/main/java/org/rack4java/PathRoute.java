package org.rack4java;

import java.util.Map;

public class PathRoute implements Route {
	private String pattern;
	private Rack handler;
	
	public PathRoute(String pattern, Rack handler) {
		this.pattern = pattern;
		this.handler = handler;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		if ("*".equals(pattern)) return true;
		
		String path = (String) env.get(Rack.PATH_INFO);
		return path.matches(pattern);
	}
	
	@Override public RackResponse call(Map<String, Object> env) throws Exception {
		return handler.call(env);
	}
}
