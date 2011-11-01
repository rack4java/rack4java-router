package test;

import java.util.Map;

import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class RackStub implements Rack {

	@Override public RackResponse call(Map<String, Object> environment) throws Exception {
		return new RackResponse(200, "Stub OK");
	}

}
