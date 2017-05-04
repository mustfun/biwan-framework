package ${groupId}.support.config;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.NoneDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.NoneTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengzhiyuan on 2017/4/25.
 */
@Configuration  //本分支sharding-jdbc-4测试分库分表，读写分离先去除
@AutoConfigureAfter(value = {MasterDataSourceConfig.class,SlaveDataSourceConfig.class})
public class MasterSlaveDataSourceConfig {

    /**
     * @return 读写分离配置，本配置不涉及到分库分表
     */
    //@Autowired   //这个注解是根据类型进行装配的，我们这类要根据名称进行装配，所以只能用@Resource
    @Resource
    private DataSource masterWriteDataSource;

    @Resource
    private DataSource slaveWriteDataSource;

    @Resource
    private DataSource masterReadDataSource;

    @Resource
    private DataSource slaveReadDataSource;


    /**
     * 这里有个很严重的bug，我们这里配置了很多数据源，但是dataSourceInitializer 在找数据源的时候发现了很多个，不知道注入哪一个，所以没办法
     * @return
     */
    @Bean(name = "dataSource")
    //@Primary //Spring优先选择被该注解所标记的数据源
    public DataSource dataSource(){
        // 构建读写分离数据源, 读写分离数据源实现了DataSource接口,第一个已经分为写
        DataSource masterSlaveDs0 = MasterSlaveDataSourceFactory.createDataSource("datasource_0", masterWriteDataSource,masterReadDataSource);

        // 构建分库分表数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("datasource_0", masterSlaveDs0);
        //dataSourceMap.put("datasource_1", masterSlaveDs1); 先暂时去掉一个数据源

        //这个是是来描述数据源的分布规则的
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap,"datasource_0");//设置一个defaultDataSource非常重要

        //分表规则   这里的mock表示不启用分表规则，具体看源码
        TableRule tableRule = TableRule.builder("mock").actualTables(Arrays.asList("mock")).build();

        //构建一下shardingRule
        ShardingRule shardingRule = ShardingRule
                .builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(tableRule))//没有分表规则，所以直接new了一个
                //无需分库的分片算法，也就是不需要分库
                //第一个参数是分库的策略，一般会使用什么策略进行分库呢？我上篇博文讲到过，一般会使用一致性hash进行分库，如对userId进行一致性hash
                .databaseShardingStrategy(new DatabaseShardingStrategy("none", new NoneDatabaseShardingAlgorithm()))
                //无需分表的分片算法
                .tableShardingStrategy(new TableShardingStrategy("none", new NoneTableShardingAlgorithm()))
                .build();


        // 通过ShardingDataSourceFactory继续创建ShardingDataSource
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }

}
