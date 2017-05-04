package ${groupId}.facade.impl;

import ${groupId}.facade.CityFacade;
import ${groupId}.model.City;
import ${groupId}.service.CityService;
import ${groupId}.support.result.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
@Component("cityFacade")
@Path("city")
public class CityFacadeImpl implements CityFacade {

    @Autowired
    private CityService cityService;

    @Override
    @Path("/get/{id}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public BaseResult<City> getCity(@PathParam("id") Integer id) {
        City one = cityService.getOne(id);
        BaseResult<City> baseResult=new BaseResult<>();
        baseResult.setData(one);
        return baseResult;
    }

    @Override
    @Path("/put")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResult<Integer> addOneCity(@RequestBody City city) {
        Integer integer = cityService.addOneCity(city);
        BaseResult<Integer> baseResult=new BaseResult<>();
        baseResult.setData(city.getId());
        if (integer==1){
            baseResult.setStatus(1);
        }
        return baseResult;
    }
    @Override
    @Path("/saveAndGet")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResult<City> saveAndGet(City city) {
        City returncity=cityService.saveAndGet(city);
        BaseResult<City> baseResult=new BaseResult<>();
        baseResult.setData(returncity);
        return baseResult;
    }
}
