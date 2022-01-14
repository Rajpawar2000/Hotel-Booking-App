package com.example.hotelbooking.objects;

public class ExploreCitiesModel {
    String city;
    String cityImg_url;

    public ExploreCitiesModel(){ }

    public ExploreCitiesModel(String city) {
        this.city = city;
    }

    public ExploreCitiesModel(String city, String cityImg_url) {
        this.city = city;
        this.cityImg_url = cityImg_url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityImg_url() {
        return cityImg_url;
    }

    public void setCityImg_url(String cityImg_url) {
        this.cityImg_url = cityImg_url ;
    }


}
