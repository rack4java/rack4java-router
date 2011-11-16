package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;

public class CatchAllRoute extends AbstractRoute {

	public CatchAllRoute(Rack handler) {
		super(handler);
	}

	@Override public Rack match(Context<Object> env) {
		return this;
	}

}
