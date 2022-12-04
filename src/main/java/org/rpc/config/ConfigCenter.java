package org.rpc.config;

import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steven
 * @date 2022年11月26日 12:11
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConfigCenter {

    public static final Map<String, Object> configMap = new ConcurrentHashMap<>();

    private boolean isAutoRefresh;


}
