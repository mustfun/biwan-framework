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
@EnableConfigurationProperties(OrderZeroDruidConfig.class)
public class OrderZeroDataSourceConfig extends AbstractDataSourceConfig {

    private static final Logger logger= LoggerFactory.getLogger(OrderZeroDataSourceConfig.class);

    @Autowired
    private OrderZeroDruidConfig orderZeroDruidConfig;

    @Bean(name="orderZeroDataSource", initMethod = "init", destroyMethod = "close") //也可以为master
    @Primary  //Spring优先选择被该注解所标记的数据源            //放在这里是因为在初始化的时候必须要指定一个，不然很多地方会注入失败
    public DataSource orderZeroDataSource() throws Exception{
        logger.info("订单0号数据库 DataSource正在初始化........");
        return initDataBase(orderZeroDruidConfig);
    }
}
