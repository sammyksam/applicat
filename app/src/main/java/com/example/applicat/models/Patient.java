package com.example.applicat.models;

public class Patient {
    int Age;
    String Residence, Type, Gender;

    public Patient( String age, String type, String residence, String gender) {
    }

    public Patient(int age, String residence, String type, String gender) {
        Age = age;
        Residence = residence;
        Type = type;
        Gender = gender;

    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getResidence() {
        return Residence;
    }

    public void setResidence(String residence) {
        Residence = residence;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

}