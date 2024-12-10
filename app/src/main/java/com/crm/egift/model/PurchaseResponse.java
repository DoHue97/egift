package com.crm.egift.model;

import java.util.ArrayList;

public class PurchaseResponse {
    private PagingResponse paging = new PagingResponse();
    private ArrayList<PurchaseContentResponse> content = new ArrayList<>();

    public PagingResponse getPaging() {
        return paging;
    }

    public void setPaging(PagingResponse paging) {
        this.paging = paging;
    }

    public ArrayList<PurchaseContentResponse> getContent() {
        return content;
    }

    public void setContent(ArrayList<PurchaseContentResponse> content) {
        this.content = content;
    }
}
