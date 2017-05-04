package ${groupId}.facade;

import ${groupId}.model.City;
import ${groupId}.support.result.BaseResult;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
public interface CityController {

    BaseResult<City> getCity(Integer id);

    BaseResult<Integer> addOneCity(City id);

    BaseResult<City> saveAndGet(City id);
}
