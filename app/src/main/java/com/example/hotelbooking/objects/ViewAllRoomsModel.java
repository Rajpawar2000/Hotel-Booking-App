package com.example.hotelbooking.objects;

import java.io.Serializable;

public class ViewAllRoomsModel implements Serializable {

    String name;
    String city;
    String images_url;
    String address;
    String d_facilities;
    String h_facilities;
    String r_facilities;
    String phone;
    String room_type;
    String parent_location;


    double people_adult;
    double people_child;
    double total_available;
    double total_room;
    double price;
    double rating;

    boolean parking;
    boolean wifi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getD_facilities() {
        return d_facilities;
    }

    public void setD_facilities(String d_facilities) {
        this.d_facilities = d_facilities;
    }

    public String getH_facilities() {
        return h_facilities;
    }

    public void setH_facilities(String h_facilities) {
        this.h_facilities = h_facilities;
    }

    public String getR_facilities() {
        return r_facilities;
    }

    public void setR_facilities(String r_facilities) {
        this.r_facilities = r_facilities;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParent_location() {
        return parent_location;
    }

    public void setParent_location(String parent_location) {
        this.parent_location = parent_location;
    }

    public double getPeople_adult() {
        return people_adult;
    }

    public void setPeople_adult(double people_adult) {
        this.people_adult = people_adult;
    }

    public double getPeople_child() {
        return people_child;
    }

    public void setPeople_child(double people_child) {
        this.people_child = people_child;
    }

    public double getTotal_available() {
        return total_available;
    }

    public void setTotal_available(double total_available) {
        this.total_available = total_available;
    }

    public double getTotal_room() {
        return total_room;
    }

    public void setTotal_room(double total_room) {
        this.total_room = total_room;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }
}



