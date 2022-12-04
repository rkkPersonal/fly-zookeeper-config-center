package org.rpc.listener;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;

/**
 * @author Steven
 * @date 2022年11月26日 14:50
 */
@Slf4j
public class StateListener implements IZkStateListener {
    @Override
    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
        log.info("state changed :" + state);
    }

    @Override
    public void handleNewSession() throws Exception {
        log.info("session is new ");
    }

    @Override
    public void handleSessionEstablishmentError(Throwable error) throws Exception {
        log.error("session is established error .");

    }
}
