package com.gm.shared.jpa.eclipselink.rest.spring.httpconverter;


import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class HttpMessageConverterImpl<T> extends AbstractGenericHttpMessageConverter<T> {

    @Autowired
    private ProjectService projectService;

    @Override
    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }

    /**
     * This implementation checks if the given class is
     * {@linkplain #supports(Class) supported}, and if the
     * {@linkplain #getSupportedMediaTypes() supported} media types
     * {@linkplain MediaType#includes(MediaType) include} the given media type.
     */
    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {


        return (type instanceof Class ? canWrite(clazz, mediaType) :
                canWrite((Class<?>) ParameterizedType.class.cast(type).getActualTypeArguments()[0], mediaType));
    }


    /**
     * This implementation checks if the given class is {@linkplain #supports(Class) supported},
     * and if the {@linkplain #getSupportedMediaTypes() supported media types}
     * {@linkplain MediaType#includes(MediaType) include} the given media type.
     *
     * @param clazz
     * @param mediaType
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return isJsonAndSupported(clazz, mediaType);
    }

    /**
     * Returns {@code true} if any of the {@linkplain #setSupportedMediaTypes(List)
     * supported} media types {@link MediaType#includes(MediaType) include} the
     * given media type.
     *
     * @param mediaType the media type to read, can be {@code null} if not specified.
     *                  Typically the value of a {@code Content-Type} header.
     * @return {@code true} if the supported media types include the media type,
     * or if the media type is {@code null}
     */
    @Override
    protected boolean canRead(MediaType mediaType) {
        return isJsonAndSupported(mediaType);
    }

    /**
     * This implementation checks if the given class is
     * {@linkplain #supports(Class) supported}, and if the
     * {@linkplain #getSupportedMediaTypes() supported} media types
     * {@linkplain MediaType#includes(MediaType) include} the given media type.
     *
     * @param clazz
     * @param mediaType
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return isJsonAndSupported(clazz, mediaType);
    }

    /**
     * Returns {@code true} if the given media type includes any of the
     * {@linkplain #setSupportedMediaTypes(List) supported media types}.
     *
     * @param mediaType the media type to write, can be {@code null} if not specified.
     *                  Typically the value of an {@code Accept} header.
     * @return {@code true} if the supported media types are compatible with the media type,
     * or if the media type is {@code null}
     */
    @Override
    protected boolean canWrite(MediaType mediaType) {
        return isJsonAndSupported(mediaType);
    }

    /**
     * Abstract template method that writes the actual body. Invoked from {@link #write}.
     *
     * @param t             the object to write to the output message
     * @param type          the type of object to write (may be {@code null})
     * @param outputMessage the HTTP output message to write to
     * @throws IOException                     in case of I/O errors
     * @throws HttpMessageNotWritableException in case of conversion errors
     */
    @Override
    protected void writeInternal(T t, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        projectService.marshal(t, outputMessage.getBody());
    }

    /**
     * Abstract template method that reads the actual object. Invoked from {@link #read}.
     *
     * @param clazz        the type of object to return
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws IOException                     in case of I/O errors
     * @throws HttpMessageNotReadableException in case of conversion errors
     */
    @Override
    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return projectService.unmarshal(clazz, inputMessage.getBody());
    }

    /**
     * Read an object of the given type form the given input message, and returns it.
     *
     * @param type         the (potentially generic) type of object to return. This type must have
     *                     previously been passed to the {@link #canRead canRead} method of this interface,
     *                     which must have returned {@code true}.
     * @param contextClass a context class for the target type, for example a class
     *                     in which the target type appears in a method signature (can be {@code null})
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws IOException                     in case of I/O errors
     * @throws HttpMessageNotReadableException in case of conversion errors
     */
    @Override
    public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return (T) readInternal((Class<? extends T>) ParameterizedType.class.cast(type).getActualTypeArguments()[0], inputMessage);
    }

    /**
     * Return the list of {@link MediaType} objects supported by this converter.
     *
     * @return the list of supported media types
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return asList(APPLICATION_JSON);
    }

    private boolean isJsonAndSupported(Class<?> aClass, MediaType mediaType) {
        // ONLY JSON is supported. NULL is assumed to JSON
        return projectService.supportsClass(aClass) && (mediaType == null || mediaType.equals(APPLICATION_JSON));
    }

    private boolean isJsonAndSupported(MediaType mediaType) {
        return mediaType == null || mediaType.equals(APPLICATION_JSON);
    }
}
