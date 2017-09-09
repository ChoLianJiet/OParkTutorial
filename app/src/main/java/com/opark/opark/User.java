package com.opark.opark;

/**
 * Created by Yz on 08-Sep-17.
 */

public class User {

    public NameAndNum userName;
    public Address userAddress;
    public Car userCar;
    public String phoneNum;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(NameAndNum userName, Address userAddress, Car userCar, String phoneNum   ) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userCar = userCar;
        this.phoneNum = phoneNum;
    }


    public NameAndNum getUserName() {
        return userName;
    }

    public void setUserName(NameAndNum userName) {
        this.userName = userName;
    }

    public Address getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address userAddress) {
        this.userAddress = userAddress;
    }

    public Car getUserCar() {
        return userCar;
    }

    public void setUserCar(Car userCar) {
        this.userCar = userCar;
    }
}