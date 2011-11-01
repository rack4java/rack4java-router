package test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.rack4java.CatchAllRoute;
import org.rack4java.PathPatternRoute;
import org.rack4java.PathPrefixRoute;
import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.Route;

public class RouteTest extends TestCase {
	Map<String, Object> env;
	Route route;
	RackResponse response;
	
	public void setUp() {
		env = new HashMap<String, Object>();
	}
	
	public void testCatchAll() throws Exception {
		route = new CatchAllRoute(new RackStub("OK"));
		
		// no path specified
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertTrue(route.match(env));
	}
	
	public void testRootPrefixPath() throws Exception {
		route = new PathPrefixRoute(new RackStub("OK"), "/");
		
		// no path specified
		assertFalse(route.match(env));
		
		env.put(Rack.PATH_INFO, "/");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertTrue(route.match(env));
	}
	
	public void testNonRootPrefixPath() throws Exception {
		route = new PathPrefixRoute(new RackStub("OK"), "/lala/");
		
		// no path specified
		assertFalse(route.match(env));
		
		env.put(Rack.PATH_INFO, "/");
		assertFalse(route.match(env));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertFalse(route.match(env));
	}
	
	public void testPatternPath() throws Exception {
		route = new PathPatternRoute(new RackStub("OK"), Pattern.compile(".*/thing.*"));
		
		// no path specified
		assertFalse(route.match(env));
		
		env.put(Rack.PATH_INFO, "/");
		assertFalse(route.match(env));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertTrue(route.match(env));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertTrue(route.match(env));
	}
}
