package com.cgi;

import com.cgi.annotations.FileColumn;
import com.cgi.annotations.FileReaderConfiguration;


@FileReaderConfiguration(filePath =  "file.properties")
public class Employee {

	@FileColumn(columnName = "id")
	private int id;
	@FileColumn(columnName = "name")
	private String name;
	@FileColumn(columnName = "address")
	private String address;
	@FileColumn(columnName = "dept")
	private String dept;
	@FileColumn(columnName = "salary")
	private int salary;
	@FileColumn(columnName = "gender")
	private String gender;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", address=" + address + ", dept=" + dept + ", salary="
				+ salary + ", gender=" + gender + "]";
	}

	
}
