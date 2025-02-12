/*
// Conversacion.java
package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "conversacion", schema="meetify")
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversacion_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Perfil usuario1;

    @OneToMany(mappedBy = "conversacion")
    private List<Mensaje> mensajes;
}*/
