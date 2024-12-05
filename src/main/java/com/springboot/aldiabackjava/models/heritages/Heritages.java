package com.springboot.aldiabackjava.models.heritages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.aldiabackjava.models.userModels.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="heritages")
public class Heritages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_heritage")
    int idHeritage;
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonIgnore
    private User user;
    @Column(name="acquisition_value")
    private int acquisitionValue;
    @Column(name="curren_value")
    private int currenValue;
    @Column(name="acquisition_date")
    Date acquisitionDate;
    String description;
    @ManyToOne
    @JoinColumn(name = "type_heritages", referencedColumnName = "id_type_heritage")
    TypeHeritages typeHeritages;
}
