package test;

import java.io.IOException;

import junit.framework.TestCase;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackBody;
import org.rack4java.RackRouter;
import org.rack4java.context.MapContext;
import org.rack4java.route.PathPrefixRoute;

public class RoutingTest extends TestCase {
	Context<String> env;
	RackRouter router;
	Context<String> response;
	RackStub ok;
	RackStub catcher;
	
	public void setUp() {
		env = new MapContext<String>();
		router = new RackRouter();
		ok = new RackStub("OK");
		catcher = new RackStub("huh?");
	}
	
	private RackBody getBody(Context<String> response) {
		RackBody body = (RackBody) response.getObject(Rack.MESSAGE_BODY);
		return body;
	}

	private String getBodyAsString(Context<String> response) {
		StringBuilder ret = new StringBuilder();
		for (byte[] chunk : getBody(response).getBodyAsBytes()) {
			ret.append(new String(chunk));
		}
		return ret.toString();
	}
	
	public void testNoRouting() throws Exception {
		check(404, "No Matching Route");
		
		env.with(Rack.PATH_INFO, "/");
		check(404, "No Matching Route");
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		check(404, "No Matching Route");
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		check(404, "No Matching Route");
	}
	
	public void testSingleCatchAll() throws Exception {
		router.setDefaultHandler(ok);
		
		// no path specified
		check(200, "Stub OK");
		
		env.with(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testSingleRoute() throws Exception {
		router.addRoute(new PathPrefixRoute(ok, "/", false));
		
		// no path specified
		check(404, "No Matching Route");
		
		env.with(Rack.PATH_INFO, "/");
		check(200, "Stub OK");
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub OK");
	}
	
	public void testRouteFallback() throws Exception {
		router.addRoute(new PathPrefixRoute(ok, "/lala/", false));
		router.setDefaultHandler(catcher);
		
		// no path specified
		check(200, "Stub huh?");
		assertFalse(ok.wasCalled());
		assertEquals(null, catcher.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/");
		check(200, "Stub huh?");
		assertFalse(ok.wasCalled());
		assertEquals("/", catcher.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		assertFalse(catcher.wasCalled());
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub huh?");
	}
	
	public void testCatchAllFloatsToEnd() throws Exception {
		router.setDefaultHandler(catcher);
		router.addRoute(new PathPrefixRoute(ok, "/lala/", false));
		
		// no path specified
		check(200, "Stub huh?");
		assertFalse(ok.wasCalled());
		assertEquals(null, catcher.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/");
		check(200, "Stub huh?");
		assertFalse(ok.wasCalled());
		assertEquals("/", catcher.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		check(200, "Stub OK");
		assertFalse(catcher.wasCalled());
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		check(200, "Stub huh?");
	}

	public void check(int expectedStatus, String expectedMessage) throws Exception, IOException {
		ok.reset();
		catcher.reset();
		response = router.call(env);
		assertEquals(expectedStatus, response.get(Rack.MESSAGE_STATUS));
		assertEquals(expectedMessage, getBodyAsString(response));
	}
}
