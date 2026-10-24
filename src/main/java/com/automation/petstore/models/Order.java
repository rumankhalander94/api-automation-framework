package com.automation.petstore.models;

public class Order {

    private Long id;
    private Long petId;
    private Integer quantity;
    private String shipDate;
    private Boolean complete;
    private String status;

    public Order() {
    }

    public Order(Long id, Long petId, Integer quantity, String shipDate, Boolean complete, String status ) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.complete = complete;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId(){
        return petId;
    }

    public void setPetId(Long petId){
        this.petId = petId;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public String getShipDate(){
        return shipDate;
    }

    public void setShipDate(String shipDate){
        this.shipDate = shipDate;
    }

    public Boolean getComplete(){
        return complete;
    }

    public void setComplete(Boolean complete){
        this.complete = complete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
