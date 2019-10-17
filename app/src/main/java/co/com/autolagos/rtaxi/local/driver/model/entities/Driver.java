package co.com.autolagos.rtaxi.local.driver.model.entities;

import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("uid")
    private String userId;
    private String fullName;
    private String departmentCode;
    private String departmentName;
    private String limitDateRTM;
    private String companyName;
    private String limitDateLicense;
    private String categoryLicense;
    private String qualification;
    private String token;

    public Driver() {}
    public Driver(String userId, String fullName, String departmentCode, String departmentName, String limitDateRTM, String companyName, String limitDateLicense, String categoryLicense, String qualification, String token) {
        this.userId = userId;
        this.fullName = fullName;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.limitDateRTM = limitDateRTM;
        this.companyName = companyName;
        this.limitDateLicense = limitDateLicense;
        this.categoryLicense = categoryLicense;
        this.qualification = qualification;
        this.token = token;
    }

    //region Getters
    public String getUserId() {
        return userId;
    }
    public String getFullName() {
        return fullName;
    }
    public String getDepartmentCode() {
        return departmentCode;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public String getLimitDateRTM() {
        return limitDateRTM;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getLimitDateLicense() {
        return limitDateLicense;
    }
    public String getCategoryLicense() {
        return categoryLicense;
    }
    public String getQualification() {
        return qualification;
    }
    public String getToken() {
        return token;
    }
    //endregion

    //region Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public void setLimitDateRTM(String limitDateRTM) {
        this.limitDateRTM = limitDateRTM;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setLimitDateLicense(String limitDateLicense) {
        this.limitDateLicense = limitDateLicense;
    }
    public void setCategoryLicense(String categoryLicense) {
        this.categoryLicense = categoryLicense;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public void setToken(String token) {
        this.token = token;
    }
    //endregion

    @Override
    public String toString() {
        return "Driver{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", limitDateRTM='" + limitDateRTM + '\'' +
                ", companyName='" + companyName + '\'' +
                ", limitDateLicense='" + limitDateLicense + '\'' +
                ", categoryLicense='" + categoryLicense + '\'' +
                ", qualification='" + qualification + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
