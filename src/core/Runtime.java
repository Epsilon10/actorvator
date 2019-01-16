package core;

import java.util.Queue;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Runtime implements AutoCloseable, Runnable {

    private final String name;
    // we need some type of MainActor here
    private final Queue<Scheduler> idleSchedulers;
    protected final CyclicIterator<Scheduler> schedulers;
    // asio threads?

    public Runtime() {
        this(null);
    }

    public Runtime(String name) {
        schedulers = new CyclicIterator<>();
        idleSchedulers = new CyclicIterator<>();
    }

    

}