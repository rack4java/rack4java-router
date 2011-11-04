package org.rack4java.route;

import java.util.Map;

import org.rack4java.Rack;

public class CatchAllRoute extends AbstractRoute {

	public CatchAllRoute(Rack handler) {
		super(handler);
	}

	@Override public Rack match(Map<String, Object> env) {
		return this;
	}

}
