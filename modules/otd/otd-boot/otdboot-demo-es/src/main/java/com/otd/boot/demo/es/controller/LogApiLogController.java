package com.otd.boot.demo.es.controller;

import com.model.base.PageVo;
import com.otd.boot.component.util.JsonUtil;
import com.otd.boot.demo.es.model.doc.LogApiLog;
import com.otd.boot.demo.es.model.vo.LogApiLogSearch;
import com.otd.boot.demo.es.service.repository.LogApiLogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/apiLog")
@Api(value = "ES日志接口", description = "ES日志接口")
public class LogApiLogController {

    private static Logger logger = LoggerFactory.getLogger(LogApiLogController.class);

    @Autowired(required = false)
    private LogApiLogRepository logApiLogRepository;

    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    @ApiOperation("分页查询es日志")
    public PageVo<LogApiLog> queryPage(LogApiLogSearch search) {
        logger.info("LogApiLogController queryPage search={}" , JsonUtil.obj2Json(search));
        Sort sort = Sort.by(Sort.Direction.DESC, "vistorId");
        Pageable pageable = PageRequest.of(search.getPage() - 1, search.getRows(), sort);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(search.getResultCode())) {
            query.must(QueryBuilders.matchQuery("resultCode", search.getResultCode()));
        }
        if (StringUtils.isNotBlank(search.getException())) {
            query.must(QueryBuilders.wildcardQuery("exception", "*" + search.getException() + "*"));
        }
        if (StringUtils.isNotBlank(search.getSysName())) {
            query.must(QueryBuilders.matchQuery("sysName", search.getSysName()));
        }
        if (StringUtils.isNotBlank(search.getParams())) {
            query.must(QueryBuilders.wildcardQuery("params.keyword", "*" + search.getParams() + "*"));
        }
        if (null != search.getModelId()) {
            query.must(QueryBuilders.matchQuery("modelId", search.getModelId()));
        }
        if (null != search.getStartTime()) {
            query.must(QueryBuilders.rangeQuery("createTime").from(search.getStartTime().getTime()).to(search.getEndTime().getTime()));
        }
        Page page = null;
        try {
            logger.info(String.format("ES日志【请求参数】: [query:%s],[pageable:%s]", query.toString(), pageable.toString()));
            page = logApiLogRepository.search(query, pageable);
            logger.info(String.format("ES日志【返回数据】: [page.getContent:%s]",  JsonUtil.obj2Json(page.getContent())));
        } catch (Exception e) {
            logger.error(String.format("ES日志【异常信息】: [Exception:%s]", e.toString()));
        }
        PageVo<LogApiLog> pageVo = new PageVo();
        pageVo.setList(page.getContent());
        pageVo.setTotalCount(new Long(page.getTotalElements()).intValue());
        return pageVo;
    }

}
