package com.x.apa.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;

/**
 * Helper for JSON serialization and deserialization
 */
public class JsonHelper {

	/**
	 * Jackson Object Mapper used to serialization/deserialization
	 */
	private static ObjectMapper objectMapper;

	private static void initialize() {
		objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		objectMapper.registerModule(module);
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		objectMapper.setAnnotationIntrospector(introspector);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	}

	/**
	 * Return the ObjectMapper. It can be used to customize
	 * serialization/deserialization configuration.
	 * 
	 * @return
	 */
	public ObjectMapper getObjectMapper() {
		if (objectMapper == null)
			initialize();
		return objectMapper;
	}

	/**
	 * Serialize and object to a JSON String representation
	 * 
	 * @param o
	 *            The object to serialize
	 * @return The JSON String representation
	 */
	public static String serialize(Object o) {
		if (objectMapper == null)
			initialize();
		Writer writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, o);
		} catch (Exception e) {
			throw new SerializationException(e);
		}
		return writer.toString();
	}

	/**
	 * 将Json数据直接输出到Writer中，减少String的内存占用情况
	 * 
	 * @param o
	 * @param writer
	 */
	public static void serialize2Writer(Object o, Writer writer) {
		if (objectMapper == null)
			initialize();
		try {
			objectMapper.writeValue(writer, o);
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	/**
	 * Serialize and object to a JSON String representation with a Jackson view
	 * 
	 * @param o
	 *            The object to serialize
	 * @param view
	 *            The Jackson view to use
	 * @return The JSON String representation
	 */
	public static String serialize(Object o, Class<?> view) {
		if (objectMapper == null)
			initialize();
		Writer w = new StringWriter();
		try {
			ObjectWriter writter = objectMapper.writerWithView(view);
			writter.writeValue(w, o);
		} catch (Exception e) {
			throw new SerializationException(e);
		}
		return w.toString();
	}

	/**
	 * Deserialize a JSON string
	 * 
	 * @param content
	 *            The JSON String object representation
	 * @param type
	 *            The type of the deserialized object instance
	 * @return The deserialized object instance
	 */
	public static <T> T deserialize(String content, Class<T> type) {
		if (objectMapper == null)
			initialize();
		try {
			return objectMapper.readValue(content, type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static <T> T deserialize(InputStream inputStream, Class<T> type) {
		if (objectMapper == null)
			initialize();
		try {
			return objectMapper.readValue(inputStream, type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deserialize a JSON string
	 * 
	 * @param content
	 *            The JSON String object representation
	 * @param valueTypeRef
	 *            The typeReference containing the type of the deserialized
	 *            object instance
	 * @return The deserialized object instance
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T deserialize(String content, TypeReference valueTypeRef) {
		if (objectMapper == null)
			initialize();
		try {
			return objectMapper.readValue(content, valueTypeRef);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static <T> T deserialize(InputStream inputStream, TypeReference valueTypeRef) {
		if (objectMapper == null)
			initialize();
		try {
			return objectMapper.readValue(inputStream, valueTypeRef);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class SerializationException extends RuntimeException {

		private static final long serialVersionUID = 4706910279516572003L;

		public SerializationException(Throwable cause) {
			super(cause);
		}
	}
}
