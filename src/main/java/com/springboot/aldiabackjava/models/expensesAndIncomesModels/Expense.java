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
@Table(name = "expenses")
public class Expense {
    @Id
    @Column( name ="id_expense")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonIgnore
    private User user;
    Date date;
    int amount;
    String description;
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id_category")
    CategoryExpenses category;
    String picture;

}
