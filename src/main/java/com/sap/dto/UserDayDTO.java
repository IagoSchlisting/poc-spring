package com.sap.dto;


public class UserDayDTO {

    private Integer id;

    private String shift;

    private Boolean disponibility;

    public UserDayDTO(){
        this.setDisponibility(true);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Boolean getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(Boolean disponibility) {
        this.disponibility = disponibility;
    }
}
