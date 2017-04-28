package com.itar.soa.biwan.support.config.shardingDatabase;

import com.itar.soa.biwan.support.config.AbstractDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by dengzhiyuan on 2017/4/26.
 */
@Configuration
@EnableConfigurationProperties(OrderOneDruidConfig.class)
public class OrderOneDataSourceConfig extends AbstractDataSourceConfig {

    private static final Logger logger= LoggerFactory.getLogger(OrderOneDataSourceConfig.class);


    @Autowired
    private OrderOneDruidConfig orderOneDruidConfig;

    @Bean(name="orderOneDataSource", initMethod = "init", destroyMethod = "close") //也可以为master
    public DataSource orderOneDataSource() throws Exception{
        logger.info("订单1号数据库 DataSource正在初始化........");
        return initDataBase(orderOneDruidConfig);
    }


}
