package com.example.book.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.service.spi.InjectService;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRole {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public AppUserRole(String name){
        this.name=name;
    }
}
