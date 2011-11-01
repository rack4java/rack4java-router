package test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

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
		call();
		assertEquals(404, response.getStatus());
	}

	public void call() throws Exception {
		response = router.call(env);
	}
}
