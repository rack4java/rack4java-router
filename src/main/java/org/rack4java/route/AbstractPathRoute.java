package org.rack4java.route;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.context.FallbackContext;
import org.rack4java.context.MapContext;

public abstract class AbstractPathRoute extends AbstractRoute {
	protected static final String ORIGINAL_ = "ORIGINAL_";
	public static final String ORIGINAL_SCRIPT_NAME = ORIGINAL_ + Rack.SCRIPT_NAME;
	public static final String ORIGINAL_PATH_INFO = ORIGINAL_ + Rack.PATH_INFO;
	public static final String ORIGINAL_QUERY_STRING = ORIGINAL_ + Rack.QUERY_STRING;

	public AbstractPathRoute(Rack handler) {
		super(handler);
	}
	
	@SuppressWarnings("unchecked") protected Context<String> push(Context<String> env) {
		return new FallbackContext<String>(new MapContext<String>(), env)
			.with(ORIGINAL_SCRIPT_NAME, env.get(Rack.SCRIPT_NAME))
			.with(ORIGINAL_PATH_INFO, env.get(Rack.PATH_INFO))
			.with(ORIGINAL_QUERY_STRING, env.get(Rack.QUERY_STRING));
	}
	
	protected String getFullPath(Context<String> env) {
		return env.get(SCRIPT_NAME) + env.get(PATH_INFO);
	}
	
	protected String getFullResource(Context<String> env) {
		String path = getFullPath(env);
		String query = env.get(QUERY_STRING);
		if (null != query && query.length() > 0) path += "?" + query;
		return path;
	}
}
