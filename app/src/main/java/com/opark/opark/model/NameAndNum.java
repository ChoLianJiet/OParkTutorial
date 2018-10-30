package com.opark.opark.model;

/**
 * Created by Yz on 09-Sep-17.
 */

public class NameAndNum {

        public String firstName;
        public String lastName;
        public String phoneNum;
        public String icNumber;

public NameAndNum(){

}

        public NameAndNum(String firstName, String lastName, String phoneNum){
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNum = phoneNum;
            this.icNumber = icNumber;
        }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
}





