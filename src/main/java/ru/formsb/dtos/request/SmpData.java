package ru.formsb.dtos.request;

import lombok.Data;

@Data
public class SmpData {
    private String orgFullName;

    private String legalEntityIndex;
    private String legalEntityCity;
    private String legalEntityStreet;
    private String legalEntityBuilding;
    private String legalEntityCorp;

    private String postIndex;
    private String postCity;
    private String postStreet;
    private String postBuilding;
    private String postCorp;
}
