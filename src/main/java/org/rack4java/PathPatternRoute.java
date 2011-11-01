package org.rack4java;

import java.util.Map;
import java.util.regex.Pattern;

public class PathPatternRoute extends AbstractRoute {
	private Pattern pattern;
	
	public PathPatternRoute(Rack handler, Pattern pattern) {
		super(handler);
		this.pattern = pattern;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path || null == pattern) return false;
		return pattern.matcher(path).matches();
	}
}
