package com.monk.license.cache;

public enum LicenseStatu {
    
    /**
     * 有效
     */
    EFFECTIVE,
    /**
     * 无效
     */
    INVALID,
    /**
     * 锁定(尝试绕过验证被发现后的锁定状态)
     */
    LOCK
}
