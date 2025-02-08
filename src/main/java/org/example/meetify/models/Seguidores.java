package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.meetify.Enum.Estado;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "seguidores", schema="meetify")
public class Seguidores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Perfil seguidor;

    @ManyToOne
    @JoinColumn(name = "seguido_id", nullable = false)
    private Perfil seguido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;
}