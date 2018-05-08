package com.CS100MessagingApp;

public class UserModel
{
    boolean isSelected;
    String userName;

    //Create Constructor and Getter setter method using shortcut Alt+Insert for Windows version


    public UserModel(boolean isSelected, String userName) {
        this.isSelected = isSelected;
        this.userName = userName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
