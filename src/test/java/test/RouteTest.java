package test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.Route;
import org.rack4java.route.CatchAllRoute;
import org.rack4java.route.PathPatternRoute;
import org.rack4java.route.PathPrefixRoute;

public class RouteTest extends TestCase {
	Map<String, Object> env;
	Route route;
	RackResponse response;
	RackStub ok;
	
	public void setUp() {
		env = new HashMap<String, Object>();
		ok = new RackStub("OK");
	}
	
	public void testCatchAll() throws Exception {
		route = new CatchAllRoute(ok);
		
		// no path specified
		assertMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertMatch();
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
	}
	
	public void testRootPrefixPathWithoutRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/", false);
		
		// no path specified
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertMatch();
		assertEquals("/", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testRootPrefixPathWithRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/", false);
		
		// no path specified
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertMatch();
		assertEquals("/", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testNonRootPrefixPathWithoutRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/lala/", false);
		
		// no path specified
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertNoMatch();
	}
	
	public void testNonRootPrefixPathWithRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/lala/", true);
		
		// no path specified
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/thing", ok.getRecordedValue(Rack.PATH_INFO));
		assertEquals("/lala/thing", ok.getRecordedValue(PathPrefixRoute.ORIGINAL_PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertNoMatch();
	}
	
	public void testPatternPath() throws Exception {
		route = new PathPatternRoute(ok, Pattern.compile(".*/thing.*"));
		
		// no path specified
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.put(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.put(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}

	public void assertNoMatch() throws Exception {
		assertFalse(route.match(env));
	}

	public void assertMatch() throws Exception {
		assertTrue(route.match(env));
		ok.reset();
		route.call(env);
	}
}
