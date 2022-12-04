package org.rpc;


import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.rpc.lock.ZookeeperLock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class App {

    private volatile static int count = 3;

    public static void reduceStock() {
        String name = Thread.currentThread().getName();
        if (count > 0) {
            count--;
            System.out.println(name + "---------【出库成功】");
        } else {

            System.out.println(name + "=======》》库存不足");
        }

    }


    public static void main(String[] args) throws IOException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException exception) {

                }
                ZookeeperLock lock = new ZookeeperLock("/locks");
                try {
                    lock.lock();
                    reduceStock();
                } finally {
                    lock.unlock();
                }

            }).start();
        }

        System.in.read();
    }

    private static void zkConfig() throws IOException {
        ZkClient zkClient = new ZkClient("192.168.43.200:2181", 5000, 15000, new FlyZkSerializer());
        String path = "/hello";
        if (!zkClient.exists(path)) {
            zkClient.createPersistent(path, "hi how are you ?");
        }
        Object value = zkClient.readData(path);
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("data path :" + dataPath + ",数据更新了:" + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("dataPath:" + dataPath + ",被删除了");
            }
        });
        System.out.println(value);
        System.in.read();
    }
}
