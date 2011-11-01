package org.rack4java;

import java.util.Map;
import java.util.regex.Pattern;

public class PathRoute extends AbstractRoute {
	private String prefix;
	private Pattern pattern;
	
	public PathRoute(Rack handler, String prefix) {
		super(handler);
		this.prefix = prefix;
	}
	
	public PathRoute(Rack handler, Pattern pattern) {
		super(handler);
		this.pattern = pattern;
	}
	
	@Override public boolean match(Map<String, Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path) return false;
		if (null != pattern) return pattern.matcher(path).matches();
		return path.startsWith(prefix);
	}
}
