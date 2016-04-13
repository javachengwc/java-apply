package com.shop.controller.product;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.PdProduct;
import com.shop.service.product.ProductService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品controller类
 */
@Controller
@RequestMapping(value = "/shop/product")
public class ProductController extends BaseController {

    private static final String PREFIX = "/shop/product/";

    @Autowired
    private ProductService productService;

    //产品页面
    @RequestMapping(value = "/product")
    public String product()
    {
        return PREFIX + "product";
    }

    //产品列表
    @RequestMapping(value = "/productList")
    public void productList(HttpServletResponse response,String productId,String name,Integer sellerId,Integer page ,Integer rows) {

        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        int start = (page - 1) * rows;
        if (start < 0) {
            start = 0;
        }

        List<PdProduct> list = productService.queryPage(productId,name,sellerId,start,rows);
        int count = productService.count(productId,name,sellerId);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
