package com.paradise.ssh;

import com.paradise.project.domain.ServerInfo;

/**
 * @author Paradise
 */
public class CmdClients {

    /**
     * 客户端创建
     *
     * @param serverInfo 服务器信息
     * @return 客户端
     */
    public static LinuxCmdClient createLinuxClient(ServerInfo serverInfo) {
        return new LinuxCmdClient(serverInfo);
    }
}
