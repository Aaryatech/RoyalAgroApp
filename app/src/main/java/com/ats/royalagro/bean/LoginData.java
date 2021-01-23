package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 8/12/17.
 */

public class LoginData {

    private Boolean error;
    private String message;
    private Integer empId;
    private Integer empCode;
    private String empName;
    private String empMobile1;
    private String empMobile2;
    private String empEmail;
    private String empAdd;
    private Integer empGender;
    private Integer isOwnVehicle;
    private String empDept;
    private Integer empType;
    private Integer isUsed;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getEmpCode() {
        return empCode;
    }

    public void setEmpCode(Integer empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpMobile1() {
        return empMobile1;
    }

    public void setEmpMobile1(String empMobile1) {
        this.empMobile1 = empMobile1;
    }

    public String getEmpMobile2() {
        return empMobile2;
    }

    public void setEmpMobile2(String empMobile2) {
        this.empMobile2 = empMobile2;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpAdd() {
        return empAdd;
    }

    public void setEmpAdd(String empAdd) {
        this.empAdd = empAdd;
    }

    public Integer getEmpGender() {
        return empGender;
    }

    public void setEmpGender(Integer empGender) {
        this.empGender = empGender;
    }

    public Integer getIsOwnVehicle() {
        return isOwnVehicle;
    }

    public void setIsOwnVehicle(Integer isOwnVehicle) {
        this.isOwnVehicle = isOwnVehicle;
    }

    public String getEmpDept() {
        return empDept;
    }

    public void setEmpDept(String empDept) {
        this.empDept = empDept;
    }

    public Integer getEmpType() {
        return empType;
    }

    public void setEmpType(Integer empType) {
        this.empType = empType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", empId=" + empId +
                ", empCode=" + empCode +
                ", empName='" + empName + '\'' +
                ", empMobile1='" + empMobile1 + '\'' +
                ", empMobile2='" + empMobile2 + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empAdd='" + empAdd + '\'' +
                ", empGender=" + empGender +
                ", isOwnVehicle=" + isOwnVehicle +
                ", empDept='" + empDept + '\'' +
                ", empType=" + empType +
                ", isUsed=" + isUsed +
                '}';
    }
}
