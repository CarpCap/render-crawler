package com.carpcap.rc.proxy;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author CarpCap
 */
@Data
@Accessors(chain=true)
public class ProxyInfo {
    private String host;
    private Integer port;
}
