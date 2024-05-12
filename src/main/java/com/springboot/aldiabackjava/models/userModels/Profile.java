package com.springboot.aldiabackjava.models.userModels;

import jakarta.persistence.*;

@Entity
@Table(name="profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_profile")
    private int idPRofile;
    @Column(name="first_name")
    private String firstName;
    @Column(name="middle_name")
    private String middleName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="surname")
    private String surnamen;
    @Column(name="type_document")
    private int typeDocument;
    private String document;
    @Column(name="profile_picture")
    private String profilePicture;
    @Column(name="birth_date")
    private String birthDate;
    private String department;
    private String town;
    private String address;
    @ManyToOne
    @JoinColumn(name="civil_status", referencedColumnName = "id") // Suponiendo que la clave primaria de CivilStatus es "id"
    private CivilStatus civilStatus;
    @Column(name="number_phone")
    private String numberPhone;
    private String email;
    private String occupation;
    @Column(name="data_treatment")
    private Boolean dataTreatment;
    private int exogenous;


    public Profile(int idPRofile, String firstName, String middleName, String lastName, String surnamen, int typeDocument, String document, String profilePicture, String birthDate, String department, String town, String address, CivilStatus civilStatus, String numberPhone, String email, String occupation, Boolean dataTreatment, int exogenous) {
        this.idPRofile = idPRofile;
        this.firstName = firstName;
        this.middleName = middleName;
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
        this.email = email;
        this.occupation = occupation;
        this.dataTreatment = dataTreatment;
        this.exogenous = exogenous;
    }

    public Profile(String firstName, String middleName, String lastName, String surnamen, int typeDocument, String document, String profilePicture, String birthDate, String department, String town, String address, CivilStatus civilStatus, String numberPhone, String email, String occupation, Boolean dataTreatment, int exogenous) {
        this.firstName = firstName;
        this.middleName = middleName;
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
        this.email = email;
        this.occupation = occupation;
        this.dataTreatment = dataTreatment;
        this.exogenous = exogenous;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public Profile() {
    }

    public long getIdPRofile() {
        return idPRofile;
    }

    public void setIdPRofile(int idPRofile) {
        this.idPRofile = idPRofile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurnamen() {
        return surnamen;
    }

    public void setSurnamen(String surnamen) {
        this.surnamen = surnamen;
    }

    public int getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(int typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Boolean getDataTreatment() {
        return dataTreatment;
    }

    public void setDataTreatment(Boolean dataTreatment) {
        this.dataTreatment = dataTreatment;
    }

    public int getExogenous() {
        return exogenous;
    }

    public void setExogenous(int exogenous) {
        this.exogenous = exogenous;
    }
}
