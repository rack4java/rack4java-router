package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.RackRouter;

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
	
	public void testCatchAll() throws Exception {
		router.addCatchAll(new RackStub("OK"));
		
		// no path specified
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
	}
	
	public void testRootPath() throws Exception {
		router.addPathRoute("/", new RackStub("OK"));
		
		// no path specified
		check(404, "No Matching Route");
		
		env.put(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
	}

	public void check(int expectedStatus, String expectedMessage) throws Exception, IOException {
		response = router.call(env);
		assertEquals(expectedStatus, response.getStatus());
		assertEquals(expectedMessage, response.getString());
	}
}
