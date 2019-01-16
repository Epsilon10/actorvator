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
    
    private final Runtime runtime;
    private volatile int isActive;
    protected final Queue<Message> mailBox;
    
    public Actor(Runtime runtime) {
    		this.isActive = 0;
    		this.runtime = runtime;
    		this.mailBox = new ConcurrentLinkedQueue<>();
    }
    
    public void setInactive() {
    		this.isActive = 0;
    }
    
    public void send(Consumer<Object[]> method, Object... args) {
    		mailBox.add(new Message(method, args));
    		
    }
}

    
