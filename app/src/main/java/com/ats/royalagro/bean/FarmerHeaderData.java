package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 11/12/17.
 */

public class FarmerHeaderData {

    private Boolean error;
    private String message;
    private Integer fId;
    private Integer tempId;
    private Integer socId;
    private String fFname;
    private String fMname;
    private String fSname;
    private String fName;
    private Integer fGender;
    private String fDob;
    private String fFarmname;
    private String fMobile;
    private String fMobile2;
    private String fLandline;
    private String fEmail;
    private String fAddress;
    private String fAtpost;
    private String fAreaTotal;
    private Integer fAcctype;
    private String fAccname;
    private String fOwnerRel;
    private String fAccAadhar;
    private String fOtherMobile;
    private String fOtherEmail;
    private String fBankName;
    private String fBankBranch;
    private String fIfsc;
    private String fAccno;
    private String fFieldmapPic;
    private Integer fCurrStatus;
    private Integer fRegDocNo;
    private String fRegDocDate;
    private Integer fReg;
    private Integer enterBy;
    private String enterDatetime;
    private Integer isUsed;
    private String fProfilePic;

    public FarmerHeaderData(Integer fId, Integer tempId, Integer socId, String fFname, String fMname, String fSname, String fName, Integer fGender, String fDob, String fFarmname, String fMobile, String fMobile2, String fLandline, String fEmail, String fAddress, String fAtpost, String fAreaTotal, Integer enterBy, String enterDatetime, Integer isUsed) {
        this.fId = fId;
        this.tempId = tempId;
        this.socId = socId;
        this.fFname = fFname;
        this.fMname = fMname;
        this.fSname = fSname;
        this.fName = fName;
        this.fGender = fGender;
        this.fDob = fDob;
        this.fFarmname = fFarmname;
        this.fMobile = fMobile;
        this.fMobile2 = fMobile2;
        this.fLandline = fLandline;
        this.fEmail = fEmail;
        this.fAddress = fAddress;
        this.fAtpost = fAtpost;
        this.fAreaTotal = fAreaTotal;
        this.enterBy = enterBy;
        this.enterDatetime = enterDatetime;
        this.isUsed = isUsed;

    }

    public FarmerHeaderData(Integer fId, Integer tempId, Integer socId, String fFname, String fMname, String fSname, String fName, Integer fGender, String fDob, String fFarmname, String fMobile, String fMobile2, String fLandline, String fEmail, String fAddress, String fAtpost, String fAreaTotal, Integer fAcctype, String fAccname, String fOwnerRel, String fAccAadhar, String fOtherMobile, String fOtherEmail, String fBankName, String fBankBranch, String fIfsc, String fAccno, String fFieldmapPic, Integer fCurrStatus, Integer fRegDocNo, String fRegDocDate, Integer fReg, Integer enterBy, String enterDatetime, Integer isUsed,String fProfilePic) {
        this.fId = fId;
        this.tempId = tempId;
        this.socId = socId;
        this.fFname = fFname;
        this.fMname = fMname;
        this.fSname = fSname;
        this.fName = fName;
        this.fGender = fGender;
        this.fDob = fDob;
        this.fFarmname = fFarmname;
        this.fMobile = fMobile;
        this.fMobile2 = fMobile2;
        this.fLandline = fLandline;
        this.fEmail = fEmail;
        this.fAddress = fAddress;
        this.fAtpost = fAtpost;
        this.fAreaTotal = fAreaTotal;
        this.fAcctype = fAcctype;
        this.fAccname = fAccname;
        this.fOwnerRel = fOwnerRel;
        this.fAccAadhar = fAccAadhar;
        this.fOtherMobile = fOtherMobile;
        this.fOtherEmail = fOtherEmail;
        this.fBankName = fBankName;
        this.fBankBranch = fBankBranch;
        this.fIfsc = fIfsc;
        this.fAccno = fAccno;
        this.fFieldmapPic = fFieldmapPic;
        this.fCurrStatus = fCurrStatus;
        this.fRegDocNo = fRegDocNo;
        this.fRegDocDate = fRegDocDate;
        this.fReg = fReg;
        this.enterBy = enterBy;
        this.enterDatetime = enterDatetime;
        this.isUsed = isUsed;
        this.fProfilePic=fProfilePic;
    }

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

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public Integer getSocId() {
        return socId;
    }

    public void setSocId(Integer socId) {
        this.socId = socId;
    }

    public String getfFname() {
        return fFname;
    }

    public void setfFname(String fFname) {
        this.fFname = fFname;
    }

    public String getfMname() {
        return fMname;
    }

    public void setfMname(String fMname) {
        this.fMname = fMname;
    }

    public String getfSname() {
        return fSname;
    }

