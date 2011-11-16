package org.rack4java;


/** 
 * the interface shared by all route definitions
 * 
 * @return null if the environment is not a match for this route
 * @return a Rack object (on which the 'call' method will be invoked) if it is a match
 *   
 * Notes:
 * * the match method should not modify the environment
 * * a simple Route might also implement Rack and return itself from 'match'
 * * a factory-style Route might find or create a Rack instance and return that instead 
 *
 */
public interface Route {
	Rack match(Context<Object> environment);
}
