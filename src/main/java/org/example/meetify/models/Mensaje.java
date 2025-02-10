// Mensaje.java
package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "mensaje", schema="meetify")
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mensaje_id")
    private Integer id;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "usuario_emisor_id", nullable = false)
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "usuario_receptor_id", nullable = false)
    private Perfil receptor;

    @ManyToOne
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    @Column(name = "fecha_enviado")
    private LocalDate fechaEnviado;

    @Column(name = "hora_enviado")
    private LocalTime horaEnviado;
}