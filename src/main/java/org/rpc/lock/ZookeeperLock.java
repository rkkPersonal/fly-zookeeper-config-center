package org.rpc.lock;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.rpc.FlyZkSerializer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Steven
 * @date 2022年11月26日 14:59
 */
@Slf4j
public class ZookeeperLock implements Lock {

    private static ZkClient zkClient;

    private String lockKey;

    public ZookeeperLock(String lockKey) {
        this.lockKey = lockKey;
        this.zkClient = new ZkClient("192.168.43.200:2181");
        this.zkClient.setZkSerializer(new FlyZkSerializer());
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            waitForLock();
            lock();
        } else {
            System.out.println(lockKey + " 获取锁成功");
        }
    }

    private void waitForLock() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                countDownLatch.countDown();

            }
        };
        zkClient.subscribeDataChanges(lockKey, listener);

        try {
            countDownLatch.await();
        } catch (InterruptedException exception) {

        }

        zkClient.unsubscribeDataChanges(lockKey, listener);

    }

    @Override
    public boolean tryLock() {
        try {
            zkClient.createEphemeral(lockKey, Thread.currentThread().getName() + " is locked");
        } catch (ZkNodeExistsException e) {
            return false;

        }
        return true;
    }

    @Override
    public void unlock() {
        zkClient.delete(lockKey);
        System.out.println(lockKey + " 锁被释放");
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
