package ${groupId}.facade;

import ${groupId}.model.Order;
import ${groupId}.support.result.BaseResult;

import java.util.List;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
public interface OrderFacade {

    BaseResult<Order> getOrder(Integer id);

    BaseResult<List<Order>> getOrderListByIds(String ids);

}
