package core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler extends AutoCloseableThread {
	protected final int id;
	private final Runtime runtime;
	private volatile boolean isRunning;
	private final Queue<Actor> rtQueue;
	
	public Scheduler(Runtime runtime, int id) {
		super("." + id);
		this.id = id;
		this.isRunning = false;
		this.runtime = runtime;
		this.rtQueue = new ConcurrentLinkedQueue<>();
	}
	
	protected void submit(final Actor actor) {
		rtQueue.add(actor);
		// set to active
	}
	
	private final Actor getActor() {
		Actor actor = rtQueue.poll();
		if (actor != null) return actor;
		for (Scheduler s : runtime.schedulers) {
			if ((actor = s.rtQueue.poll()) != null)
				return actor;
		
		}
		return rtQueue.poll();
	}
	
	@Override
	public void close() throws InterruptedException {
		
	}
	
	@Override
	public void run() {
		isRunning = true;
		while(isRunning) {
			final Actor actor = getActor();
			if (actor != null) {
				final Actor.Message message = actor.mailBox.poll();
				if (message == null)
					actor.setInactive();
				else {
					message.run();
					runtime.submit(actor);
				}
			}
		}
	}
	
	
}
