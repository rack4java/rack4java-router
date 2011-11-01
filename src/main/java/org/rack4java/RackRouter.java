package org.rack4java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

	public void addPathPrefixRoute(String prefix, Rack handler) {
		addRoute(new PathRoute(handler, prefix));
	}

	public void addPathPatternRoute(Pattern pattern, Rack handler) {
		addRoute(new PathRoute(handler, pattern));
	}

	public void addCatchAll(Rack handler) {
		addRoute(new CatchAllRoute(handler));
	}

}
