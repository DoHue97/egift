package com.crm.egift.model;

public class TransactionResponse {
    private Double net;
    private Double tax;
    private Double discount;
    private Double total;

    public TransactionResponse() {
    }

    public TransactionResponse(Double net, Double tax, Double discount, Double total) {
        this.net = net;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
    }

    public Double getNet() {
        return net;
    }

    public void setNet(Double net) {
        this.net = net;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
