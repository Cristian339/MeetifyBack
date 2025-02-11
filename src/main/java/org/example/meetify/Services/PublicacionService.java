package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.DTO.UsuarioDTO;
import org.example.meetify.Repositories.CategoriaRepository;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.publicacionPerfilRepository;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.models.*;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PublicacionService {

    private PublicacionRepository repository;

    private JWTFilter jwtFilter;

    private PerfilService perfilService;

    private UsuarioService usuarioService;

    private PerfilCategoriaService perfilCategoriaService;

    private final CategoriaRepository categoriaRepository;

    private final PerfilRepository perfilRepository;

    private final PublicacionRepository publicacionRepository;

    private final publicacionPerfilRepository publicacionPerfilRepository;

    public List<PublicacionIdDTO> getAll() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionIdDTO> publicacionDTOS = new ArrayList<>();


        Usuario us = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(us.getCorreoElectronico());
        List<Categoria> categorias = perfilCategoriaService.obtenerCategoriasPorPerfil(perfil);

        for (Publicacion p : publicaciones) {
            // Filtra las publicaciones para excluir las del usuario autenticado
            if (!p.getUsuarioCreador().getCorreoElectronico().equals(perfil.getCorreoElectronico())) {
                if (categorias.contains(p.getCategoria())) {
                    PublicacionIdDTO dto = new PublicacionIdDTO(p.getId(), p.getUsuarioCreador().getNombreUsuario(),
                            p.getCategoria().getNombre(), p.getImagenUrlPub(), p.getTitulo(), p.getDescripcion(),
                            p.getUbicacion(), p.getFechaIni(), p.getFechaFin());
                    publicacionDTOS.add(dto);
                }
            }
        }

        return publicacionDTOS;

    }

    public Publicacion encontrarPublicacionPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<PublicacionDTO> getSeguidos(){
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());

        List<Publicacion> publis = repository.findAll();
        List<PublicacionDTO> todas = new ArrayList<>();
        for (Publicacion d : publis) {
            Perfil perfilCreador = perfilService.obtenerPerfilPorCorreo(d.getUsuarioCreador().getCorreoElectronico());
            todas.add(new PublicacionDTO(d.getId(), d.getUsuarioCreador().getNombreUsuario(),
                    d.getCategoria().getNombre(), d.getImagenUrlPub(), perfilCreador.getImagenUrlPerfil(),
                    d.getTitulo(), d.getDescripcion(), d.getUbicacion(), d.getFechaIni(), d.getFechaFin()));
        }




        List<PublicacionDTO> publicaciones = new ArrayList<>();




        for (PublicacionDTO p : todas){
            Usuario us = usuarioService.obtenerUsuarioPorNombre(p.getNombrePerfil());
            Perfil perfilPubli = perfilService.obtenerPerfilPorCorreo(us.getCorreoElectronico());

            if(perfil.getSeguidos().contains(perfilPubli)){
                publicaciones.add(p);
            }
        }
        return publicaciones;
    }


    public PublicacionDTO aniadirPublicacion(PublicacionDTO publicacionDTO){
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Publicacion publicacion = new Publicacion();
        Categoria categoria = categoriaRepository.findByNombre(publicacionDTO.getCategoria());

        if(categoria == null){
            throw new IllegalArgumentException("Categoria no encontrada" + publicacionDTO.getCategoria());
        }
        publicacion.setCategoria(categoria);
        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());
        publicacion.setUbicacion(publicacionDTO.getUbicacion());
        publicacion.setImagenUrlPub(publicacionDTO.getImagenUrlPub());
        publicacion.setUsuarioCreador(usuario);
        publicacion.setFechaIni(publicacionDTO.getFechaIni());
        publicacion.setFechaFin(publicacionDTO.getFechaFin());
        repository.save(publicacion);
        return publicacionDTO;

    }

    public PublicacionDTO actualizarPublicacion(Integer id, PublicacionDTO publicacionDTO) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Publicacion publicacion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada con id: " + id));

        if (!publicacion.getUsuarioCreador().equals(usuario)) {
            throw new SecurityException("No tienes permiso para actualizar esta publicacion");
        }

        Categoria categoria = categoriaRepository.findByNombre(publicacionDTO.getCategoria());
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no encontrada: " + publicacionDTO.getCategoria());
        }

        publicacion.setCategoria(categoria);
        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());
        publicacion.setUbicacion(publicacionDTO.getUbicacion());
        publicacion.setImagenUrlPub(publicacionDTO.getImagenUrlPub());
        publicacion.setFechaIni(publicacionDTO.getFechaIni());
        publicacion.setFechaFin(publicacionDTO.getFechaFin());
        repository.save(publicacion);

        return publicacionDTO;
    }

    public void eliminarPublicacion(Integer id) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Publicacion publicacion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada con id: " + id));

        if (!publicacion.getUsuarioCreador().equals(usuario)) {
            throw new SecurityException("No tienes permiso para eliminar esta publicacion");
        }

        repository.delete(publicacion);
    }

    // Metodo para unirse a una publicacion
    public void unirsePublicacion(Integer idPublicacion) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicacion no encontrada"));

        if (publicacion.getUsuarioCreador().equals(usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El creador de la publicacion no puede unirse a su propia publicacion");
        }

        boolean isAlreadyMember = publicacionPerfilRepository.findByPerfilAndPublicacion(perfil, publicacion).isPresent();
        if (isAlreadyMember) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya eres miembro de esta publicacion");
        }

        PublicacionPerfil publicacionPerfil = new PublicacionPerfil();
        publicacionPerfil.setPerfil(perfil);
        publicacionPerfil.setPublicacion(publicacion);

        publicacionPerfilRepository.save(publicacionPerfil);
    }

    public void salirPublicacion(Integer idPublicacion) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicacion no encontrada"));

        PublicacionPerfil publicacionPerfil = publicacionPerfilRepository.findByPerfilAndPublicacion(perfil, publicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la relacion entre el perfil y la publicacion"));

        publicacionPerfilRepository.delete(publicacionPerfil);
    }

    // Metodo para obtener los usuarios unidos a una publicacion
    public List<UsuarioDTO> obtenerUsuariosUnidos(Integer idPublicacion) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicacion no encontrada"));

        List<PublicacionPerfil> publicacionPerfils = publicacionPerfilRepository.findByPublicacion(publicacion);

        boolean isMember = publicacionPerfils.stream()
                .anyMatch(pp -> pp.getPerfil().equals(perfil));

        if (!isMember && !publicacion.getUsuarioCreador().equals(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo los miembros de la publicacion o el creador pueden ver los demas miembros");
        }

        if (publicacionPerfils.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay usuarios unidos a esta publicacion");
        }

        List<UsuarioDTO> usuarios = new ArrayList<>();

        for (PublicacionPerfil pp : publicacionPerfils) {
            Perfil p = pp.getPerfil();
            usuarios.add(new UsuarioDTO(p.getId(), p.getNombre()));
        }

        return usuarios;
    }

    public PublicacionDTO obtenerPublicacionPorId(Integer id) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Publicacion publicacion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada con id: " + id));

        if (!publicacion.getUsuarioCreador().equals(usuario)) {
            throw new SecurityException("No tienes permiso para ver esta publicacion");
        }

        return new PublicacionDTO(publicacion.getId(), publicacion.getUsuarioCreador().getNombreUsuario(),
                publicacion.getCategoria().getNombre(), publicacion.getImagenUrlPub(), publicacion.getImagenUrlPerfil() ,publicacion.getTitulo(),
                publicacion.getDescripcion(), publicacion.getUbicacion(), publicacion.getFechaIni(), publicacion.getFechaFin());
    }


    public List<PublicacionDTO> verMisPublicaciones() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        List<Publicacion> publicaciones = repository.findByUsuarioCreador(usuario);
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            Perfil perfilCreador = perfilService.obtenerPerfilPorCorreo(p.getUsuarioCreador().getCorreoElectronico());
            PublicacionDTO dto = new PublicacionDTO(p.getId(), p.getUsuarioCreador().getNombreUsuario(),
                    p.getCategoria().getNombre(), p.getImagenUrlPub(), perfilCreador.getImagenUrlPerfil(),
                    p.getTitulo(), p.getDescripcion(), p.getUbicacion(), p.getFechaIni(), p.getFechaFin());
            publicacionDTOS.add(dto);
        }

        return publicacionDTOS;
    }


    public List<PublicacionIdDTO> publicacionesPerfil(String correo) {
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(correo);

        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionIdDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            if(Objects.equals(p.getUsuarioCreador().getCorreoElectronico(), perfil.getCorreoElectronico())){
                PublicacionIdDTO dto = new PublicacionIdDTO(p.getId(),p.getUsuarioCreador().getNombreUsuario(),
                        p.getCategoria().getNombre(), p.getImagenUrlPub(), p.getTitulo(), p.getDescripcion(),
                        p.getUbicacion(), p.getFechaIni(), p.getFechaFin());
                publicacionDTOS.add(dto);
            }
        }

        return publicacionDTOS;
    }


    public void cambiarCategoriaPerfil(List<String> categorias, String correo){
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(correo);
        for(String c : categorias){
            Categoria cat = categoriaRepository.findByNombre(c);
            if (cat != null){
                perfilCategoriaService.anadirCategoriaAPerfil(perfil,cat);
            }
        }
    }


    public void eliminarTodasLasPublicacionesPorPerfil(Perfil perfil) {
        // Obtiene todas las publicaciones asociadas con el perfil
        List<Publicacion> publicaciones = repository.findByUsuarioCreador(perfil.getUsuario());

        // Si hay publicaciones, las elimina
        if (!publicaciones.isEmpty()) {
            repository.deleteAll(publicaciones);
        }
    }


}
