package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.meetify.Enum.Genero;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "perfil", schema="meetify")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(name = "puntaje_total", nullable = true)
    private Integer puntajeTotal;

    @Column(name = "fecha_nacimiento", nullable = true)
    private LocalDate fechaNacimiento;

    @Column(name = "pais", nullable = true)
    private String pais;

    @Column(name = "genero", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private Genero genero;

    @Column(name = "biografia", nullable = true)
    private String biografia;

    @Column(name = "privado", nullable = true)
    private Boolean privado;

    @Column(name = "baneado", nullable = true)
    private Boolean baneado;

    @Column(name = "imagen_url", nullable = true)
    private String imagenUrl;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, targetEntity = Perfil.class)
    @JoinTable(
            name = "seguidores" ,schema="meetify",
            joinColumns = @JoinColumn(name = "seguidor_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "seguido_id", nullable = false)
    )
    private List<Perfil> seguidos = new ArrayList<>(0);

    // Relación inversa para obtener a los seguidores
    @ManyToMany(mappedBy = "seguidos", fetch = FetchType.LAZY)
    private List<Perfil> seguidores = new ArrayList<>();
}
