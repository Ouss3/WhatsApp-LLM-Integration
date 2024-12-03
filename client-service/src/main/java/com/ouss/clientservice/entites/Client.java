package com.ouss.clientservice.entites;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Data@AllArgsConstructor@NoArgsConstructor@ToString@Builder
public class Client {
    @Id@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer idClient;
    private String nom;

    private String prenom;
    private String email;
    private String password;
    private String token;
    @Enumerated(EnumType.STRING)
    private List<Role> roles;
}
