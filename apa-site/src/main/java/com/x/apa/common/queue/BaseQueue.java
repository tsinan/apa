package com.x.apa.common.queue;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 队列超类
 * 
 * @author liumeng
 */
public abstract class BaseQueue<T>{

	private static final int QUEUE_MAX_SIZE = 1000;

	private LinkedBlockingDeque<T> queue = new LinkedBlockingDeque<>(QUEUE_MAX_SIZE);

	public boolean put(T o) {
		try {
			queue.put(o);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	public T take() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}
}
