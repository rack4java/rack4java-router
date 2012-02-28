package org.rack4java.route;

import java.util.regex.Pattern;

import org.rack4java.Context;
import org.rack4java.Rack;

public class HeaderPatternRoute extends AbstractRoute {
	
	private String headerName;
	private Pattern pattern;

	public HeaderPatternRoute(Rack handler, String headerName, Pattern pattern) {
		super(handler);
		this.headerName = headerName;
		this.pattern = pattern;
	}

	@Override public Rack match(Context<String> environment) {
		String header = environment.get(HTTP_ + headerName);
		if (null == header) return null;
		return pattern.matcher(header).matches() ? this : null;
	}

	@Override protected Context<String> adjust(Context<String> env) {
		return env;
	}

}
