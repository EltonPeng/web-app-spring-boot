package com.zijian.java.web.spring.webapp.ui.model.response;

import org.springframework.hateoas.RepresentationModel;

public class AddressRest extends RepresentationModel<AddressRest> {
    private String addressId;
    private String city;
    private String street;
    private String type;
    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
