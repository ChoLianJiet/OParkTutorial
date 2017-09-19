package com.opark.opark.model;

/**
 * Created by Yz on 08-Sep-17.
 */

public class Address {

    public String firstline;
    public String secondline;
    public String postcode;
    public String city;
    public String countryState;



public Address() {
        // basic constructor
        }

        public Address (String firstline, String secondline, String city, String postcode, String countryState){
            this.firstline = firstline;
            this.secondline = secondline;
            this.postcode = postcode ;
            this.city = city;
            this.countryState = countryState;


        }


    public String getFirstline() {
        return firstline;
    }

    public void setFirstline(String firstline) {
        this.firstline = firstline;
    }

    public String getSecondline() {
        return secondline;
    }

    public void setSecondline(String secondline) {
        this.secondline = secondline;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryState() {
        return countryState;
    }

    public void setCountryState(String state) {
        this.countryState = state;
    }
}
