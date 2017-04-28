package com.itar.soa.biwan.support.jaxrsFastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * JAX-RS Provider for fastjson.
 *
 * fastjson JAX-RS实现
 * 
 * @author smallnest
 *
 */
@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	private boolean annotated = false;
	private String[] scanpackages = null;
	private Class<?>[] clazzes = null;

	protected boolean pretty;

	protected FastJsonConfig fastJsonConfig = new FastJsonConfig(new SerializeConfig(), null, null, new ParserConfig(), null);

	/**
	 * Can serialize/deserialize all types.
	 */
	public FastJsonProvider() {

	}

	/**
	 * Only serialize/deserialize all types annotated with {@link com.itar.soa.biwan.support.jaxrsFastjson.FastJsonType}.
	 */
	public FastJsonProvider(boolean annotated) {
		this.annotated = annotated;
	}
	
	/**
	 * Only serialize/deserialize all types in scanpackages.
	 */
	public FastJsonProvider(String[] scanpackages) {
		this.scanpackages = scanpackages;
	}

	/**
	 * Only serialize/deserialize all types in scanpackages.
	 */
	public FastJsonProvider(String[] scanpackages, boolean annotated) {
		this.scanpackages = scanpackages;
		this.annotated = annotated;	
	}
	
	/**
	 * Only serialize/deserialize all types in clazzes.
	 */
	public FastJsonProvider(Class<?>[] clazzes) {
		this.clazzes = clazzes;
	}

	/**
	 * Init this provider with more fastjson configurations.
	 * @param fastJsonConfig fastjson config
	 */
	public FastJsonProvider init(FastJsonConfig fastJsonConfig) {
		this.fastJsonConfig = fastJsonConfig;
		return this;
	}


	// Set pretty format
	public FastJsonProvider setPretty(boolean p) {
		this.pretty = p;
		return this;
	}
	
	/**
	 * Check whether a class can be serialized or deserialized. It can check
	 * based on packages, annotations on entities or explicit classes.
	 * 
	 * @param type class need to check
	 * @return true if valid
	 */
	protected boolean isValidType(Class<?> type, Annotation[] classAnnotations) {
		if (type == null)
			return false;
		
		if (annotated) {
			return checkAnnotation(type);			
		} else if (scanpackages != null) {
			String classPackage = type.getPackage().getName();
			for (String pkg : scanpackages) {
				if (classPackage.startsWith(pkg)) {
					if (annotated) {
						return checkAnnotation(type);		
					} else
						return true;
				}
					
			}

			return false;
		} else if (clazzes != null) {
			for (Class<?> cls : clazzes) { // must strictly equal. Don't check
											// inheritance
				if (cls == type)
					return true;
			}

			return false;
		}

		return true;
	}

	private boolean checkAnnotation(Class<?> type) {
		Annotation[] annotations = type.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof FastJsonType) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Check media type like "application/json".
	 * 
	 * @param mediaType
	 *            media type
	 * @return true if the media type is valid
	 */
	protected boolean hasMatchingMediaType(MediaType mediaType) {
		if (mediaType != null) {
			String subtype = mediaType.getSubtype();
			return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype);
		}
		return true;
	}

	public String toJSONString(Object object, SerializeFilter filter, SerializerFeature[] features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out, fastJsonConfig.serializeConfig);
            if (features != null) {
            	for (SerializerFeature feature : features) {
                    serializer.config(feature, true);
                }
            }
            
            if (filter != null) {
                if (filter instanceof PropertyPreFilter) {
                    serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
                }

                if (filter instanceof NameFilter) {
                    serializer.getNameFilters().add((NameFilter) filter);
                }

                if (filter instanceof ValueFilter) {
                    serializer.getValueFilters().add((ValueFilter) filter);
                }

                if (filter instanceof PropertyFilter) {
                    serializer.getPropertyFilters().add((PropertyFilter) filter);
                }

                if (filter instanceof BeforeFilter) {
                    serializer.getBeforeFilters().add((BeforeFilter) filter);
                }
                
                if (filter instanceof AfterFilter) {
                    serializer.getAfterFilters().add((AfterFilter) filter);
                }
            }

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

	/*
	 * /********************************************************** /* Partial
	 * MessageBodyWriter impl
	 * /**********************************************************
	 */

	/**
	 * Method that JAX-RS container calls to try to check whether given value
	 * (of specified type) can be serialized by this provider.
	 */
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (!hasMatchingMediaType(mediaType)) {
			return false;
		}

		return isValidType(type, annotations);
	}

	/**
	 * Method that JAX-RS container calls to try to figure out serialized length
	 * of given value. always return -1 to denote "not known".
	 */
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	/**
	 * Method that JAX-RS container calls to serialize given value.
	 */
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		SerializeFilter filter = null;

        if(pretty) {
			if (fastJsonConfig.serializerFeatures == null)
				fastJsonConfig.serializerFeatures = new  SerializerFeature[]{SerializerFeature.PrettyFormat};
            else {
                List<SerializerFeature> serializerFeatures = Arrays.asList(fastJsonConfig.serializerFeatures);
                serializerFeatures.add(SerializerFeature.PrettyFormat);
                fastJsonConfig.serializerFeatures = serializerFeatures.toArray(new SerializerFeature[]{});
            }
		}

		if (fastJsonConfig.serializeFilters != null)
			filter = fastJsonConfig.serializeFilters.get(type);
		String jsonStr = toJSONString(t, filter, fastJsonConfig.serializerFeatures);
		if (jsonStr != null)
			entityStream.write(jsonStr.getBytes());
	}

	/*
	 * /********************************************************** /*
	 * MessageBodyReader impl
	 * /**********************************************************
	 */

	/**
	 * Method that JAX-RS container calls to try to check whether values of
	 * given type (and media type) can be deserialized by this provider.
	 */
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (!hasMatchingMediaType(mediaType)) {
			return false;
		}

		return isValidType(type, annotations);
	}

	/**
	 * Method that JAX-RS container calls to deserialize given value.
	 */
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		String input = null;
		try {
			input = IOUtils.inputStreamToString(entityStream);
		} catch (Exception e) {
			
		}
		if (input == null) {
            return null;
        }
		if (fastJsonConfig.features == null)
			return JSON.parseObject(input, type, fastJsonConfig.parserConfig, JSON.DEFAULT_PARSER_FEATURE);
		else
			return JSON.parseObject(input, type, fastJsonConfig.parserConfig, JSON.DEFAULT_PARSER_FEATURE, fastJsonConfig.features);
	}

}
