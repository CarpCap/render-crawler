package com.carpcap.sc.proxy;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;

/**
 * @author CarpCap
 */
@Data
@Accessors(chain=true)
public class ProxyInfo {
    private String host;
    private Integer port;
}
