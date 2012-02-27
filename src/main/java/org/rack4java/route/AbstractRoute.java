package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.Route;

public abstract class AbstractRoute implements Route, Rack {
	protected Rack handler;
	
	public AbstractRoute(Rack handler) {
		this.handler = handler;
	}
	
	@Override public RackResponse call(Context<Object> env) throws Exception {
		return handler.call(adjust(env));
	}
	
	protected abstract Context<Object> adjust(Context<Object> env);
}
