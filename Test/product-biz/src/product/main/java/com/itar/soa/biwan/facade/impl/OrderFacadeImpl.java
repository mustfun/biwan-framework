package com.itar.soa.biwan.facade.impl;

import com.alibaba.fastjson.JSONArray;
import com.itar.soa.biwan.facade.OrderFacade;
import com.itar.soa.biwan.model.Order;
import com.itar.soa.biwan.service.OrderService;
import com.itar.soa.biwan.support.result.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
@Component("orderFacade")
@Path("order")
public class OrderFacadeImpl implements OrderFacade {

    @Autowired
    private OrderService orderService;

    @Override
    @Path("/get/{id}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public BaseResult<Order> getOrder (@PathParam("id") Integer id) {
        Order one = orderService.getOne(id);
        BaseResult<Order> baseResult=new BaseResult<>();
        baseResult.setData(one);
        return baseResult;
    }

    @Path("/getList")
    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    //这里如果把json数据变成ids集合还是有问题的，jackson会报错，为什么不用fastjson
    //目前resteasy只支持jackson,如果要用到jackson,需要定制一下，详情请搜素  jax-rs  fastjson支持
    //这里采用折中方法
    //请求参数  [1,2,3,4,5,6]
    public BaseResult<List<Order>> getOrderListByIds(String ids) {

        /**
         * 整出了4条sql，意料之中，2个库，2张表，都覆盖到了
         */

        BaseResult<List<Order>> baseResult=new BaseResult<>();
        List<Integer> integers = JSONArray.parseArray(ids, Integer.class);
        List<Order> orders = orderService.selectByIdList(integers);
        baseResult.setData(orders);
        return baseResult;
    }
}
