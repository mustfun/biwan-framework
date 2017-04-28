package com.itar.soa.biwan.support.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Created by dengzhiyuan on 2017/4/26.
 * 取模分片算法
 * 实现单分片键分表算法
 */
public final class ModuloTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {

    /**
     *  select * from t_order from t_order where order_id = 11
     *          └── SELECT *  FROM t_order_1 WHERE order_id = 11
     *  select * from t_order from t_order where order_id = 44
     *          └── SELECT *  FROM t_order_0 WHERE order_id = 44
     *  等于的时候怎么分片
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue()%2+"")){   //tagetName是t_order_1，然后  value为11
                return each;   //比如11进来了  ，那么  就会匹配  t_order_1  因为%2就是1啊
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     *  select * from t_order from t_order where order_id in (11,44)
     *          ├── SELECT *  FROM t_order_0 WHERE order_id IN (11,44)
     *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,44)
     *  select * from t_order from t_order where order_id in (11,13,15)
     *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,13,15)
     *  select * from t_order from t_order where order_id in (22,24,26)
     *          └──SELECT *  FROM t_order_0 WHERE order_id IN (22,24,26)
     *  in的时候怎么分片
     */
    public Collection<String> doInSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());//为什么这里需要一个双向链表？应该是为了保证顺序
        for (Integer value : shardingValue.getValues()) {
            for (String tableName : tableNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    /**
     *  select * from t_order from t_order where order_id between 10 and 20
     *          ├── SELECT *  FROM t_order_0 WHERE order_id BETWEEN 10 AND 20
     *          └── SELECT *  FROM t_order_1 WHERE order_id BETWEEN 10 AND 20
     *          between是如何分片的
     */
    public Collection<String> doBetweenSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        //google guava里面的一个类
        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {//返回上界和下届节点，涉及到开闭区间，比较有意思这个类
            for (String each : tableNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }

}