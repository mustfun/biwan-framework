package ${groupId}.support.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @Author dengzhiyuan
 * @Date 2017/4/26
 * @Version 1.0
 * 继承单分片键数据源算法
 */
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer>{//给了一个Integer的泛型

    /**
     * 如果数据是11  那么就会落到 1这张表，这是对于 select * from table  =11这种情况
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        for (String name: availableTargetNames) {
            if (name.endsWith(shardingValue.getValue()%2+"")){
                return name;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result=new LinkedHashSet<>();
        for (Integer value:shardingValue.getValues()){
            for (String name:availableTargetNames){
                if (name.endsWith(value%2+"")){
                    result.add(name);
                }
            }
        }
        return result; //这里感觉有bug，in里面的不管怎么样都会被筛到这个list中
    }

    /**
     * 针对between
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result=new LinkedHashSet<>();
        Range<Integer> valueRange = shardingValue.getValueRange();
        for (int i=valueRange.lowerEndpoint();i<valueRange.upperEndpoint();i++){
            for (String name : availableTargetNames) {
                if (name.endsWith(i%2+"")){
                    result.add(name);
                }
            }
        }
        return result;
    }
}
