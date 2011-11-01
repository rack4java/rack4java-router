package org.rack4java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rack4java.route.CatchAllRoute;

public class RackRouter implements Rack {
	private List<Route> routes;
	
	public RackRouter() {
		this.routes = new ArrayList<Route>();
	}

	@Override public RackResponse call(Map<String, Object> env) throws Exception {
		for (Route route : routes) {
			if (route.match(env)) return route.call(env);
		}
		return new RackResponse(404, "No Matching Route");
	}
	
	public void addRoute(Route route) {
		routes.add(route); 
	}

	public void addCatchAll(Rack handler) {
		addRoute(new CatchAllRoute(handler));
	}

}
