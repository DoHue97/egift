package com.crm.egift.model;

import java.util.ArrayList;

public class Outlet {
    private String id;
    private String name;
    private String state;
    private ArrayList<ContactInfo> contact_info = new ArrayList<>();
    private ArrayList<Address> addresses = new ArrayList<>();
    public Outlet(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<ContactInfo> getContact_info() {
        return contact_info;
    }

    public void setContact_info(ArrayList<ContactInfo> contact_info) {
        this.contact_info = contact_info;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return name;
    }

    public static class ContactInfo{
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Address{
        private String address_line_1;
        private String address_line_2;
        private String town_city;
        private String postal_code;

        public String getAddress_line_1() {
            return address_line_1;
        }

        public void setAddress_line_1(String address_line_1) {
            this.address_line_1 = address_line_1;
        }

        public String getAddress_line_2() {
            return address_line_2;
        }

        public void setAddress_line_2(String address_line_2) {
            this.address_line_2 = address_line_2;
        }

        public String getTown_city() {
            return town_city;
        }

        public void setTown_city(String town_city) {
            this.town_city = town_city;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(String postal_code) {
            this.postal_code = postal_code;
        }
    }
}
