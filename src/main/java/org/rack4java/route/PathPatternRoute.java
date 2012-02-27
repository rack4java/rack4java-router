package org.rack4java.route;

import java.util.regex.Pattern;

import org.rack4java.Context;
import org.rack4java.Rack;

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
	
	@Override protected Context<Object> adjust(Context<Object> env) {
		if (null != replacement) return push(env)
				.with(PATH_INFO, pattern.matcher((String)env.get(PATH_INFO)).replaceAll(replacement));
	
		return env;
	}
}