    public void setfSname(String fSname) {
        this.fSname = fSname;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Integer getfGender() {
        return fGender;
    }

    public void setfGender(Integer fGender) {
        this.fGender = fGender;
    }

    public String getfDob() {
        return fDob;
    }

    public void setfDob(String fDob) {
        this.fDob = fDob;
    }

    public String getfFarmname() {
        return fFarmname;
    }

    public void setfFarmname(String fFarmname) {
        this.fFarmname = fFarmname;
    }

    public String getfMobile() {
        return fMobile;
    }

    public void setfMobile(String fMobile) {
        this.fMobile = fMobile;
    }

    public String getfMobile2() {
        return fMobile2;
    }

    public void setfMobile2(String fMobile2) {
        this.fMobile2 = fMobile2;
    }

    public String getfLandline() {
        return fLandline;
    }

    public void setfLandline(String fLandline) {
        this.fLandline = fLandline;
    }

    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public String getfAddress() {
        return fAddress;
    }

    public void setfAddress(String fAddress) {
        this.fAddress = fAddress;
    }

    public String getfAtpost() {
        return fAtpost;
    }

    public void setfAtpost(String fAtpost) {
        this.fAtpost = fAtpost;
    }

    public String getfAreaTotal() {
        return fAreaTotal;
    }

    public void setfAreaTotal(String fAreaTotal) {
        this.fAreaTotal = fAreaTotal;
    }

    public Integer getfAcctype() {
        return fAcctype;
    }

    public void setfAcctype(Integer fAcctype) {
        this.fAcctype = fAcctype;
    }

    public String getfAccname() {
        return fAccname;
    }

    public void setfAccname(String fAccname) {
        this.fAccname = fAccname;
    }

    public String getfOwnerRel() {
        return fOwnerRel;
    }

    public void setfOwnerRel(String fOwnerRel) {
        this.fOwnerRel = fOwnerRel;
    }

    public String getfAccAadhar() {
        return fAccAadhar;
    }

    public void setfAccAadhar(String fAccAadhar) {
        this.fAccAadhar = fAccAadhar;
    }

    public String getfOtherMobile() {
        return fOtherMobile;
    }

    public void setfOtherMobile(String fOtherMobile) {
        this.fOtherMobile = fOtherMobile;
    }

    public String getfOtherEmail() {
        return fOtherEmail;
    }

    public void setfOtherEmail(String fOtherEmail) {
        this.fOtherEmail = fOtherEmail;
    }

    public String getfBankName() {
        return fBankName;
    }

    public void setfBankName(String fBankName) {
        this.fBankName = fBankName;
    }

    public String getfBankBranch() {
        return fBankBranch;
    }

    public void setfBankBranch(String fBankBranch) {
        this.fBankBranch = fBankBranch;
    }

    public String getfIfsc() {
        return fIfsc;
    }

    public void setfIfsc(String fIfsc) {
        this.fIfsc = fIfsc;
    }

    public String getfAccno() {
        return fAccno;
    }

    public void setfAccno(String fAccno) {
        this.fAccno = fAccno;
    }

    public String getfFieldmapPic() {
        return fFieldmapPic;
    }

    public void setfFieldmapPic(String fFieldmapPic) {
        this.fFieldmapPic = fFieldmapPic;
    }

    public Integer getfCurrStatus() {
        return fCurrStatus;
    }

    public void setfCurrStatus(Integer fCurrStatus) {
        this.fCurrStatus = fCurrStatus;
    }

    public Integer getfRegDocNo() {
        return fRegDocNo;
    }

    public void setfRegDocNo(Integer fRegDocNo) {
        this.fRegDocNo = fRegDocNo;
    }

    public String getfRegDocDate() {
        return fRegDocDate;
    }

    public void setfRegDocDate(String fRegDocDate) {
        this.fRegDocDate = fRegDocDate;
    }

    public Integer getfReg() {
        return fReg;
    }

    public void setfReg(Integer fReg) {
        this.fReg = fReg;
    }

    public Integer getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(Integer enterBy) {
        this.enterBy = enterBy;
    }

    public String getEnterDatetime() {
        return enterDatetime;
    }

    public void setEnterDatetime(String enterDatetime) {
        this.enterDatetime = enterDatetime;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getfProfilePic() {
        return fProfilePic;
    }

    public void setfProfilePic(String fProfilePic) {
        this.fProfilePic = fProfilePic;
    }

    @Override
    public String toString() {
        return "FarmerHeaderData{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", fId=" + fId +
                ", tempId=" + tempId +
                ", socId=" + socId +
                ", fFname='" + fFname + '\'' +
                ", fMname='" + fMname + '\'' +
                ", fSname='" + fSname + '\'' +
                ", fName='" + fName + '\'' +
                ", fGender=" + fGender +
                ", fDob='" + fDob + '\'' +
                ", fFarmname='" + fFarmname + '\'' +
                ", fMobile='" + fMobile + '\'' +
                ", fMobile2='" + fMobile2 + '\'' +
                ", fLandline='" + fLandline + '\'' +
                ", fEmail='" + fEmail + '\'' +
                ", fAddress='" + fAddress + '\'' +
                ", fAtpost='" + fAtpost + '\'' +
                ", fAreaTotal=" + fAreaTotal +
                ", fAcctype=" + fAcctype +
                ", fAccname='" + fAccname + '\'' +
                ", fOwnerRel='" + fOwnerRel + '\'' +
                ", fAccAadhar='" + fAccAadhar + '\'' +
                ", fOtherMobile='" + fOtherMobile + '\'' +
                ", fOtherEmail='" + fOtherEmail + '\'' +
                ", fBankName='" + fBankName + '\'' +
                ", fBankBranch='" + fBankBranch + '\'' +
                ", fIfsc='" + fIfsc + '\'' +
                ", fAccno='" + fAccno + '\'' +
                ", fFieldmapPic='" + fFieldmapPic + '\'' +
                ", fCurrStatus=" + fCurrStatus +
                ", fRegDocNo=" + fRegDocNo +
                ", fRegDocDate='" + fRegDocDate + '\'' +
                ", fReg=" + fReg +
                ", enterBy=" + enterBy +
                ", enterDatetime='" + enterDatetime + '\'' +
                ", isUsed=" + isUsed +
                ", fProfilePic='" + fProfilePic + '\'' +
                '}';
    }
}
