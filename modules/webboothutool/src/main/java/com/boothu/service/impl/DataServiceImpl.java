package com.boothu.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.boothu.model.vo.DataVo;
import com.boothu.service.DataService;
import com.boothu.util.ExcelAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Override
    public List<DataVo> addData(MultipartFile file) {
        log.info("DataServiceImpl addData start........");
        List<DataVo> list = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader readerAll = ExcelUtil.getReader(inputStream);
            int sheetCount = readerAll.getSheetCount();
            log.info("DataServiceImpl addData sheetCount={}",sheetCount);
            List<String> sheetNameList = readerAll.getSheetNames();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < sheetCount; i++) {
                String sheetName = sheetNameList.get(i);
                log.info("DataServiceImpl addData i={},sheetName={}",i,sheetName);
                ExcelReader sheetReader = ExcelUtil.getReader(file.getInputStream(), i);
                sheetReader.setHeaderAlias(getHeaderAlias(DataVo.class, true));
                List<DataVo> sheetDataList = sheetReader.readAll(DataVo.class);
                for (DataVo dataVo: sheetDataList) {
                    dataVo.setType(i);
                    String recordTimeStr = dataVo.getRecordTimeStr();
                    LocalDateTime recordTime = LocalDateTime.parse(recordTimeStr, formatter);
                    dataVo.setRecordTime(recordTime);
                    list.add(dataVo);
                }
            }
        } catch (Exception e) {
            log.error("DataServiceImpl addData error,",e);
        }
        log.info("DataServiceImpl addData end........");
        for(DataVo dataVo:list) {
            log.info("DataServiceImpl dataVo={}",dataVo);
        }
        return list;
    }

    public static <T> Map<String, String> getHeaderAlias(Class<T> beanType, boolean isRead) {
        Map headerAlias = new HashMap<String, String>();
        List<Field> fields = Arrays.asList(ReflectUtil.getFields(beanType));
        if (fields.isEmpty()) {
            return headerAlias;
        }
        for (Field field : fields) {
            // 获得每个字段的 @ExcelAlias注解
            ExcelAlias anno = field.getAnnotation(ExcelAlias.class);
            if (anno == null || "".equals(anno.value())) {
                continue;
            }
            if (isRead) {
                headerAlias.put(anno.value(), field.getName());
            } else {
                headerAlias.put(field.getName(), anno.value());
            }
        }
        return headerAlias;
    }
}
