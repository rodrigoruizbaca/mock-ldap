package com.siriusxm.idm.mockLdap;

import java.util.PriorityQueue;
import java.util.Queue;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class MockNamingEnumeration <T> implements NamingEnumeration<T> {

	private Queue<T> data;
	
	public MockNamingEnumeration(Queue<T> data) {
		this.data = data;
	}
	
	public MockNamingEnumeration(T obj) {
		addObject(obj);
	}
	
	public void addObject(T obj) {
		if (data == null) {
			data = new PriorityQueue<T>();
		}
		data.add(obj);
	}

	public boolean hasMoreElements() {
		return !data.isEmpty();
	}

	public T nextElement() {
		return data.poll();
	}

	public void close() throws NamingException {
	}

	public boolean hasMore() throws NamingException {
		return !data.isEmpty();
	}

	public T next() throws NamingException {
		return data.poll();
	}
	
	

}
