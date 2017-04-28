package com.itar.soa.biwan.facade;

import com.itar.soa.biwan.model.Order;
import com.itar.soa.biwan.support.result.BaseResult;

import java.util.List;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
public interface OrderFacade {

    BaseResult<Order> getOrder(Integer id);

    BaseResult<List<Order>> getOrderListByIds(String ids);

}
