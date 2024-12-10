package com.crm.egift.model;

public class Organisation {
    String name;
    String id;
    String type;
    String status;
    public Organisation(){}
    public Organisation(String name, String id, String type, String status){
        this.name = name;
        this.id = id;
        this.type = type;
        this.status = status;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
