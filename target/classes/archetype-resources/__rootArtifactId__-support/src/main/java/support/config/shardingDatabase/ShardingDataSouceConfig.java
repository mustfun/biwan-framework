package ${groupId}.support.config.shardingDatabase;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import ${groupId}.support.sharding.ModuloDatabaseShardingAlgorithm;
import ${groupId}.support.sharding.ModuloTableShardingAlgorithm;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengzhiyuan on 2017/4/26.
 * 分库分表的数据库配置
 *
 */
@Configuration
@AutoConfigureAfter(value = {OrderOneDataSourceConfig.class,OrderZeroDataSourceConfig.class})
public class ShardingDataSouceConfig {

    @Resource
    private DataSource orderZeroDataSource;

    @Resource
    private DataSource orderOneDataSource;


    @Bean
    public DataSource dataSource(){

        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("ds_0", orderZeroDataSource); //2个数据源 ds_0和ds_1
        dataSourceMap.put("ds_1", orderOneDataSource);

        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);

        //表规则
        TableRule orderTableRule = TableRule
                .builder("t_order")
                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
                .dataSourceRule(dataSourceRule)
                .build();
        //分片规则,分表和分库都用了
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule))
                //分库的时候用user_id去取出余数
                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
                //分表的时候用order_id去取出余数
                .tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()))
                .build();

        //官方文档有问题，已经变成静态方法了

        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }


}
