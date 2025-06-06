package com.springboot.aldiabackjava.models.userModels;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Entity
@Table(name="profiles", uniqueConstraints = {@UniqueConstraint(columnNames = {"document"})})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_profile")
    private int idPRofile;
    @Column(name="name")
    private String name;
    @Column(name="last_name")
    private String lastName;
    @Column(name="surname",nullable = true)
    private String surnamen;
    @Column(name="type_document")
    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;
    private String document;
    @Column(name="profile_picture")
    private String profilePicture;
    @Column(name="birth_date")
    private String birthDate;
    private String department;
    private String town;
    private String address;
    @Enumerated(EnumType.STRING)
    private CivilStatus civilStatus;
    @Column(name="number_phone")
    private String numberPhone;
    private String occupation;
    @Column(name="data_treatment")
    private Boolean dataTreatment;
    private int exogenous;

    public Profile(int idPRofile, String name, String lastName, String surnamen, TypeDocument typeDocument, String document, String profilePicture, String birthDate, String department, String town, String address, CivilStatus civilStatus, String numberPhone, String occupation, Boolean dataTreatment, int exogenous) {
        this.idPRofile = idPRofile;
        this.name = name;
        this.lastName = lastName;
        this.surnamen = surnamen;
        this.typeDocument = typeDocument;
        this.document = document;
        this.profilePicture = profilePicture;
        this.birthDate = birthDate;
        this.department = department;
        this.town = town;
        this.address = address;
        this.civilStatus = civilStatus;
        this.numberPhone = numberPhone;
        this.occupation = occupation;
        this.dataTreatment = dataTreatment;
        this.exogenous = exogenous;
    }

    public Profile(String firstName, String name, String surnamen, TypeDocument typeDocument, String document, String profilePicture, String birthDate, String department, String town, String address, CivilStatus civilStatus, String numberPhone, String email, String occupation, Boolean dataTreatment, int exogenous) {
        this.name = name;
        this.lastName = lastName;
        this.surnamen = surnamen;
        this.typeDocument = typeDocument;
        this.document = document;
        this.profilePicture = profilePicture;
        this.birthDate = birthDate;
        this.department = department;
        this.town = town;
        this.address = address;
        this.civilStatus = civilStatus;
        this.numberPhone = numberPhone;
        this.occupation = occupation;
        this.dataTreatment = dataTreatment;
        this.exogenous = exogenous;
    }

    public Profile() {

    }
}
