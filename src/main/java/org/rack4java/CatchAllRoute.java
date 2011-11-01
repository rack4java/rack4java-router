package org.rack4java;

import java.util.Map;

public class CatchAllRoute extends AbstractRoute {

	public CatchAllRoute(Rack handler) {
		super(handler);
	}

	@Override public boolean match(Map<String, Object> env) {
		return true;
	}

}
