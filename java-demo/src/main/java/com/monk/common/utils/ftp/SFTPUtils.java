/**
 * 
 * 文件名：SFTPUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils.ftp;

import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP工具类
 * @author Monk
 * @version V1.0
 * @date 2018年12月19日 下午3:25:07
 */
public class SFTPUtils {

    private static JSch jsch = null;

    private static Session session = null;

    private static Channel channel = null;
    
    private static ChannelSftp sftp = null;

    /**
     * 创建ChannelSftp连接
     * 
     * @param user
     *            用户名
     * @param password
     *            密码
     * @param host
     *            IP地址
     * @param port
     *            端口
     * @return ChannelSftp连接
     * @throws JSchException
     * @author Monk
     * @date 2018年12月19日 下午4:30:23
     */
    public static void connect(String user, String password, String host, int port) throws JSchException {
        jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(1000 * 30);
        session.connect();
        System.out.println("session connected.");
        channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
    }

    /**
     * 关闭SFTP连接对象
     * 
     * @author Monk
     * @date 2018年12月19日 下午4:30:06
     */
    public static void close() {
        if (channel != null) {
            channel.disconnect();
            System.out.println("关闭channel成功");
        }
        if (session != null) {
            session.disconnect();
            System.out.println("关闭session成功");
        }
    }

    /**
     * 递归统计目录下的文件数量
     * 
     * @param sftp
     *            ChannelSftp对象
     * @param path
     *            统计的目录
     * @return 文件数量
     * @throws SftpException
     * @author Monk
     * @date 2018年12月19日 下午4:29:07
     */
    @SuppressWarnings("unchecked")
    public static int countFileNum(String path) throws SftpException {
        Vector<LsEntry> vec = sftp.ls(path);
        int fileNum = 0;
        if (vec != null && vec.size() > 0) {
            for (int i = 0; i < vec.size(); i++) {
                String name = vec.get(i).getFilename();
                if ("..".equals(name) || ".".equals(name)) {
                    continue;
                }
                if (vec.get(i).getAttrs().isDir()) {
                    fileNum += countFileNum(path + "/" + name);
                } else {
                    fileNum++;
                }
                System.out.println("file name: " + path + "/" + name);
            }
        }
        return fileNum;
    }

    public static void main(String[] args) {
        String user = "wang";
        String password = "123456";
        String host = "10.204.103.176";
        int port = 22;
        String path = "/home/wang/Desktop/test_app_3";

        try {
            connect(user, password, host, port);
            System.out.println(countFileNum(path));
            close();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
}
