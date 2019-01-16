package core;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Actor {
    protected static final class Message implements Runnable {
        private final Object[] args;
        private final Consumer<Object[]> method;

        public Message(final Consumer<Object[]> method, final Object[] args) {
        		this.args = args;
        		this.method = method;
        }

        @Override
        public void run() {
            method.accept(args);
        }
    }
}

    
