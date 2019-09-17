package com.spring.pseudocode.web.web.client;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;

import com.spring.pseudocode.web.http.client.support.InterceptingHttpAccessor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.*;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {

    private static boolean romePresent =
            ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", org.springframework.web.client.RestTemplate.class.getClassLoader());

    private static final boolean jaxb2Present =
            ClassUtils.isPresent("javax.xml.bind.Binder", org.springframework.web.client.RestTemplate.class.getClassLoader());

    private static final boolean jackson2Present =
            ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", org.springframework.web.client.RestTemplate.class.getClassLoader()) &&
                    ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", org.springframework.web.client.RestTemplate.class.getClassLoader());

    private static final boolean jackson2XmlPresent =
            ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", org.springframework.web.client.RestTemplate.class.getClassLoader());

    private static final boolean gsonPresent =
            ClassUtils.isPresent("com.google.gson.Gson", org.springframework.web.client.RestTemplate.class.getClassLoader());


    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    private UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

    private final ResponseExtractor<HttpHeaders> headersExtractor = new RestTemplate.HeadersExtractor();

    public RestTemplate() {
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ResourceHttpMessageConverter());
        this.messageConverters.add(new SourceHttpMessageConverter<Source>());
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        if (romePresent) {
            this.messageConverters.add(new AtomFeedHttpMessageConverter());
            this.messageConverters.add(new RssChannelHttpMessageConverter());
        }

        if (jackson2XmlPresent) {
            this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        }
        else if (jaxb2Present) {
            this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }

        if (jackson2Present) {
            this.messageConverters.add(new MappingJackson2HttpMessageConverter());
        }
        else if (gsonPresent) {
            this.messageConverters.add(new GsonHttpMessageConverter());
        }
    }

    public RestTemplate(ClientHttpRequestFactory requestFactory) {
        this();
        setRequestFactory(requestFactory);
    }

    public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
        this.messageConverters.addAll(messageConverters);
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
        // Take getMessageConverters() List as-is when passed in here
        if (this.messageConverters != messageConverters) {
            this.messageConverters.clear();
            this.messageConverters.addAll(messageConverters);
        }
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
        this.errorHandler = errorHandler;
    }

    public ResponseErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public void setUriTemplateHandler(UriTemplateHandler handler) {
        Assert.notNull(handler, "UriTemplateHandler must not be null");
        this.uriTemplateHandler = handler;
    }

    public UriTemplateHandler getUriTemplateHandler() {
        return this.uriTemplateHandler;
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... urlVariables)
            throws RestClientException {

        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> urlVariables)
            throws RestClientException {

        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }


    // HEAD

    @Override
    public HttpHeaders headForHeaders(String url, Object... urlVariables) throws RestClientException {
        return execute(url, HttpMethod.HEAD, null, headersExtractor(), urlVariables);
    }

    @Override
    public HttpHeaders headForHeaders(String url, Map<String, ?> urlVariables) throws RestClientException {
        return execute(url, HttpMethod.HEAD, null, headersExtractor(), urlVariables);
    }

    @Override
    public HttpHeaders headForHeaders(URI url) throws RestClientException {
        return execute(url, HttpMethod.HEAD, null, headersExtractor());
    }


    // POST

    @Override
    public URI postForLocation(String url, Object request, Object... urlVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), urlVariables);
        return headers.getLocation();
    }

    @Override
    public URI postForLocation(String url, Object request, Map<String, ?> urlVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), urlVariables);
        return headers.getLocation();
    }

    @Override
    public URI postForLocation(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor());
        return headers.getLocation();
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
            throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<T>(responseType, getMessageConverters());
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables)
            throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
    }


    // PUT

    @Override
    public void put(String url, Object request, Object... urlVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, null, urlVariables);
    }

    @Override
    public void put(String url, Object request, Map<String, ?> urlVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, null, urlVariables);
    }

    @Override
    public void put(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, null);
    }


    // DELETE

    @Override
    public void delete(String url, Object... urlVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null, urlVariables);
    }

    @Override
    public void delete(String url, Map<String, ?> urlVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null, urlVariables);
    }

    @Override
    public void delete(URI url) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null);
    }


    // OPTIONS

    @Override
    public Set<HttpMethod> optionsForAllow(String url, Object... urlVariables) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = execute(url, HttpMethod.OPTIONS, null, headersExtractor, urlVariables);
        return headers.getAllow();
    }

    @Override
    public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> urlVariables) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = execute(url, HttpMethod.OPTIONS, null, headersExtractor, urlVariables);
        return headers.getAllow();
    }

    @Override
    public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = execute(url, HttpMethod.OPTIONS, null, headersExtractor);
        return headers.getAllow();
    }


    // exchange

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
                                          Class<T> responseType) throws RestClientException {

        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(url, method, requestCallback, responseExtractor);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {

        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {

        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
                                          ParameterizedTypeReference<T> responseType) throws RestClientException {

        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return execute(url, method, requestCallback, responseExtractor);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType)
            throws RestClientException {

        Assert.notNull(requestEntity, "'requestEntity' must not be null");

        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType)
            throws RestClientException {

        Assert.notNull(requestEntity, "'requestEntity' must not be null");

        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
    }


    // general execution

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
                         ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {

        URI expanded = getUriTemplateHandler().expand(url, urlVariables);
        return doExecute(expanded, method, requestCallback, responseExtractor);
    }

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
                         ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) throws RestClientException {

        URI expanded = getUriTemplateHandler().expand(url, urlVariables);
        return doExecute(expanded, method, requestCallback, responseExtractor);
    }

    @Override
    public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback,
                         ResponseExtractor<T> responseExtractor) throws RestClientException {

        return doExecute(url, method, requestCallback, responseExtractor);
    }

    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
                              ResponseExtractor<T> responseExtractor) throws RestClientException {

        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            handleResponse(url, method, response);
            if (responseExtractor != null) {
                return responseExtractor.extractData(response);
            }
            else {
                return null;
            }
        }
        catch (IOException ex) {
            throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + url + "\": " + ex.getMessage(), ex);
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }

    protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        if (logger.isDebugEnabled()) {
            try {
                logger.debug(method.name() + " request for \"" + url + "\" resulted in " +
                        response.getRawStatusCode() + " (" + response.getStatusText() + ")" +
                        (hasError ? "; invoking error handler" : ""));
            }
            catch (IOException ex) {
                // ignore
            }
        }
        if (hasError) {
            errorHandler.handleError(response);
        }
    }

    protected <T> RequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
        return new AcceptHeaderRequestCallback(responseType);
    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody) {
        return new RestTemplate.HttpEntityRequestCallback(requestBody);
    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return new ResponseEntityResponseExtractor<T>(responseType);
    }

    protected ResponseExtractor<HttpHeaders> headersExtractor() {
        return this.headersExtractor;
    }

    private class AcceptHeaderRequestCallback implements RequestCallback {

        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                Class<?> responseClass = null;
                if (this.responseType instanceof Class) {
                    responseClass = (Class<?>) this.responseType;
                }
                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> converter : getMessageConverters()) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                    else if (converter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                        if (genericConverter.canRead(this.responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharSet() != null) {
                    supportedMediaType =
                            new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }

    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            }
            else if (requestBody != null) {
                this.requestEntity = new HttpEntity<Object>(requestBody);
            }
            else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!this.requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() < 0) {
                    httpHeaders.setContentLength(0L);
                }
            }
            else {
                Object requestBody = this.requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : getMessageConverters()) {
                    if (messageConverter.canWrite(requestType, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        if (logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                        "\" using [" + messageConverter + "]");
                            }
                            else {
                                logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }

                        }
                        ((HttpMessageConverter<Object>) messageConverter).write(
                                requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" +
                        requestType.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
        }
    }

    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {

        private final HttpMessageConverterExtractor<T> delegate;

        public ResponseEntityResponseExtractor(Type responseType) {
            if (responseType != null && Void.class != responseType) {
                this.delegate = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
            }
            else {
                this.delegate = null;
            }
        }

        @Override
        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            if (this.delegate != null) {
                T body = this.delegate.extractData(response);
                return new ResponseEntity<T>(body, response.getHeaders(), response.getStatusCode());
            }
            else {
                return new ResponseEntity<T>(response.getHeaders(), response.getStatusCode());
            }
        }
    }

    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {

        @Override
        public HttpHeaders extractData(ClientHttpResponse response) throws IOException {
            return response.getHeaders();
        }
    }

}
