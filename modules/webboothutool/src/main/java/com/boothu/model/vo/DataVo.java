package com.boothu.model.vo;

import com.boothu.util.ExcelAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVo {

    private Integer type;

    @ExcelAlias(value = "代码")
    private String code;

    @ExcelAlias(value = "名称")
    private String name;

    @ExcelAlias(value = "时间")
    private String recordTimeStr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime recordTime;

    @ExcelAlias(value = "降雨量")
    private BigDecimal rain;

    @ExcelAlias(value = "时长")
    private String duration;

    @ExcelAlias(value = "水位")
    private BigDecimal z;

    @ExcelAlias(value = "流量")
    private BigDecimal q;

    @ExcelAlias(value = "入库量")
    private BigDecimal inBound;

    @ExcelAlias(value = "出库量")
    private BigDecimal outBound;

}
