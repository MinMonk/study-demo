package com.monk.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class ZipUtil {

    public static void main(String[] args) throws ZipException {
        String currentDir = "/Users/monk/test/aaa.txt";
        File file = new File(currentDir);
        String toFilePath = "/Users/monk/test/aaa1.zip";
        String password = "abcABC!@123456";

        compressFile(file, toFilePath, password);
    }

    public static void compressFile(File currentDir, String toFilePath, String password) throws ZipException {
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(toFilePath);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        // 是否设置加密文件
        parameters.setEncryptFiles(true);
        // 设置加密算法
        parameters.setEncryptionMethod(EncryptionMethod.AES);
        // 设置AES加密密钥的密钥强度
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        // 设置密码
        if(StringUtils.isNotBlank(password)) {
            zipFile.setPassword(password.toCharArray());
        }
        if(currentDir.isDirectory()){
            // 要打包的文件夹
            File[] fList = currentDir.listFiles();

            // 遍历test文件夹下所有的文件、文件夹
            for (File f : fList) {
                if (f.isDirectory()) {
                    zipFile.addFolder(f, parameters);
                } else {
                    zipFile.addFile(f, parameters);
                }
            }
        }else {
            zipFile.addFile(currentDir, parameters);
        }

    }
}
