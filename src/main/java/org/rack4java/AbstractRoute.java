package org.rack4java;

import java.util.Map;

public abstract class AbstractRoute implements Route {
	protected Rack handler;
	
	public AbstractRoute(Rack handler) {
		this.handler = handler;
	}
	
	@Override public RackResponse call(Map<String, Object> env) throws Exception {
		return handler.call(env);
	}
}
