package org.example.meetify.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.meetify.Enum.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "usuario", schema="meetify")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(name = "rol")
    @Enumerated(EnumType.ORDINAL)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.rol.name()));
    }

    @Override
    public String getPassword() {
        return this.contrasenia;
    }

    @Override
    public String getUsername() {
        return this.nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setCuentaVerificada(boolean b) {
    }

    public boolean isCuentaVerificada() {
        return false;
    }
}