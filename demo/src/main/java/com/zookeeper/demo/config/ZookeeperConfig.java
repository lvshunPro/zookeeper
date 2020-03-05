package com.zookeeper.demo.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * @Title
 * @author lv
 * @Date 2020/3/5 11:48
 */
@Configuration
public class ZookeeperConfig {

//    @Value("zk.url")
    private String zkUrl = "127.0.0.1:2181";

    @Bean(name = "curator1")
    public CuratorFramework curatorFramework() {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient(zkUrl,retry);
    }
    @Bean(name = "curator2")
    public CuratorFramework curatorFramework2() {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.builder().connectString(zkUrl).retryPolicy(retry).connectionTimeoutMs(10000).sessionTimeoutMs(1000*60*30).build();
    }
}
