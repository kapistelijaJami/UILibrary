package uilibrary.thread;

import java.util.function.Consumer;

public class ThreadExecutor extends Thread {
	private Consumer<Object> method;
	
	public ThreadExecutor(Consumer<Object> method) {
		this.method = method;
	}
	
	@Override
	public void run() {
		method.accept(null);
	}
}
