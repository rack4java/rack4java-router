package org.rack4java.route;

import org.rack4java.Rack;

public abstract class AbstractPathRoute extends AbstractRoute {
	public static final String ORIGINAL_PATH_INFO = "ORIGINAL_" + Rack.PATH_INFO;

	public AbstractPathRoute(Rack handler) {
		super(handler);
	}
}
