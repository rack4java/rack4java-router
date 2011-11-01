package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.RackRouter;
import org.rack4java.route.PathPatternRoute;
import org.rack4java.route.PathPrefixRoute;

public class RoutingTest extends TestCase {
	Map<String, Object> env;
	RackRouter router;
	RackResponse response;
	
	public void setUp() {
		env = new HashMap<String, Object>();
		router = new RackRouter();
	}
	
	public void testNoRouting() throws Exception {
		check(404, "No Matching Route");
	}
	
	public void testSingleCatchAll() throws Exception {
		router.addCatchAll(new RackStub("OK"));
		
		// no path specified
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testSingleRootPrefixPath() throws Exception {
		router.addRoute(new PathPrefixRoute(new RackStub("OK"), "/"));
		
		// no path specified
		check(404, "No Matching Route");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testSinglePatternPath() throws Exception {
		router.addRoute(new PathPatternRoute(new RackStub("OK"), Pattern.compile(".*/thing.*")));
		
		// no path specified
		check(404, "No Matching Route");
		
		env.put(Rack.PATH_INFO, "/");
		check(404, "No Matching Route");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testPathFallback() throws Exception {
		router.addRoute(new PathPrefixRoute(new RackStub("OK"), "/lala/"));
		router.addCatchAll(new RackStub("huh?"));
		
		// no path specified
		check(200, "Stub huh?");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub huh?");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub huh?");
	}

	public void check(int expectedStatus, String expectedMessage) throws Exception, IOException {
		response = router.call(env);
		assertEquals(expectedStatus, response.getStatus());
		assertEquals(expectedMessage, response.getString());
	}
}
