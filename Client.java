package com.cgi;

import com.cgi.reader.PropertiesReader;
import com.cgi.reader.Reader;

public class Client {
	
	public static void main(String[] args) {
		Reader<Employee> reader = new PropertiesReader<Employee>();
		Employee emp= reader.get(Employee.class);
		System.out.println(emp);
	}

}
