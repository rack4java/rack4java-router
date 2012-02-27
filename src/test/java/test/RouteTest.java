package test;

import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackResponse;
import org.rack4java.Route;
import org.rack4java.context.MapContext;
import org.rack4java.route.CatchAllRoute;
import org.rack4java.route.HeaderPatternRoute;
import org.rack4java.route.PathPatternRoute;
import org.rack4java.route.PathPrefixRoute;

public class RouteTest extends TestCase {
	Context<Object> env;
	Route route;
	RackResponse response;
	RackStub ok;
	
	public void setUp() {
		env = new MapContext<Object>();
		ok = new RackStub("OK");
	}
	
	public void testCatchAll() throws Exception {
		route = new CatchAllRoute(ok);
		
		// no path specified
		assertMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertMatch();
		assertEquals("/", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testRootPrefixPathWithoutRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/", false);
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertMatch();
		assertEquals("/", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testRootPrefixPathWithRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/", false);
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertMatch();
		assertEquals("/", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testNonRootPrefixPathWithoutRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/lala/", false);
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertNoMatch();
	}
	
	public void testNonRootPrefixPathWithRemove() throws Exception {
		route = new PathPrefixRoute(ok, "/lala/", true);
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/thing", ok.getRecordedValue(Rack.PATH_INFO));
		assertEquals("/lala/thing", ok.getRecordedValue(PathPrefixRoute.ORIGINAL_PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertNoMatch();
	}
	
	public void testPatternPathWithoutReplace() throws Exception {
		route = new PathPatternRoute(ok, Pattern.compile(".*/thing.*"));
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala/thing", ok.getRecordedValue(Rack.PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/thing/lala", ok.getRecordedValue(Rack.PATH_INFO));
	}
	
	public void testPatternPathWithReplace() throws Exception {
		route = new PathPatternRoute(ok, Pattern.compile("(.*)/thing(.*)"), "$1$2");
		
		// no path specified
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/");
		assertNoMatch();
		
		env.with(Rack.PATH_INFO, "/lala/thing");
		assertMatch();
		assertEquals("/lala", ok.getRecordedValue(Rack.PATH_INFO));
		assertEquals("/lala/thing", ok.getRecordedValue(PathPrefixRoute.ORIGINAL_PATH_INFO));
		
		env.with(Rack.PATH_INFO, "/thing/lala");
		assertMatch();
		assertEquals("/lala", ok.getRecordedValue(Rack.PATH_INFO));
		assertEquals("/thing/lala", ok.getRecordedValue(PathPrefixRoute.ORIGINAL_PATH_INFO));
	}
	
	public void testPatternHeaderWithoutReplace() throws Exception {
		route = new HeaderPatternRoute(ok, "X-Mode", Pattern.compile("[0-9]*"));
		
		// no headers specified
		assertNoMatch();
		
		env.with(Rack.HTTP_ + "lala", "12");
		assertNoMatch();
		
		env.with(Rack.HTTP_ + "X-Mode", "abc");
		assertNoMatch();
		
		env.with(Rack.HTTP_ + "X-Mode", "12");
		assertMatch();
		assertEquals("12", ok.getRecordedValue(Rack.HTTP_ + "X-Mode"));
	}

	public void assertNoMatch() throws Exception {
		assertNull(route.match(env));
	}

	public void assertMatch() throws Exception {
		Rack target = route.match(env);
		assertNotNull(target);
		ok.reset();
		target.call(env);
	}
}
