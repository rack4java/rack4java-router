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
	
	@SuppressWarnings("unchecked") protected Context<Object> push(Context<Object> env) {
		return new FallbackContext<Object>(new MapContext<Object>(), env)
			.with(ORIGINAL_SCRIPT_NAME, (String) env.get(Rack.SCRIPT_NAME))
			.with(ORIGINAL_PATH_INFO, (String) env.get(Rack.PATH_INFO))
			.with(ORIGINAL_QUERY_STRING, (String) env.get(Rack.QUERY_STRING));
	}
	
	protected String getFullPath(Context<Object> env) {
		return (String)env.get(SCRIPT_NAME) + (String)env.get(PATH_INFO);
	}
	
	protected String getFullResource(Context<Object> env) {
		String path = getFullPath(env);
		String query = (String)env.get(QUERY_STRING);
		if (null != query && query.length() > 0) path += "?" + query;
		return path;
	}
}
