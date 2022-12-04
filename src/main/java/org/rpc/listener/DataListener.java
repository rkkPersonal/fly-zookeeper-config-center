package org.rpc.listener;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.rpc.config.ConfigCenter;

/**
 * @author Steven
 * @date 2022年11月26日 14:48
 */
@Slf4j
public class DataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
       log.info("data path :" + dataPath + ",数据更新了:" + data);
        if (ConfigCenter.configMap.containsKey(dataPath)) {
            ConfigCenter.configMap.put(dataPath, data);
        }
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        log.info("dataPath:" + dataPath + ",被删除了");
        if (ConfigCenter.configMap.containsKey(dataPath)) {
            ConfigCenter.configMap.remove(dataPath);
        }
    }
}
