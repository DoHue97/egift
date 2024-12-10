package com.crm.egift.model;

public class PurchaseContentResponse implements Comparable<PurchaseContentResponse>{
    private AccountResponse account = new AccountResponse();// khong dung nua
    private ContactResponse contact = new ContactResponse();
    private OrganisationResponse organisation = new OrganisationResponse();
    private ClassificationResponse classification = new ClassificationResponse();
    private TransactionResponse transaction_amounts = new TransactionResponse();
    private String id;
    private String reference_number;
    private String state;
    private Double date;
    //
    public static final String STATUS_VOIDED = "VOIDED";
    public static final String STATUS_POSTED = "POSTED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public PurchaseContentResponse() {
    }

    public PurchaseContentResponse(String id, ContactResponse contact, String reference_number, String state, TransactionResponse transaction_amounts, Double date) {
        this.id = id;
        this.contact = contact;
        this.reference_number = reference_number;
        this.state = state;
        this.transaction_amounts = transaction_amounts;
        this.date = date;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }

    public ContactResponse getContact() {
        return contact;
    }

    public void setContact(ContactResponse contact) {
        this.contact = contact;
    }

    public OrganisationResponse getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationResponse organisation) {
        this.organisation = organisation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;

    }


    public ClassificationResponse getClassification() {
        return classification;
    }

    public void setClassification(ClassificationResponse classification) {
        this.classification = classification;
    }

    public TransactionResponse getTransaction_amounts() {
        return transaction_amounts;
    }

    public void setTransaction_amounts(TransactionResponse transaction_amounts) {
        this.transaction_amounts = transaction_amounts;
    }

    public Double getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = date;
    }

    //
    @Override
    public int compareTo(PurchaseContentResponse purchaseContentResponse) {
        if (getDate() == null || purchaseContentResponse.getDate() == null) {
            return 0;
        }
        return purchaseContentResponse.getDate().compareTo(getDate());
    }


}
