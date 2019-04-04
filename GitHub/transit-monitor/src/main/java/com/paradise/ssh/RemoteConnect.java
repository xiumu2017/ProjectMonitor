package com.paradise.ssh;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Paradise
 */
@Data
@AllArgsConstructor
public class RemoteConnect {

    private String ip;
    private String userName;
    private String password;
}
