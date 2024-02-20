package com.otd.boot.oms.controller.base;

import com.model.base.Resp;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.oms.model.entity.BaseProduct;
import com.otd.boot.oms.model.vo.BaseProductVo;
import com.otd.boot.oms.service.base.BaseProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base/product")
@Api(value = "基础物料接口", description = "基础物料接口")
public class BaseProductController {

    @Autowired
    private BaseProductService baseProductService;

    @RequestMapping(value = "/queryByProductCode", method = RequestMethod.GET)
    @ApiOperation("根据产品编码查询物料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productCode", value = "产品编码", required = true, dataTypeClass =String.class, paramType = "query")
    })
    public Resp<BaseProductVo> queryPage(@RequestParam(value = "productCode") String productCode ) {
        if(StringUtils.isBlank(productCode)) {
            return Resp.error("产品编码不能为空");
        }
        BaseProduct baseProduct= baseProductService.queryByProductCode(productCode);
        if(baseProduct==null) {
            return Resp.error("产品不存在");
        }
        BaseProductVo baseProductVo = TransUtil.transEntity(baseProduct,BaseProductVo.class);
        return Resp.data(baseProductVo);
    }
}
