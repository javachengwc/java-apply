package com.spring.pseudocode.webmvc.mvc.method.annotation;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.beans.factory.BeanFactoryAware;
import com.spring.pseudocode.beans.factory.InitializingBean;
import com.spring.pseudocode.context.ui.ModelMap;
import com.spring.pseudocode.web.http.converter.ByteArrayHttpMessageConverter;
import com.spring.pseudocode.web.http.converter.HttpMessageConverter;
import com.spring.pseudocode.web.http.converter.StringHttpMessageConverter;
import com.spring.pseudocode.web.http.converter.support.AllEncompassingFormHttpMessageConverter;
import com.spring.pseudocode.web.http.converter.xml.SourceHttpMessageConverter;
import com.spring.pseudocode.web.web.bind.support.WebDataBinderFactory;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;
import com.spring.pseudocode.web.web.context.request.ServletWebRequest;
import com.spring.pseudocode.web.web.method.HandlerMethod;
import com.spring.pseudocode.web.web.method.annotation.ModelFactory;
import com.spring.pseudocode.web.web.method.support.ModelAndViewContainer;
import com.spring.pseudocode.webmvc.ModelAndView;
import com.spring.pseudocode.webmvc.View;
import com.spring.pseudocode.webmvc.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter implements BeanFactoryAware, InitializingBean {

    private ConfigurableBeanFactory beanFactory;

    private List<HttpMessageConverter<?>> messageConverters;

    private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;

    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    //初始化，设置默认的messageConverter
    public RequestMappingHandlerAdapter()
    {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);

        this.messageConverters = new ArrayList(4);
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(stringHttpMessageConverter);
        this.messageConverters.add(new SourceHttpMessageConverter());
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if ((beanFactory instanceof ConfigurableBeanFactory))
            this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
    }

    //实现InitializingBean的afterPropertiesSet,初始化argumentResolvers,returnValueHandlers
    public void afterPropertiesSet()
    {
        if (this.argumentResolvers == null) {
            List resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
        if (this.initBinderArgumentResolvers == null) {
            List resolvers = getDefaultInitBinderArgumentResolvers();
            this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
        if (this.returnValueHandlers == null) {
            List handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }
    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers()
    {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();
//
//        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
//        resolvers.add(new RequestParamMapMethodArgumentResolver());
//        resolvers.add(new PathVariableMethodArgumentResolver());
//        resolvers.add(new PathVariableMapMethodArgumentResolver());
//        resolvers.add(new MatrixVariableMethodArgumentResolver());
//        resolvers.add(new MatrixVariableMapMethodArgumentResolver());
//        resolvers.add(new ServletModelAttributeMethodProcessor(false));
//        resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
//        resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
//        resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
//        resolvers.add(new RequestHeaderMapMethodArgumentResolver());
//        resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
//        resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
//
//        resolvers.add(new ServletRequestMethodArgumentResolver());
//        resolvers.add(new ServletResponseMethodArgumentResolver());
//        resolvers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
//        resolvers.add(new RedirectAttributesMethodArgumentResolver());
//        resolvers.add(new ModelMethodProcessor());
//        resolvers.add(new MapMethodProcessor());
//        resolvers.add(new ErrorsMethodArgumentResolver());
//        resolvers.add(new SessionStatusMethodArgumentResolver());
//        resolvers.add(new UriComponentsBuilderMethodArgumentResolver());
//
//        if (getCustomArgumentResolvers() != null) {
//            resolvers.addAll(getCustomArgumentResolvers());
//        }
//
//        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
//        resolvers.add(new ServletModelAttributeMethodProcessor(true));

        return resolvers;
    }

    private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers()
    {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();
//
//        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
//        resolvers.add(new RequestParamMapMethodArgumentResolver());
//        resolvers.add(new PathVariableMethodArgumentResolver());
//        resolvers.add(new PathVariableMapMethodArgumentResolver());
//        resolvers.add(new MatrixVariableMethodArgumentResolver());
//        resolvers.add(new MatrixVariableMapMethodArgumentResolver());
//        resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
//
//        resolvers.add(new ServletRequestMethodArgumentResolver());
//        resolvers.add(new ServletResponseMethodArgumentResolver());
//
//        if (getCustomArgumentResolvers() != null) {
//            resolvers.addAll(getCustomArgumentResolvers());
//        }
//
//        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));

        return resolvers;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers()
    {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>();
//        handlers.add(new ModelAndViewMethodReturnValueHandler());
//        handlers.add(new ModelMethodProcessor());
//        handlers.add(new ViewMethodReturnValueHandler());
//        handlers.add(new ResponseBodyEmitterReturnValueHandler(getMessageConverters()));
//        handlers.add(new StreamingResponseBodyReturnValueHandler());
//        handlers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
//
//        handlers.add(new HttpHeadersReturnValueHandler());
//        handlers.add(new CallableMethodReturnValueHandler());
//        handlers.add(new DeferredResultMethodReturnValueHandler());
//        handlers.add(new AsyncTaskMethodReturnValueHandler(this.beanFactory));
//        handlers.add(new ListenableFutureReturnValueHandler());
//        if (completionStagePresent) {
//            handlers.add(new CompletionStageReturnValueHandler());
//        }
//
//        handlers.add(new ModelAttributeMethodProcessor(false));
//        handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
//
//        handlers.add(new ViewNameMethodReturnValueHandler());
//        handlers.add(new MapMethodProcessor());
//
//        if (getCustomReturnValueHandlers() != null) {
//            handlers.addAll(getCustomReturnValueHandlers());
//        }
//
//        if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
//            handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
//        }
//        else {
//            handlers.add(new ModelAttributeMethodProcessor(true));
//        }

        return handlers;
    }

    protected boolean supportsInternal(HandlerMethod handlerMethod)
    {
        return true;
    }

    //处理请求的入口
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception
    {
        //checkRequest(request);
        ModelAndView mav;
        //...................
        mav = invokeHandlerMethod(request, response, handlerMethod);
        //...................
        return mav;
    }

    //调用handlerMethod处理请求
    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception
    {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        //WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
        WebDataBinderFactory binderFactory=null;
        //ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
        ModelFactory modelFactory =null;

        ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
        invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
        invocableMethod.setDataBinderFactory(binderFactory);
        invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        //mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
        //modelFactory.initModel(webRequest, mavContainer, invocableMethod);
        //mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

        AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
        asyncWebRequest.setTimeout(this.asyncRequestTimeout);

        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        asyncManager.setTaskExecutor(this.taskExecutor);
        asyncManager.setAsyncWebRequest(asyncWebRequest);
        asyncManager.registerCallableInterceptors(this.callableInterceptors);
        asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

        if (asyncManager.hasConcurrentResult()) {
            Object result = asyncManager.getConcurrentResult();
            mavContainer = (ModelAndViewContainer)asyncManager.getConcurrentResultContext()[0];
            asyncManager.clearConcurrentResult();
            invocableMethod = invocableMethod.wrapConcurrentResult(result);
        }

        invocableMethod.invokeAndHandle(webRequest, mavContainer, new Object[0]);
        if (asyncManager.isConcurrentHandlingStarted()) {
            return null;
        }

        return getModelAndView(mavContainer, modelFactory, webRequest);
    }

    //包装结果为modelAndView
    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer, ModelFactory modelFactory, NativeWebRequest webRequest) throws Exception
    {
        //modelFactory.updateModel(webRequest, mavContainer);
        if (mavContainer.isRequestHandled()) {
            return null;
        }
        ModelMap model = mavContainer.getModel();
        ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
        if (!mavContainer.isViewReference()) {
            mav.setView((View)mavContainer.getView());
        }
        //...................
        return mav;
    }
}
