package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.Repositories.CategoriaRepository;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
                if(categorias.contains(p.getCategoria())){
                    PublicacionIdDTO dto = new PublicacionIdDTO(p.getId(),p.getUsuarioCreador().getNombreUsuario(),
                            p.getCategoria().getNombre(),p.getImagenUrl(),p.getTitulo(),p.getDescripcion(),
                            p.getUbicacion(),p.getFechaIni(),p.getFechaFin());
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
        for(Publicacion d : publis){
            todas.add(new PublicacionDTO(d.getUsuarioCreador().getNombreUsuario(),
                    d.getCategoria().getNombre(),d.getImagenUrl(),d.getTitulo(),d.getDescripcion(),
                    d.getUbicacion(),d.getFechaIni(),d.getFechaFin()));
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
        publicacion.setImagenUrl(publicacionDTO.getImageUrl());
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
        publicacion.setImagenUrl(publicacionDTO.getImageUrl());
        publicacion.setFechaIni(publicacionDTO.getFechaIni());
        publicacion.setFechaFin(publicacionDTO.getFechaFin());
        repository.save(publicacion);

        return publicacionDTO;
    }

    public String eliminarPublicacion(Integer id) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Publicacion publicacion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada con id: " + id));

        if (!publicacion.getUsuarioCreador().equals(usuario)) {
            throw new SecurityException("No tienes permiso para eliminar esta publicacion");
        }

        repository.delete(publicacion);
        return "Publicacion eliminada exitosamente";
    }

    public List<PublicacionDTO> verMisPublicaciones() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        List<Publicacion> publicaciones = repository.findByUsuarioCreador(usuario);
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            PublicacionDTO dto = new PublicacionDTO(p.getUsuarioCreador().getNombreUsuario(),
                    p.getCategoria().getNombre(), p.getImagenUrl(), p.getTitulo(), p.getDescripcion(),
                    p.getUbicacion(), p.getFechaIni(), p.getFechaFin());
            publicacionDTOS.add(dto);
        }

        return publicacionDTOS;
    }


    public List<PublicacionIdDTO> publicacionesPerfil(String correo) {
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(correo);

        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionIdDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            if(p.getUsuarioCreador().getCorreoElectronico() == perfil.getCorreoElectronico()){
                PublicacionIdDTO dto = new PublicacionIdDTO(p.getId(),p.getUsuarioCreador().getNombreUsuario(),
                        p.getCategoria().getNombre(), p.getImagenUrl(), p.getTitulo(), p.getDescripcion(),
                        p.getUbicacion(), p.getFechaIni(), p.getFechaFin());
                publicacionDTOS.add(dto);
            }
        }

        return publicacionDTOS;
    }






    public void cambiarCategoriaPerfil(List<String> categorias){
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());
        for(String c : categorias){
            Categoria cat = categoriaRepository.findByNombre(c);
            if (cat != null){
                perfilCategoriaService.anadirCategoriaAPerfil(perfil,cat);
            }
        }
    }


}
