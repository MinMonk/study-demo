package com.monk.license.creator;

import java.util.Calendar;

import com.monk.license.LicenseVerify;

public class TestMain {

    public static void main(String[] args) throws Exception {
        licenseCreate();
        licenseVerify();
    }

    private static void licenseCreate() {
        // 生成license需要的一些参数
        LicenseCreatorParam param = new LicenseCreatorParam();
        param.setSubject("ESBConsole");
        param.setPrivateAlias("privateKey");
        param.setKeyPass("soa@1234");
        param.setStorePass("soa@1234");
        param.setLicensePath("D:\\licenseTest\\license.lic");
        param.setPrivateKeysStorePath("D:\\licenseTest\\privateKeys.keystore");
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        Calendar expiryCalendar = Calendar.getInstance();
        expiryCalendar.set(2021, Calendar.DECEMBER, 31, 23, 59, 59);
        param.setExpiryTime(expiryCalendar.getTime());
        param.setConsumerType("user");
        param.setConsumerAmount(1);
        LicenseCreator licenseCreator = new LicenseCreator(param);
        // 生成license
        licenseCreator.generateLicense();
    }

    private static void licenseVerify() {
        LicenseVerify licenseVerify = new LicenseVerify("ESBConsole", "publicCert", "soa@1234",
                "D:\\licenseTest\\license.lic", "D:\\licenseTest\\publicCerts.keystore");
        licenseVerify.installLicense();
        System.out.println("licese是否有效：" + licenseVerify.verify());
    }

}
