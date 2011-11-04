package test;

import java.util.Map;
import java.util.regex.Pattern;

import org.rack4java.Rack;
import org.rack4java.route.AbstractRoute;

public class HeaderPatternRoute extends AbstractRoute {
	
	private String headerName;
	private Pattern pattern;

	public HeaderPatternRoute(Rack handler, String headerName, Pattern pattern) {
		super(handler);
		this.headerName = headerName;
		this.pattern = pattern;
	}

	@Override public Rack match(Map<String, Object> environment) {
		String header = (String) environment.get(HTTP_ + headerName);
		if (null == header) return null;
		return pattern.matcher(header).matches() ? this : null;
	}

}
