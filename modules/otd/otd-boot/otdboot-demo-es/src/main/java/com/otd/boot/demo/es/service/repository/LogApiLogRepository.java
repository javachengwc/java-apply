package com.otd.boot.demo.es.service.repository;

import com.otd.boot.demo.es.model.doc.LogApiLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface LogApiLogRepository extends ElasticsearchRepository<LogApiLog, Long> {
}