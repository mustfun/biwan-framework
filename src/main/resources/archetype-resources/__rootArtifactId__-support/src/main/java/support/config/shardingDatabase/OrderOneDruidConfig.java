package ${groupId}.support.config.shardingDatabase;

import ${groupId}.support.config.BaseDruidConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by dengzhiyuan on 2017/4/5.
 */
@ConfigurationProperties(prefix = "database.order1.druid") //这个注解只是把属性设置进去，还达不到注入要求，要么加一个@configuration，要么加个@Component
public class OrderOneDruidConfig extends BaseDruidConfig{

    private Logger logger = LoggerFactory.getLogger(getClass());

    public OrderOneDruidConfig(){
        logger.info("druid正在初始化中====={order1数据库}",getMaxActive());
    }

}
