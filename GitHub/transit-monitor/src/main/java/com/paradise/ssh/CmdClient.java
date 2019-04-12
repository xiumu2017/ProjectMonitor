package com.paradise.ssh;

import com.paradise.monitor.MR;
import com.paradise.project.domain.ServerInfo;

import java.io.IOException;

/**
 * cmd 命令行客户端
 *
 * @author Paradise
 */
public interface CmdClient {

    /**
     * 客户端登录认证
     *
     * @return 登录结果、认证结果
     * @throws IOException e
     */
    MR login() throws IOException;

    /**
     * 客户端服务器当前时间
     *
     * @return 时间
     * @throws IOException e
     */
    String date() throws IOException;

    /**
     * 客户端关闭
     *
     * @return 结果
     */
    boolean closeClient();

}
