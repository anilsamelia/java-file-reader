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

	@Override
	public T get(Class<T> clazz) {

		boolean isFileReader = clazz.isAnnotationPresent(FileReaderConfiguration.class);
		if (isFileReader) {
			FileReaderConfiguration filereader = clazz.getAnnotation(FileReaderConfiguration.class);
			if (filereader.filePath() == null || filereader.filePath().isEmpty()) {
				throw new RuntimeException("File path is empty or null");

			} else {
				validation(clazz, filereader.filePath());
				T t = loadFile(clazz, filereader.filePath());
				return t;
			}

		} else {
			throw new RuntimeException(clazz.getName() + " Is not FileReader Configuration ");
		}
	}

	private void validation(Class<T> clazz, String filePath) {
		Field[] fields = clazz.getDeclaredFields();
		int nonAnnotatedFieldCount = Stream.of(fields).filter(field -> !field.isAnnotationPresent(FileColumn.class))
				.filter(field -> !field.isAnnotationPresent(Transient.class)).toList().size();
		if (nonAnnotatedFieldCount > 0) {
			throw new RuntimeException("Field Should be annotated with @FileColumn or @Transient");
		}

	}

	private T loadFile(Class<T> clazz, String filePath) {
		Properties prop = new Properties();
		Field[] fields = clazz.getDeclaredFields();

		try {
			FileInputStream input = new FileInputStream(filePath);
			prop.load(input);
			T configObject = clazz.getDeclaredConstructor().newInstance();
			for (Field field : fields) {
				FileColumn fileColumn = field.getAnnotation(FileColumn.class);
				field.setAccessible(true);
				String value = (String) prop.get(fileColumn.columnName());
				field.set(configObject, value);
			}
			return configObject;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
