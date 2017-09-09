package com.opark.opark;

/**
 * Created by Yz on 08-Sep-17.
 */

public class Car {
    public String carColour;
    public String carBrand;
    public String carModel;
    public String carPlate;


    public Car (String carColour, String carBrand, String carModel, String carPlate){
       this.carBrand = carBrand;
        this.carColour = carColour;
        this.carModel = carModel;
        this.carPlate= carPlate;
    }


    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }
}
