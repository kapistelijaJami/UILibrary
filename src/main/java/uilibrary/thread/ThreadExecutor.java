package uilibrary.thread;

/**
 * Create Runnable by
 * <ul>
 * <li>Lambda expression:
 * <ul>
 * <li>One-liner: <code>() -> callAMethod(var1);</code></li>
 * <p>
 * <li>In block form:</li>
 * </ul>
 * <pre>
 * {@code
 *	Runnable method = () -> {
 *		this.callAMethod(var1);
 *		//maybe do something else here as well
 *	};
 * }</pre>
 * </li>
 * 
 * <li>Reference a method that has the same input (void) and return value (void).
 * 
 * <ul>
 *	<li>You can reference it statically with <code>ClassName::methodName</code></li>
 *	<li>and non-statically with <code>this::methodName</code> or <code>objectVariable::methodName</code></li>
 * </ul>
 * <p>
 * Like this:
 * <code>new ThreadExecutor(this::doSomething).start();</code>
 * </ul>
 */
public class ThreadExecutor extends Thread {
	private final Runnable method;
	
	public ThreadExecutor(Runnable method) {
		this.method = method;
	}
	
	@Override
	public void run() {
		method.run();
	}
}
