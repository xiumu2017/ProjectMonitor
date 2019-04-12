package com.paradise.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.paradise.monitor.MR;
import com.paradise.project.domain.ServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * LinuxCmdClient - linux命令行客户端
 *
 * @author Paradise
 */
@Slf4j
public class LinuxCmdClient implements CmdClient {

    private static String DEFAULT_CHARSET = "UTF-8";

    private static Connection connection;

    private static Session session;

    private ServerInfo serverInfo;

    LinuxCmdClient(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public MR login() throws IOException {
        boolean flag;
        connection = new Connection(serverInfo.getIp(), Integer.valueOf(serverInfo.getPort()));
        connection.connect();
        flag = connection.authenticateWithPassword(serverInfo.getUserName(), serverInfo.getPassword());
        if (flag) {
            log.info("SSH - 认证成功！");
            String gbk = "GBK";
            if (locale().contains(gbk)) {
                DEFAULT_CHARSET = "gbk";
            }
            return MR.success("SSH - 认证成功！");
        } else {
            log.info("认证失败！");
            connection.close();
            return MR.error(MR.Result_Code.SERVER_AUTH_ERROR, "认证失败！");
        }

    }

    private String locale() throws IOException {
        session = connection.openSession();
        session.execCommand("echo $LANG");
        String result = processStdout(session.getStdout(), DEFAULT_CHARSET);
        if (StringUtils.isBlank(result)) {
            result = processStdout(session.getStderr(), DEFAULT_CHARSET);
        }
        session.close();
        return result;
    }

    @Override
    public String date() throws IOException {
        session = connection.openSession();
        session.execCommand("date");
        String result = processStdout(session.getStdout(), DEFAULT_CHARSET);
        if (StringUtils.isBlank(result)) {
            result = processStdout(session.getStderr(), DEFAULT_CHARSET);
        }
        log.info(result);
        session.close();
        return result;
    }

    @Override
    public boolean closeClient() {
        connection.close();
        return true;
    }


    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp("111.39.14.4");
        serverInfo.setPort("9022");
        serverInfo.setUserName("root");
        serverInfo.setPassword("xuanchengmas#2016");
        LinuxCmdClient client = CmdClients.createLinuxClient(serverInfo);
        try {
            client.login();
            System.out.println(client.date());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.closeClient();
        }

    }

}
