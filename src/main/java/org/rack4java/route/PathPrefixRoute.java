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
	
	@Override public Rack match(Context<String> env) {
		String path = env.get(Rack.PATH_INFO);
		if (null == path || null == prefix) return null;
		return path.startsWith(prefix) ? this : null;
	}

	@Override protected Context<String> adjust(Context<String> env) {
		if (remove) {
			String tail = env.get(Rack.PATH_INFO).substring(prefix.length());
			if (!tail.startsWith("/")) tail = "/" + tail;
			return push(env).with(PATH_INFO, tail);
		}
		
		return env;
	}
}
