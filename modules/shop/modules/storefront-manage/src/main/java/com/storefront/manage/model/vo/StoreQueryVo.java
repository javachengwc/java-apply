package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "storeQueryVo", description = "门店查询条件")
public class StoreQueryVo extends PageParam {

    @ApiModelProperty(value = "门店名称")
    private String name;

    @ApiModelProperty(value = "顶级行业code")
    private String firstIdstryCode;

    @ApiModelProperty(value = "直接行业code")
    private String directIdstryCode;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "区域code")
    private String areaCode;

    @ApiModelProperty(value = "位置名称")
    private String positionName;

    @ApiModelProperty(value = "位置类型  0--小区,1--楼盘,2--商场,3--大厦,4--学校,5--步行街")
    private Integer positionType;

    @ApiModelProperty(value = "街道名称")
    private String streetName;

    @ApiModelProperty("开业时间从 格式:yyyy-MM-dd")
    private String startTimeFrom;

    @ApiModelProperty("开业时间到 格式:yyyy-MM-dd")
    private String startTimeTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstIdstryCode() {
        return firstIdstryCode;
    }

    public void setFirstIdstryCode(String firstIdstryCode) {
        this.firstIdstryCode = firstIdstryCode;
    }

    public String getDirectIdstryCode() {
        return directIdstryCode;
    }

    public void setDirectIdstryCode(String directIdstryCode) {
        this.directIdstryCode = directIdstryCode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStartTimeFrom() {
        return startTimeFrom;
    }

    public void setStartTimeFrom(String startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }

    public String getStartTimeTo() {
        return startTimeTo;
    }

    public void setStartTimeTo(String startTimeTo) {
        this.startTimeTo = startTimeTo;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}