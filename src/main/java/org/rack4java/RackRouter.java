package org.rack4java;

import java.util.Map;

public class RackRouter implements Rack {

	@Override public RackResponse call(Map<String, Object> arg0) throws Exception {
		return new RackResponse(404, "No Routes");
	}

}
