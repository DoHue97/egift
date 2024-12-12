package com.crm.egift.model;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String code;
    private String id;
    private Wallet wallet;
    private String email_address;
    private Phone phone;

    public Customer(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public static class Wallet{
        private String code;
        private String id;
        private ArrayList<Balance> balances = new ArrayList<>();

        public Wallet() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<Balance> getBalances() {
            return balances;
        }

        public void setBalances(ArrayList<Balance> balances) {
            this.balances = balances;
        }
    }

    public static class Balance{
        private String type;
        private Double total;
        private String currency_code;

        public Balance() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }
    }
    public static class Phone{
        private String number;
        public Phone() {
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
