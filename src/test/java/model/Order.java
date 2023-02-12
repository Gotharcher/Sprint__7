package model;

import java.util.List;

public class Order {
    private String firstName, lastName, address, metroStation, phone, deliveryDate, comment;
    private int rentTime;
    private List<String> color;
    private int track;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public Order(){

    }

    public Order(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.rentTime = rentTime;
    }

    public static Order fakeOrderWithColors(List<String> colors){
        Order order = new Order("Ars", "Che", "SPb", "10", "88009002030", 4, "2023-02-02", "Hello!");
        order.setColor(colors);
        return order;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public int getRentTime() {
        return rentTime;
    }

    public List<String> getColor() {
        return color;
    }
}
