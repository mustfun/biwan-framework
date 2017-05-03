package com.itar.soa.biwan.service;

import com.itar.soa.biwan.model.Order;

import java.util.List;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
public interface OrderService {
    Order getOne(Integer id);

    List<Order> selectByIdList(List<Integer> ids);

}
