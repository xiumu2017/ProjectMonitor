package com.paradise.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Paradise
 * @date 2019-3-13
 */
public class ConnectLinuxCommand {
    private static final Logger logger = LoggerFactory.getLogger(ConnectLinuxCommand.class);

    private static String DEFAULT_CHARSET = "UTF-8";

    private static Connection connection;

    public static boolean login(RemoteConnect remoteConnect) {
        boolean flag = false;
        try {
            connection = new Connection(remoteConnect.getIp());
            connection.connect();
            flag = connection.authenticateWithPassword(remoteConnect.getUserName(), remoteConnect.getPassword());
            if (flag) {
                logger.info("SSH - 认证成功！");
            } else {
                logger.info("认证失败！");
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
        }
        return flag;
    }

    public static String execute(String cmd) {
        String result = "";

        try {
            Session session = connection.openSession();
            session.execCommand(cmd);
            result = processStdout(session.getStdout(), DEFAULT_CHARSET);
            if (StringUtils.isBlank(result)) {
                result = processStdout(session.getStderr(), DEFAULT_CHARSET);
            }
            connection.close();
            session.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
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
            logger.error(e.getLocalizedMessage(), e);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        RemoteConnect remoteConnect = new RemoteConnect("192.168.1.47", "root", "bigdata#2018");
        ConnectLinuxCommand.login(remoteConnect);
        String cmd = "tail -f /software/apache-tomcat-8.0.43//logs/catalina.out";
        logger.info("command: " + cmd);
        String res = ConnectLinuxCommand.execute(cmd);
        logger.info(res);
    }

}
