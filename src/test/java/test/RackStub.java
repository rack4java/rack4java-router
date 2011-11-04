package test;

import java.util.HashMap;
import java.util.Map;

import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class RackStub implements Rack {

	private String message;
	private Map<String, Object> recorded;

	public RackStub(String message) {
		this.message = message;
	}

	@Override public RackResponse call(Map<String, Object> environment) throws Exception {
		recorded = new HashMap<String, Object>();
		recorded.putAll(environment);
		return new RackResponse(200, "Stub " + message);
	}

	public Object getRecordedValue(String key) {
		return recorded.get(key);
	}

	public boolean wasCalled() {
		return null != recorded;
	}

}
