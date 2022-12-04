package org.rpc.parse;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.rpc.config.ConfigCenter;
import org.rpc.listener.DataListener;
import org.rpc.listener.StateListener;


/**
 * @author Steven
 * @date 2022年11月26日 12:14
 */
@Slf4j
public class ConfigParser {

    private ZkClient zkClient;

    public ConfigParser(ZkClient zkClient) {
        this(zkClient, false);
    }

    public ConfigParser(ZkClient zkClient, boolean watch) {
        if (zkClient != null)
            this.zkClient = zkClient;
        if (watch)
            zkClient.subscribeStateChanges(new StateListener());
    }


    public void putConfig(String path, Object data) {
        if (!zkClient.exists(path)) {
            zkClient.createPersistent(path, data);
        } else {
            zkClient.writeData(path, data);
        }
    }

    public Object readConfig(String path, boolean watch) {
        Object value = zkClient.readData(path);
        String wd = watch == true ? "是" : "否";
        log.info("Path:{},当前数据:{}", path, value);
        if (watch) {
            zkClient.subscribeDataChanges(path, new DataListener());
        }
        return value;
    }

    public static void main(String[] args) {

    }
}
