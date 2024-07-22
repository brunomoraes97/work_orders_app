package com.xptoinfo.workorders;

public class Client {

    private int id;
    private String name;
    private int[] orders;
    private String phone;
    private String address;
    private String email;

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name, String phone, String address, String email) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] orders() {
        return orders;
    }

}
