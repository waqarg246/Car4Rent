package com.example.shahbaz.car4rent;

public class AddCarData {

    String carId,carName,carModel,pricPerKM,carPicurl;

    public AddCarData(String carId, String carName, String carModel, String pricPerKM, String carPicurl) {
        this.carId = carId;
        this.carName = carName;
        this.carModel = carModel;
        this.pricPerKM = pricPerKM;
        this.carPicurl = carPicurl;
    }

    public AddCarData(){


    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getPricPerKM() {
        return pricPerKM;
    }

    public void setPricPerKM(String pricPerKM) {
        this.pricPerKM = pricPerKM;
    }

    public String getCarPicurl() {
        return carPicurl;
    }

    public void setCarPicurl(String carPicurl) {
        this.carPicurl = carPicurl;
    }
}
