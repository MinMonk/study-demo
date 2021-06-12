package com.monk.license.cache;

import java.util.Random;

public class LicenseCache {
    
    private static LicenseInfo licenseInfo;

    private static LicenseCache licenseCache;

    private static long ttlTime;

    public long getTtlTime() {
        return ttlTime;
    }

    private LicenseCache() {
        if (null == licenseInfo) {
            buildLicenseInfo();
        }
    }

    private static synchronized LicenseInfo buildLicenseInfo() {
        if (null == licenseInfo) {
            licenseInfo = new LicenseInfo();
        }
        ttlTime = licenseInfo.getValidateTime() + getRandomExpireTime();
        return licenseInfo;
    }

    private static synchronized LicenseCache buildLicenseCache() {
        if (null == licenseCache) {
            licenseCache = new LicenseCache();
        }
        return licenseCache;
    }

    private static long getRandomExpireTime() {
        Random random = new Random();
        /**
         * 失效时间在10-24内随机生成
         */
        int randHour = random.nextInt(24 - 10 + 1) + 10;
        return randHour * 60 * 60 * 1000L;
    }

    public static LicenseCache getInstance() {
        if (null == licenseCache) {
            buildLicenseCache();
        }

        return licenseCache;
    }

    public synchronized LicenseInfo getLicenseInfo() {
        if (ttlTime == -1L) {
            return licenseInfo;
        }

        if (System.currentTimeMillis() > ttlTime || System.currentTimeMillis() < licenseInfo.getValidateTime()) {
            licenseInfo.setStatu(null);
        }
        return licenseInfo;
    }

    public synchronized void setLicenseStatus(LicenseStatu statu) {
        licenseInfo.setStatu(statu);
        licenseInfo.setValidateTime(System.currentTimeMillis());
        if (LicenseStatu.LOCK.equals(statu)) {
            // 永不失效
            ttlTime = -1L;
        } else {
            ttlTime = licenseInfo.getValidateTime() + getRandomExpireTime();
        }
    }
    
    public synchronized static void clearCache() {
        licenseCache = null;
        licenseInfo = null;
    }

    @Override
    public String toString() {
        return "LicenseCache [ttlTime=" + ttlTime + ", " + licenseInfo + "]";
    }
}
