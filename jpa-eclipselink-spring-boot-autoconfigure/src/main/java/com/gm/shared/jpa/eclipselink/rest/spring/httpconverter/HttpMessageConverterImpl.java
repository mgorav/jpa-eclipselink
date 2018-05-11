package com.gm.shared.jpa.eclipselink.rest.spring.httpconverter;


import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class HttpMessageConverterImpl<T> extends AbstractHttpMessageConverter<T> {
    @Autowired
    private ProjectService projectService;

    /**
     * Indicates whether the given class can be read by this converter.
     *
     * @param clazz     the class to test for readability
     * @param mediaType the media type to read (can be {@code null} if not specified);
     *                  typically the value of a {@code Content-Type} header.
     * @return {@code true} if readable; {@code false} otherwise
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return isJsonAndSupported(clazz, mediaType);
    }

    /**
     * Indicates whether the given class can be written by this converter.
     *
     * @param clazz     the class to test for writability
     * @param mediaType the media type to write (can be {@code null} if not specified);
     *                  typically the value of an {@code Accept} header.
     * @return {@code true} if writable; {@code false} otherwise
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return isJsonAndSupported(clazz, mediaType);
    }

    /**
     * Indicates whether the given class is supported by this converter.
     *
     * @param clazz the class to test for support
     * @return {@code true} if supported; {@code false} otherwise
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return isJsonAndSupported(clazz, APPLICATION_JSON);
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
     * Abstract template method that writes the actual body. Invoked from {@link #write}.
     *
     * @param t             the object to write to the output message
     * @param outputMessage the HTTP output message to write to
     * @throws IOException                     in case of I/O errors
     * @throws HttpMessageNotWritableException in case of conversion errors
     */
    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        projectService.marshal(t, outputMessage.getBody());
    }

    private boolean isJsonAndSupported(Class<?> aClass, MediaType mediaType) {
        // ONLY JSON is supported
        return projectService.supportsClass(aClass) && mediaType.equals(APPLICATION_JSON);
    }
}
