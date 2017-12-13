package com.app.controller;

import com.app.metrics.CounterMetrics;
import com.app.metrics.HistogramMetrics;
import com.app.metrics.MeterMetrics;
import com.app.metrics.TimerMetrics;
import com.codahale.metrics.Timer;
import com.util.base.RandomUtil;
import com.util.base.ThreadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 与rest接口是互斥的，启rest服务就不能启此mvc服务，启mvc服务就不能启rest服务
 * 但通过MvcConfig类处理后，既可以启rest服务，又可以启此mvc服务
 * JerseyConfig不标注@Component 就表示不起rest服务
 */
@Controller
@RequestMapping("/web")
@Api(value = "WebController相关接口")
public class WebController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired(required = false)
    private TimerMetrics timerMetrics;

    @Autowired(required = false)
    private MeterMetrics meterMetrics;

    @Autowired(required = false)
    private CounterMetrics counterMetrics;

    @Autowired(required = false)
    private HistogramMetrics histogramMetrics;

    @ResponseBody
    @RequestMapping(value="/getWebInfo",method = RequestMethod.GET)
    @ApiOperation(value = "getWebInfo", notes = "getWebInfo")
    public Map<String,Object> getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {

        logger.info("WebController getWebInfo invoked.................");
        Timer.Context timerContext = timerMetrics==null?null:timerMetrics.getRequestTimer().time();
        if(meterMetrics!=null) { meterMetrics.getMeter().mark();}
        if(counterMetrics!=null) {counterMetrics.getCounter().inc(); }

        Map<String,Object> map  = new HashMap<String,Object>();

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //		request.getContextPath();/mll
        //		request.getRequestURI(); /mll/web/getWebInfo.do
        //		request.getRequestURL(); http://localhost:8081/mll/web/getWebInfo.do
        //		request.getServletPath(); /web/getWebInfo.do

        map.put("contextPath",request.getContextPath());
        map.put("requestURI",request.getRequestURI());
        map.put("requestURL",request.getRequestURL().toString());
        map.put("servletPath",request.getServletPath());

        //webapp的path F:\workproject\java-application\modules\webapp\modules\app-z7z8\src\main\webapp
        String url = session.getServletContext().getRealPath("/");
        map.put("webappRootPath",url);

        if(timerContext!=null) {
            Integer rdmInt =RandomUtil.nextRandomInt(10,200);
            ThreadUtil.sleep(rdmInt.longValue());
            timerContext.stop();
            if(histogramMetrics!=null) {
                histogramMetrics.getHistogram().update(rdmInt.intValue());
            }
        }

        return map;
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    @ApiOperation(value = "redirect", notes = "redirect")
    public String redirect(@RequestParam(value = "url") String url, HttpServletRequest request)
    {
        logger.info("WebController redirect invoked.................");

        if(!StringUtils.isBlank(url) && (url.startsWith("http") ||  url.startsWith("www")))
        {
            return "redirect:" + url;
        } else {
            return "";
        }
    }

}
