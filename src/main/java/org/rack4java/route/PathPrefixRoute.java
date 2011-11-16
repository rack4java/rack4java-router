package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class PathPrefixRoute extends AbstractPathRoute {
	
	private String prefix;
	private boolean remove;
	
	public PathPrefixRoute(Rack handler, String prefix, boolean remove) {
		super(handler);
		this.prefix = prefix;
		this.remove = remove;
	}
	
	@Override public Rack match(Context<Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path || null == prefix) return null;
		return path.startsWith(prefix) ? this : null;
	}
	
	@Override public RackResponse call(Context<Object> env) throws Exception {
		if (remove) {
			String path = (String) env.get(Rack.PATH_INFO);
			env.put(ORIGINAL_PATH_INFO, path);
			String tail = path.substring(prefix.length());
			if (!tail.startsWith("/")) tail = "/" + tail;
			env.put(PATH_INFO, tail);
		}
		
		return super.call(env);
	}
}
