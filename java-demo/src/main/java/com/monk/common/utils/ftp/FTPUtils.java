/**
 * 
 * 文件名：FTPUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP工具类
 * 
 * @author Monk
 * @version V1.0
 * @date 2018年12月21日 下午4:18:12
 */
public class FTPUtils {

    private static FTPClient ftp = null;

    /**
     * 获取FTP连接
     * 
     * @param ip
     *            ip地址
     * @param port
     *            端口号
     * @param userName
     *            用户名
     * @param password
     *            密码
     * @return FTP连接对象
     * @throws Exception
     * @author Monk
     * @date 2018年12月21日 下午5:30:46
     */
    public static FTPClient getConnect(String ip, int port, String userName, String password) throws Exception {
        ftp = new FTPClient();
        ftp.connect(ip, port);
        ftp.login(userName, password);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.logout();
            ftp.disconnect();
            System.out.println("get ftp connection failed.");
            return null;
        }
        System.out.println("get conncetion succeed.");
        return ftp;
    }

    /**
     * 统计文件目录下的文件数量
     * 
     * @param path
     *            要统计的文件目录
     * @return 该目录下的文件数量
     * @throws IOException
     * @author Monk
     * @date 2018年12月21日 下午5:31:15
     */
    public static int countFileNum(String path) throws IOException {
        int total = 0;
        ftp.changeWorkingDirectory(path);
        FTPFile[] files = ftp.listFiles();
        for (FTPFile file : files) {
            if (file.isFile()) {
                total++;
                System.out.println(path + "/" + file.getName());
            }
            if (file.isDirectory()) {
                System.out.println(path + "/" + file.getName());
                total += countFileNum(path + "/" + file.getName());
            }
        }
        return total;
    }

    /**
     * 关闭FTP连接
     * 
     * @author Monk
     * @date 2018年12月21日 下午5:31:41
     */
    public static void closeConnect() {
        if (ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String userName = "wang";
        String password = "123456";
        String ip = "10.204.103.176";
        int port = 21;
        String path = "/home/wang/Desktop/test_app_3";
        try {
            ftp = getConnect(ip, port, userName, password);
            System.out.println(countFileNum(path));
            closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
