package com.CS100MessagingApp;

public class AllKindsOfChat {
    String GroupName, startdate="", endDate="";
    int flag = 0;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
