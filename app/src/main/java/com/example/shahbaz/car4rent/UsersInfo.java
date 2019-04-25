package com.example.shahbaz.car4rent;

public class UsersInfo {
    private String id, Name, City, Phone, Email,Password;

    public UsersInfo() {


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public UsersInfo(String id, String name, String city, String phone, String email, String password) {
        this.id = id;
        Name = name;
        City = city;
        Phone = phone;
        Email = email;
        Password = password;
    }
}


