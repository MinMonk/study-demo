package com.monk.license.cache;

public class LicenseInfo {
    private LicenseStatu statu = LicenseStatu.INVALID;

    private long validateTime = System.currentTimeMillis();
    
    private long exipreDate;

    public LicenseStatu getStatu() {
        return statu;
    }

    public void setStatu(LicenseStatu statu) {
        this.statu = statu;
    }

    public long getValidateTime() {
        return validateTime;
    }

    public void setValidateTime(long validateTime) {
        this.validateTime = validateTime;
    }

    public long getExipreDate() {
        return exipreDate;
    }

    public void setExipreDate(long exipreDate) {
        this.exipreDate = exipreDate;
    }

    @Override
    public String toString() {
        return "LicenseInfo [statu=" + statu + ", validateTime=" + validateTime + ", exipreDate=" + exipreDate
                + "]";
    }

}
