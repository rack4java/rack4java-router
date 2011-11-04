package org.rack4java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rack4java.route.CatchAllRoute;

public class RackRouter implements Route, Rack {
	private List<Route> routes;
	
	public RackRouter() {
		this.routes = new ArrayList<Route>();
	}

	@Override public Rack match(Map<String, Object> environment) {
		for (Route route : routes) {
			Rack target = route.match(environment);
			if (null != target) return target;
		}
		return null;
	}

	@Override public RackResponse call(Map<String, Object> environment) throws Exception {
		Rack target = match(environment);
		return null != target 
			? target.call(environment)
			: new RackResponse(404, "No Matching Route");
	}
	
	public void addRoute(Route route) {
		routes.add(route); 
	}

	public void addCatchAll(Rack handler) {
		addRoute(new CatchAllRoute(handler));
	}

}
