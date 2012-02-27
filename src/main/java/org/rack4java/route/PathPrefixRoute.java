package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;

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

	@Override protected Context<Object> adjust(Context<Object> env) {
		if (remove) {
			String tail = ((String) env.get(Rack.PATH_INFO)).substring(prefix.length());
			if (!tail.startsWith("/")) tail = "/" + tail;
			return push(env).with(PATH_INFO, tail);
		}
		
		return env;
	}
}
