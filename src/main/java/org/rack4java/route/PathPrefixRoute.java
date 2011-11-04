package org.rack4java.route;

import java.util.Map;

import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class PathPrefixRoute extends AbstractRoute {
	public static final String ORIGINAL_PATH_INFO = "ORIGINAL_" + Rack.PATH_INFO;
	
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
	
	@Override public RackResponse call(Map<String, Object> env) throws Exception {
		String path = (String) env.get(Rack.PATH_INFO);
		if (remove) {
			env.put(ORIGINAL_PATH_INFO, path);
			String tail = path.substring(prefix.length());
			if (!tail.startsWith("/")) tail = "/" + tail;
			env.put(PATH_INFO, tail);
		}
		
		return super.call(env);
	}
}
