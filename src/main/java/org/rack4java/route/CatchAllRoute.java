package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;

public class CatchAllRoute extends AbstractRoute {

	public CatchAllRoute(Rack handler) {
		super(handler);
	}

	@Override public Rack match(Context<String> env) {
		return this;
	}

	@Override protected Context<String> adjust(Context<String> env) {
		return env;
	}

}
