package com.itar.soa.biwan.service;

import com.itar.soa.biwan.model.City;

/**
 * Created by dengzhiyuan on 2017/4/6.
 */
public interface CityService {
    City getOne(Integer id);

    Integer addOneCity(City city);

    City saveAndGet(City city);
}
