package com.springboot.aldiabackjava.models.expensesAndIncomesModels;

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
@Table(name="incomes")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_income")
    private int idIncome;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonIgnore
    private User user;
    Date date;
    int amount;
    String description;
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id_category")
    CategoryIncomes category;
    String picture;
}
