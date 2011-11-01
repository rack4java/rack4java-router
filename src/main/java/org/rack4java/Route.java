package org.rack4java;

import java.util.Map;

public interface Route extends Rack {
	boolean match(Map<String, Object> env);
}
