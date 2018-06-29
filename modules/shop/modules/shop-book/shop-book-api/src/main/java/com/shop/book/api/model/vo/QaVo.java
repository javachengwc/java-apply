package com.shop.book.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;


@ApiModel(value = "qaVo", description = "qa信息")
public class QaVo {

    @ApiModelProperty( name = "id",value = "id")
    private Long id;

    @ApiModelProperty( name = "question",value = "问题")
    private String question;

    @ApiModelProperty( name = "type",value = "问题类型")
    private Integer type;

    @ApiModelProperty( name = "question",value = "答案")
    private String answer;

    @ApiModelProperty( name = "isShow",value = "是否显示 0--否 1--是")
    private Integer isShow;

    @ApiModelProperty( name = "sort",value = "顺序值")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
