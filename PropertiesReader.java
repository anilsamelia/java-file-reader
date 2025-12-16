package com.cgi.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.stream.Stream;

import com.cgi.annotations.FileColumn;
import com.cgi.annotations.FileReaderConfiguration;
import com.cgi.annotations.Transient;

public class PropertiesReader<T> implements Reader<T> {
	private String filePath = null;

	public PropertiesReader(String filePath) {
		this.filePath = filePath;
	}

	public PropertiesReader() {
		//
	}

	@Override
	public T get(Class<T> clazz) {

		boolean isFileReader = clazz.isAnnotationPresent(FileReaderConfiguration.class);
		if (isFileReader) {
			FileReaderConfiguration filereader = clazz.getAnnotation(FileReaderConfiguration.class);
			if (filePath == null) {
				this.filePath = filereader.filePath();
			}
			if (filePath == null || filePath.isEmpty()) {
				throw new RuntimeException("File path is empty or null");

			} else {
				validation(clazz, filePath);
				T t = loadFile(clazz, filePath);
				return t;
			}

		} else {
			throw new RuntimeException(clazz.getName() + " Is not FileReader Configuration ");
		}
	}

	private void validation(Class<T> clazz, String filePath) {
		Field[] fields = clazz.getDeclaredFields();
		int nonAnnotatedFieldCount = Stream.of(fields).filter(
				field -> !field.isAnnotationPresent(FileColumn.class) && !field.isAnnotationPresent(Transient.class))
				.toList().size();
		if (nonAnnotatedFieldCount > 0) {
			throw new RuntimeException("Field Should be annotated with @FileColumn or @Transient");
		}

	}

	private T loadFile(Class<T> clazz, String filePath) {
		Properties prop = new Properties();
		try {
			FileInputStream input = new FileInputStream(filePath);
			prop.load(input);
			T configObject = clazz.getDeclaredConstructor().newInstance();
			List<Field> fieldList = Stream.of(clazz.getDeclaredFields())
					.filter(field -> field.isAnnotationPresent(FileColumn.class)).toList();
			if (fieldList.stream().filter(field -> field.getType().isPrimitive()).findAny().isPresent()) {
				throw new RuntimeException("Primitive values are not allowed. Please use wrapper types instead");
			}
			for (Field field : fieldList) {
				FileColumn fileColumn = field.getAnnotation(FileColumn.class);
				field.setAccessible(true);
				String value = (String) prop.get(fileColumn.columnName());
				// field.set(configObject, value);
				setValueInField(configObject, field, value);
			}
			return configObject;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setValueInField(T object, Field field, String value)
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Class clazz = field.getType();
		if (clazz == Integer.class) {
			field.set(object, Integer.valueOf(value));
		} else if (clazz == Float.class) {
			field.set(object, Float.valueOf(value));
		} else if (clazz == Double.class) {
			field.set(object, Double.valueOf(value));
		} else if (clazz == Long.class) {
			field.set(object, Long.valueOf(value));
		} else {
			field.set(object, value);
		}
	}

}
