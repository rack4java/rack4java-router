package org.rack4java.route;

import java.util.Map;

import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.Route;

public abstract class AbstractRoute implements Route {
	protected Rack handler;
	
	public AbstractRoute(Rack handler) {
		this.handler = handler;
	}
	
	@Override public RackResponse call(Map<String, Object> env) throws Exception {
		return handler.call(env);
	}
}
