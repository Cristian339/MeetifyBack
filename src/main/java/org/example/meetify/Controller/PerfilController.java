package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.*;
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



    @GetMapping("/todos")
    public List<PerfilconIdDTO> obtenerTodosLosPerfiles(@RequestHeader("Authorization") String token){
        Perfil perfil = jwtService.extraerPerfilToken(token);
        return usuarioService.getAllPerfiles(perfil);
    }

    @GetMapping("/otro-usuario/{id}")
    public PerfilconIdDTO obtenerPerfilPorId(@PathVariable Integer id){
        Perfil p = perfilRepository.findById(id).orElse(null);
        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(p.getCorreoElectronico());
        PerfilconIdDTO dto = new PerfilconIdDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setApellidos(p.getApellidos());
        dto.setCorreoElectronico(p.getCorreoElectronico());
        dto.setGenero(p.getGenero());
        dto.setBiografia(p.getBiografia());
        dto.setPais(p.getPais());
        dto.setPuntajeTotal(p.getPuntajeTotal());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        return dto;
    }

    @GetMapping("/mi")
    public PerfilDTO obtenerMiPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilDTOPorId(perfilLogueado.getId());
    }

    @GetMapping("/mi-id")
    public PerfilIDDTO obtenerMiPerfilId(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilIDDTOById(perfilLogueado.getId());
    }

    @GetMapping("/nombre")
    public UsuarioDTO obtenerNombreUsuario(@RequestHeader("Authorization") String token) {
        Usuario usuario = jwtService.extraerUsuarioToken(token);
        return new UsuarioDTO(usuario.getId(), usuario.getNombreUsuario());
    }

    @PostMapping("/actualizar-categorias")
    public void cambiarCategorias(@RequestBody List<String> categorias,@RequestHeader("Authorization") String token) {
        Perfil perfil = jwtService.extraerPerfilToken(token);
        publicacionService.cambiarCategoriaPerfil(categorias,perfil);
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


    @GetMapping("/baneado")
    public boolean verEstadoBaneo(@RequestHeader("Authorization") String token){
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilLogueado.getBaneado();
    }

    @GetMapping("/entrada")
    public boolean verEstadoEntrada(@RequestHeader("Authorization") String token){
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilLogueado.getEntrada();
    }


    @PostMapping("/entrada/hecha")
    public void cambiarEstadoEntrada(@RequestHeader("Authorization") String token){
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        perfilLogueado.setEntrada(true);
        perfilRepository.save(perfilLogueado);
    }

}
