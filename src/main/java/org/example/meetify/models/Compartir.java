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
@Table(name = "compartir", schema = "meetify")
public class Compartir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compartir_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", referencedColumnName = "perfil_id", nullable = false)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", referencedColumnName = "publicacion_id", nullable = false)
    private Publicacion publicacion;
}
