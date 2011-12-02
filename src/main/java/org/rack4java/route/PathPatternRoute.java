package org.rack4java.route;

import java.util.regex.Pattern;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class PathPatternRoute extends AbstractPathRoute {
	private Pattern pattern;
	private String replacement;
	
	public PathPatternRoute(Rack handler, Pattern pattern, String replacement) {
		super(handler);
		this.pattern = pattern;
		this.replacement = replacement;
	}
	
	public PathPatternRoute(Rack handler, Pattern pattern) {
		this(handler, pattern, null);
	}

	@Override public Rack match(Context<Object> env) {
		String path = (String) env.get(Rack.PATH_INFO);
		if (null == path || null == pattern) return null;
		return pattern.matcher(path).matches() ? this : null;
	}
	
	@Override public RackResponse call(Context<Object> env)throws Exception {
		if (null != replacement) {
			String path = (String) env.get(Rack.PATH_INFO);
			env.with(ORIGINAL_PATH_INFO, path);
			String tail = pattern.matcher(path).replaceAll(replacement);
			env.with(PATH_INFO, tail);
		}
		
		return super.call(env);
	}
}
