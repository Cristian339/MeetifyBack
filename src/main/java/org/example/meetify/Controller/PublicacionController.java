package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.*;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.Services.*;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Reputacion;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.example.meetify.seguridad.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    private final PerfilService perfilService;
    private PublicacionService service;

    private PublicacionRepository repository;

    private UsuarioService usuarioService;

    private ReputacionService reputacionService;

    private JWTService jwtService;


    @GetMapping("/todas")
    public List<PublicacionIdDTO> todasPublicaciones(@RequestHeader("Authorization") String token) {
        return service.getAllsinMi();
    }

    @GetMapping("/all")
    public List<PublicacionIdDTO> general(){
        return service.getAll();
    }


    @GetMapping("/segui")
    public List<PublicacionIdDTO> seguidos(){
        return service.getSeguidos();
    }


    @PostMapping("/crear")
    public PublicacionDTO guardar(@RequestBody PublicacionDTO publicacionDTO){
        return service.aniadirPublicacion(publicacionDTO);
    }

    @GetMapping("all/mi")
    public List<PublicacionDTO> getMyPublications(){
        return service.verMisPublicaciones();
    }

    @GetMapping("all/otro/{id}")
    public List<PublicacionDTO> gePublicationsOther(@PathVariable Integer id){
        return service.verPublicacionesOtro(id);
    }

    @PostMapping("del/{idPub}")
    public void eliminarPublicacion2(@PathVariable Integer idPub) {
        repository.deleteById(idPub);
    }

    @DeleteMapping("/{idPub}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable Integer idPub) {
        try {
            service.eliminarPublicacion(idPub);
            return ResponseEntity.ok("Publicación eliminada exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la publicación");
        }
    }

    @PutMapping("/{idPub}")
    public PublicacionDTO actualizarPublicacion(@PathVariable Integer idPub, @RequestBody PublicacionDTO publicacionDTO){
        return service.actualizarPublicacion(idPub, publicacionDTO);
    }

    @GetMapping("/{idPub}")
    public PublicacionDTO obtenerPublicacionPorId(@PathVariable Integer idPub) {
        return service.obtenerPublicacionPorId(idPub);
    }

    @PostMapping("/unirse/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public void unirsePublicacion(@PathVariable Integer idPublicacion) {
        service.unirsePublicacion(idPublicacion);
    }

    @GetMapping("/usuarios-unidos/{idPublicacion}")
    public List<UsuarioDTO> obtenerUsuariosUnidos(@PathVariable Integer idPublicacion) {
        return service.obtenerUsuariosUnidos(idPublicacion);
    }

    @DeleteMapping("/salir/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public void salirPublicacion(@PathVariable Integer idPublicacion) {
        service.salirPublicacion(idPublicacion);
    }




    @GetMapping("/creador/{id}")
    public boolean obtenerCreadorPublicacion(@PathVariable Integer id,@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(perfilLogueado.getCorreoElectronico());

        if(usuario.getId() == id) {
            return true;
        }else {
            return false;
        }
    }

    @GetMapping("/dentro/{id}")
    public boolean dentroOFueradelEvento(@PathVariable Integer id,@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);

        return service.estaEnPublicacion(id,perfilLogueado);
    }


    @GetMapping("/dentro/lista")
    public List<PublicacionIdDTO> listasDeEventosDentro(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionIdDTO> publicacionIdDTOs = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            if(service.estaEnPublicacion(publicacion.getId(),perfilLogueado)){
                Perfil perfil = perfilService.obtenerPerfilPorCorreo(publicacion.getUsuarioCreador().getCorreoElectronico());
                PublicacionIdDTO dto = new PublicacionIdDTO(publicacion.getId(), publicacion.getUsuarioCreador().getNombreUsuario(),
                        publicacion.getCategoria().getNombre(), publicacion.getImagenUrlPub(),perfil.getImagenUrlPerfil() ,
                        publicacion.getTitulo(), publicacion.getDescripcion(), publicacion.getUbicacion(),
                        publicacion.getFechaIni(), publicacion.getFechaFin());
                publicacionIdDTOs.add(dto);
            }
        }

        return publicacionIdDTOs;
    }


    @GetMapping("/otro-usuario/{id}")
    public PerfilDTO obtenerPerfilAjeno(@PathVariable Integer id) {
        Perfil perfil = service.obtenerPerfilPorPublicacion(id);

        return new PerfilDTO(perfil.getNombre(), perfil.getApellidos(), perfil.getCorreoElectronico(),
                perfil.getUsuario().getNombreUsuario(), perfil.getPuntajeTotal(), perfil.getGenero(),
                perfil.getBiografia(), perfil.getPais(), perfil.getFechaNacimiento(), perfil.getImagenUrlPerfil());
    }


    @PostMapping("/{idPublicacion}/puntuar")
    public Reputacion puntuarPublicacion(@PathVariable Integer idPublicacion, @RequestBody ReputacionDTO reputacionDTO, @RequestHeader("Authorization") String token) {
        if (reputacionDTO.getMotivo() != null && reputacionDTO.getMotivo().length() > 255) {
            throw new IllegalArgumentException("El motivo no puede exceder los 255 caracteres");
        }
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return reputacionService.puntuarPublicacion(idPublicacion, perfilLogueado, reputacionDTO.getEstrellas(), reputacionDTO.getMotivo());
    }

    @GetMapping("/reputacion/{idPublicacion}")
    public List<ReputacionDTO> listarReputaciones(@PathVariable Integer idPublicacion) {
        List<Reputacion> repu = reputacionService.obtenerReputacionesPorPublicacion(idPublicacion);
        List<ReputacionDTO> reputaciones = new ArrayList<>();
        for(Reputacion r : repu) {
            ReputacionDTO dto = new ReputacionDTO();
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(r.getPerfil().getCorreoElectronico());
            dto.setNombreUsuario(usuario.getNombreUsuario());
            dto.setImagenUrlUsuario(r.getPerfil().getImagenUrlPerfil());
            dto.setEstrellas(r.getEstrellas());
            dto.setMotivo(r.getMotivo());
            reputaciones.add(dto);
        }
        return reputaciones;
    }

    @GetMapping("/reputacion/mi-reputacion")
    public List<MiPuntuacionDTO> miReputacion(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return reputacionService.miReputacion(perfilLogueado);
    }




}
