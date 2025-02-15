package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "reputacion", schema="meetify")
public class Reputacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reputacion_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Perfil perfil;

    @Column(name = "estrellas", nullable = false)
    private Integer estrellas;

    @Column(name = "motivo", nullable = true)
    private String motivo;
}