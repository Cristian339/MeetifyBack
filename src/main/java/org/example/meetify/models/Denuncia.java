package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "denuncia", schema="meetify")
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "denuncia_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

   @ManyToOne
    @JoinColumn(name = "usuario_reportado_id")
    private Usuario usuarioReportado;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}
