package Entities;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(name = "puntaje_total")
    private Integer puntajeTotal;

    @Column(name = "pais")
    private String pais;

    @Column(name = "genero")
    private String genero;

    @Column(name = "biografia")
    private String biografia;

    @Column(name = "privado", nullable = false)
    private Boolean privado;

    @Column(name = "baneado", nullable = false)
    private Boolean baneado;
}
