package test;

import org.rack4java.Context;
import org.rack4java.Rack;
import org.rack4java.RackResponse;

public class RackStub implements Rack {

	private final String message;
	private Context<String> recorded;

	public RackStub(String message) {
		this.message = message;
	}

	@Override public Context<String> call(Context<String> environment) throws Exception {
		recorded = environment;
		return new RackResponse(200).withBody("Stub " + message);
	}

	public Object getRecordedValue(String key) {
		return recorded.get(key);
	}

	public boolean wasCalled() {
		return null != recorded;
	}

	public void reset() {
		recorded = null;
	}
}
