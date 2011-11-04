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
	RackStub ok;
	RackStub catcher;
	
	public void setUp() {
		env = new HashMap<String, Object>();
		router = new RackRouter();
		ok = new RackStub("OK");
		catcher = new RackStub("huh?");
	}
	
	public void testNoRouting() throws Exception {
		check(404, "No Matching Route");
	}
	
	public void testSingleCatchAll() throws Exception {
		router.addCatchAll(ok);
		
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
		router.addRoute(new PathPrefixRoute(ok, "/", false));
		
		// no path specified
		check(404, "No Matching Route");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testSinglePatternPathWithoutRemove() throws Exception {
		router.addRoute(new PathPatternRoute(ok, Pattern.compile(".*/thing.*")));
		
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
		router.addRoute(new PathPrefixRoute(ok, "/lala/", false));
		router.addCatchAll(catcher);
		
		// no path specified
		check(200, "Stub huh?");
		assertFalse(ok.wasCalled());
		assertEnvValue(catcher, Rack.PATH_INFO, null);
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub huh?");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub huh?");
	}

	private void assertEnvValue(RackStub stub, String key, Object expected) {
		assertNotNull(stub);
		Object actual = stub.getRecordedValue(key);
		assertEquals(expected, actual);
	}

	public void check(int expectedStatus, String expectedMessage) throws Exception, IOException {
		response = router.call(env);
		assertEquals(expectedStatus, response.getStatus());
		assertEquals(expectedMessage, response.getString());
	}
}
