package com.cgi.reader;

public interface Reader<T> {
	T get(Class<T> clazz);
}
