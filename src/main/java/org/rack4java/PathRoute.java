package org.rack4java;

import java.util.Map;
import java.util.regex.Pattern;

public class PathRoute extends AbstractRoute {
	public static final String ALL = ".*";
	
	private String pattern;
	private Pattern compiled;
	
	public PathRoute(String pattern, Rack handler) {
		super(handler);
		this.pattern = pattern;
		this.compiled = Pattern.compile(pattern);
	}
	
	@Override public boolean match(Map<String, Object> env) {
		if (ALL.equals(pattern)) return true;
		
		String path = (String) env.get(Rack.PATH_INFO);
		return null != path && compiled.matcher(path).find();
	}
}
