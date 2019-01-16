package core;

import java.util.Queue;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Runtime implements AutoCloseable, Runnable {

    private final String name;
    // we need some type of MainActor here
    private final BlockingEvent isRunning;
    private final Queue<Scheduler> idleSchedulers;
    protected final CyclicIterator<Scheduler> schedulers;
    // asio threads?

    public Runtime() {
        this(null);
    }

    public Runtime(String name) {
    		this.name = name;
    		isRunning = new BlockingEvent();
        schedulers = new CyclicIterator<>();
        idleSchedulers = new ConcurrentLinkedQueue<>();
    }
    
    protected void addIdleScheduler(final Scheduler scheduler) {
    		idleSchedulers.add(scheduler);
    }
    
    public void submit(final Actor actor) {
    		Scheduler scheduler = idleSchedulers.poll();
    		if (scheduler == null)
    			scheduler = schedulers.get();
    		scheduler.submit(actor);
    }
    
    public void start(final String[] args, int numSchedulers) {
    		while (numSchedulers-- != 0) addScheduler();
    		
    }
    
    public final Scheduler addScheduler() {
    		final Scheduler thread = new Scheduler(this, schedulers.size());
    		schedulers.add(thread);
    		if (isRunning.isSet())
    			thread.start();
    		return thread;
    		
    }
    
    
    @Override
    public void run() {
    		for (Scheduler scheduler : schedulers) {
    			scheduler.start();
    		}
    }
    

}