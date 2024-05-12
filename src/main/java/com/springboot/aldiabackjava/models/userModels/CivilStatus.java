package com.springboot.aldiabackjava.models.userModels;

import jakarta.persistence.*;

@Entity
@Table(name="civil_status")
public class CivilStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name_status")
    private String nameStatus;

    public CivilStatus(int id, String nameStatus) {
        this.id = id;
        this.nameStatus = nameStatus;
    }

    public CivilStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public CivilStatus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }
}
