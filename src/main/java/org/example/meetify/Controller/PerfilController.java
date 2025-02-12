package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.CategoriaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.UsuarioRepository;
import org.example.meetify.Services.*;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.PerfilCategoria;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.example.meetify.seguridad.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/publicacion/perfil")
@AllArgsConstructor
public class PerfilController {
    private final PerfilRepository perfilRepository;
    private PerfilService perfilService;
    private JWTService jwtService;
    private PublicacionService publicacionService;
    private CompartirService compartirService;
    private PerfilCategoriaService perfilCategoriaService;
    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;

    @GetMapping("/mi")
    public PerfilDTO obtenerMiPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilDTOPorId(perfilLogueado.getId());
    }

    @PostMapping("/actualizar-categorias/{correo}")
    public void cambiarCategorias(@RequestBody List<String> categorias,@PathVariable String correo) {
        publicacionService.cambiarCategoriaPerfil(categorias,correo);
    }


    @GetMapping("/compartidos-otro/{id}")
    public List<PublicacionDTO> obtenerPublicacionesCompartidasDeOtro(@PathVariable Integer id) {
        return compartirService.obtenerPublicacionesCompartidasPorOtroPerfil(id);

    }

    @GetMapping("/compartidos")
    public List<PublicacionDTO> obtenerPublicacionesCompartidas() {
        return compartirService.publicacionesCompartidas();
    }

    // Permitir que un perfil comparta una publicación
    @PostMapping("/compartir/{id}")
    public PublicacionDTO compartirPublicacion(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        Perfil perfil = jwtService.extraerPerfilToken(token);
        System.out.println(perfil.getCorreoElectronico());
        return compartirService.compartirPublicacion(id,perfil);
    }


    @GetMapping("/categorias")
    public List<CategoriaDTO> obtenerCategorias() {

        return perfilCategoriaService.obtenerCategoriasPorPerfil2();
    }


    @GetMapping("/categorias-otro/{id}")
    public List<CategoriaDTO> obtenerCategoriasOtro(@PathVariable Integer id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);
        List<Categoria> categorias = perfilCategoriaService.obtenerCategoriasPorPerfil(perfil);
        List<CategoriaDTO> dtos = new ArrayList<>();
        for (Categoria categoria : categorias) {
            CategoriaDTO categoriaDTO = new CategoriaDTO(categoria.getId(),categoria.getNombre());
            dtos.add(categoriaDTO);
        }
        return dtos;
    }


    @PostMapping("/eliminar/{contrasenia}")
    public void eliminarCuenta(@PathVariable String contrasenia,@RequestHeader("Authorization") String token){
        Perfil perfil = jwtService.extraerPerfilToken(token);
        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(perfil.getCorreoElectronico());
        if(usuarioService.autentificarse(contrasenia,usuario)){
            compartirService.eliminarTodasLasPublicacionesCompartidasPorPerfil(perfil);
            perfilCategoriaService.eliminarTodasLasCategoriasPorPerfil(perfil);
            publicacionService.eliminarTodasLasPublicacionesPorPerfil(perfil);
            perfilService.eliminarPerfil(perfil);
            usuarioRepository.delete(usuario);
        }else {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<PerfilDTO> actualizarPerfil(@RequestBody PerfilDTO perfilDTO, @RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        PerfilDTO updatedPerfil = perfilService.actualizarPerfil(perfilLogueado.getId(), perfilDTO);
        return ResponseEntity.ok(updatedPerfil);
    }

    @PostMapping("/anadir-categoria")
    public ResponseEntity<String> anadirCategoriaExistenteAPerfil(@RequestBody CategoriaDTO categoriaDTO, @RequestHeader("Authorization") String token) {
        try {
            Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
            perfilCategoriaService.anadirCategoriaExistenteAPerfil(perfilLogueado, categoriaDTO);
            return ResponseEntity.ok("Categoría añadida exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarCategoriaPreferenteDePerfil(@RequestBody CategoriaDTO categoriaDTO, @RequestHeader("Authorization") String token) {
        try {
            Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
            perfilCategoriaService.eliminarCategoriaPreferenteDePerfil(perfilLogueado, categoriaDTO);
            return ResponseEntity.ok("Categoría eliminada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todas")
    public List<CategoriaDTO> verTodasLasCategorias() {
        return perfilCategoriaService.verTodasLasCategorias();
    }

    @GetMapping("/elegidas")
    public List<CategoriaDTO> verCategoriasElegidasPorPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilCategoriaService.verCategoriasElegidasPorPerfil(perfilLogueado);
    }

    @GetMapping("/noelegidas")
    public List<CategoriaDTO> verCategoriasNoElegidasPorPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilCategoriaService.verCategoriasNoElegidasPorPerfil(perfilLogueado);
    }




}
