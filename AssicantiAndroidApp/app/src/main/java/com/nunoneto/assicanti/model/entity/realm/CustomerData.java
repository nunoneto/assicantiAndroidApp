package com.nunoneto.assicanti.model.entity.realm;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by nb20301 on 22/07/2016.
 */
public class CustomerData extends RealmObject {

    private String name, contact, comment, nif, address, companyCode, email;
    private Date insertedAt;

    public CustomerData() {    }

    public CustomerData(String name, String contact, String comment, String nif, String address, String companyCode, String email, Date insertedAt) {
        this.name = name;
        this.contact = contact;
        this.comment = comment;
        this.nif = nif;
        this.address = address;
        this.companyCode = companyCode;
        this.email = email;
        this.insertedAt = insertedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;
    }
}
