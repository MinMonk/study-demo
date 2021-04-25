package com.monk.rule;

import com.monk.rule.random.RadomRobinV1;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomRule extends AbstractLoadBalancerRule {

    public static final Logger logger = LoggerFactory.getLogger(CustomRule.class);

    private Random random = null;

    private int skipIndex = -1;
    private int currIndex = -1;
    private int lastIndex = -1;

    public CustomRule(){
        random = new Random();
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            int index = 0;
            if(serverCount > 1){
                index = chooseRandomInt(serverCount);
                lastIndex = index;
                logger.info("服务器数量:[{}], 最终随机到的下标:[{}]", serverCount, lastIndex);
            }

            server = upList.get(index);

            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            server = null;
            Thread.yield();
        }
        return server;
    }

    /**
     * 自定义随机策略:如果连续随机到一台server2次,那么就继续随机直到不是重复了两次的机器为止
     * @param serverCount
     * @return
     */
    protected int chooseRandomInt(int serverCount) {
        currIndex = random.nextInt(serverCount);
        logger.debug("当前下标[{}]", currIndex);
        if(currIndex == skipIndex){
            do{
                currIndex = random.nextInt(serverCount);
            }while(currIndex == skipIndex);
            logger.debug("跳过之后的下标:[{}]", currIndex);
        }

        if(currIndex == lastIndex){
            logger.debug("需要跳过的下标:[{}]", currIndex);
            skipIndex = currIndex;
        }else{
            skipIndex = -1;
        }

        return currIndex;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }
}
