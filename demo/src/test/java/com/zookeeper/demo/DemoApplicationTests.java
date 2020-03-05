package com.zookeeper.demo;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/***
 * zookeeper通过curator进行CRUD操作测试
 */
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    public CuratorFramework curator1;

    @Test
    void contextLoads() {
    }

    @Test
    void createNode() throws Exception {
        curator1.start();
        String data = "hello";
        byte[] payload = data.getBytes(StandardCharsets.UTF_8);
        String zkPath = "/test/CRUD/node-1";
        curator1.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkPath,payload);
    }

    @Test
    void readNode() {
        try {
            //实例连接到服务器
            curator1.start();
            String zkPath = "/test/CRUD/node-1";
            //检查节点是否存在
            Stat stat = curator1.checkExists().forPath(zkPath);
            if(stat != null) {
                //获取节点值
                byte[] payLoad = curator1.getData().forPath(zkPath);
                String data = new String(payLoad, StandardCharsets.UTF_8);
                System.out.println(zkPath + "节点值 = " + data);
            }

            //获取子节点列表
            String parentPath = "/test";
            List<String> children = curator1.getChildren().forPath(parentPath);
            for (String child : children) {
                System.out.println("child = " + child);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(curator1);
        }
    }

    @Test
    public void updateNode() {
        try {
            //实例连接到服务器
            curator1.start();
            String zkPath = "/test/CRUD/node-1";
            String data = "hello world";
            byte[] payLoad = data.getBytes(StandardCharsets.UTF_8);
            Stat stat = curator1.setData().forPath(zkPath, payLoad);
            System.out.println("修改结果 = " + stat);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(curator1);
        }
    }

    @Test
    public void AsyncUpdateNode() {
        //异步更新完成，调用实例
        AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
            @SneakyThrows
            @Override
            public void processResult(int i, String s, Object o, String s1) {
                Thread.sleep(1000);
                System.out.println("回调 i = " + i + " | " + " s = " + s + " | " + "o = " + o + " | " + "s1 = " + s1);
            }
        };

        try {
            curator1.start();
            //实例连接到服务器
            String zkPath = "/test/CRUD/node-1";
            String data = "hello world async 1";
            byte[] payLoad = data.getBytes(StandardCharsets.UTF_8);
//            Stat stat = curator1.setData().forPath(zkPath, payLoad);
            Stat stat = curator1.setData().inBackground(callback).forPath(zkPath, payLoad);
            System.out.println("修改调用完成 = " + stat);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(curator1);
        }
    }

    @Test
    public void deleteNode() {
        try {
            curator1.start();
            //实例连接到服务器
            String zkPath = "/test/CRUD/node-1";
            curator1.delete().forPath(zkPath);
            String parentPath = "/test";
            List<String> children = curator1.getChildren().forPath(parentPath);
            for (String child : children) {
                System.out.println(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(curator1);
        }
    }
}
