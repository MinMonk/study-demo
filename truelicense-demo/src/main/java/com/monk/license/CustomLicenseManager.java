package com.monk.license;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.license.cache.LicenseCache;
import com.monk.license.cache.LicenseInfo;
import com.monk.license.cache.LicenseStatu;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import de.schlichtherle.xml.XMLConstants;

/**
 * 自定义LicenseManager，用于增加额外的信息校验(除了LicenseManager的校验，我们还可以在这个类里面添加额外的校验信息)
 */
public class CustomLicenseManager extends LicenseManager {

    private static Logger logger = LoggerFactory.getLogger(CustomLicenseManager.class);

    private static final String LOCK_FILE_NAME = ".lock_csb";

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 复写create方法
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 复写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setLicenseKey(key);
        setCertificate(certificate);

        return content;
    }

    /**
     * 复写verify方法，调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {

        // Load license key from preferences,
        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }

        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setCertificate(certificate);

        return content;
    }

    /**
     * 校验生成证书的参数信息
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {
        //final LicenseParam param = getLicenseParam();
        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }

    /**
     * 复写validate方法，用于增加我们额外的校验信息
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        LicenseCache licenseCache = LicenseCache.getInstance();
        LicenseInfo licenseInfo = licenseCache.getLicenseInfo();
        long currTime = System.currentTimeMillis();

        if (LicenseStatu.LOCK.equals(licenseInfo.getStatu())) {
            return;
        }

        if (currTime < licenseInfo.getValidateTime()) {
            // 如果当前时间在最后一次校验时间之前，说明是手动篡改了系统时间，那么就将缓存中的状态设置为Lock，并生成Lock文件
            licenseCache.setLicenseStatus(LicenseStatu.LOCK);
            createLockFile();
        } else {
            if (checkLockFileExists()) {
                licenseCache.setLicenseStatus(LicenseStatu.LOCK);
            } else {
                boolean validateResult = false;
                try {
                    // 1. 首先调用父类的validate方法
                    super.validate(content);
                    // 2. 然后校验自定义的License参数，去校验我们的license信息
                    // LicenseExtraModel expectedCheckModel = (LicenseExtraModel) content.getExtra();
                    // 做我们自定义的校验
                    
                    validateResult = true;
                } catch (LicenseContentException e) {
                    logger.error(e.getMessage(), e);
                    throw e;
                } finally {
                    if (validateResult) {
                        licenseInfo.setExipreDate(content.getNotAfter().getTime());
                        licenseCache.setLicenseStatus(LicenseStatu.EFFECTIVE);
                    } else {
                        licenseCache.setLicenseStatus(LicenseStatu.INVALID);
                    }
                }
            }
        }
    }

    /**
     * 创建锁文件
     * 
     * @author Monk
     * @date 2021年5月22日 下午4:03:10
     */
    private void createLockFile() {
        try {
            String homePath = System.getProperty("user.home");
            File file = new File(homePath + File.separator + LOCK_FILE_NAME);
            file.createNewFile();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 检查锁文件是否存在
     * 
     * @return 锁文件是否存在
     * @author Monk
     * @date 2021年5月22日 下午4:03:26
     */
    private boolean checkLockFileExists() {
        String homePath = System.getProperty("user.home");
        File file = new File(homePath + File.separator + LOCK_FILE_NAME);
        return file.exists();
    }

    /**
     * 重写XMLDecoder解析XML
     */
    private Object load(String encoded) {
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(
                    new ByteArrayInputStream(encoded.getBytes(XMLConstants.XML_CHARSET)));
            decoder = new XMLDecoder(new BufferedInputStream(inputStream, XMLConstants.DEFAULT_BUFSIZE), null,
                    null);
            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (decoder != null) {
                    decoder.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("XMLDecoder解析XML失败", e);
            }
        }

        return null;
    }

}
