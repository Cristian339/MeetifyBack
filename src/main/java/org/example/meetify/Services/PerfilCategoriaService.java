package org.example.meetify.Services;

import org.example.meetify.DTO.CategoriaDTO;
import org.example.meetify.Repositories.CategoriaRepository;
import org.example.meetify.Repositories.PerfilCategoriaRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.PerfilCategoria;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PerfilCategoriaService {

    @Autowired
    private PerfilCategoriaRepository repository;

    @Autowired
    private JWTFilter jwtFilter;
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerCategoriasPorPerfil(Perfil perfil) {
        List<PerfilCategoria> usuarioCategorias = repository.findByPerfil(perfil);
        return usuarioCategorias.stream()
                .map(PerfilCategoria::getCategoria)
                .collect(Collectors.toList());
    }


    public List<CategoriaDTO> obtenerCategoriasPorPerfil2() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());
        List<Categoria> cats = obtenerCategoriasPorPerfil(perfil);
        List<CategoriaDTO> categorias =  new ArrayList<>();
        for(Categoria cat : cats) {
            System.out.println(cat.getNombre());
            CategoriaDTO catDTO = new CategoriaDTO();
            catDTO.setNombre(cat.getNombre());
            categorias.add(catDTO);
        }
        return categorias;
    }


    public PerfilCategoria anadirCategoriaAPerfil(Perfil perfil, Categoria categoria) {
        // Comprobar si ya existe la relación
        List<PerfilCategoria> existingRelations = repository.findByPerfil(perfil);
        boolean categoriaExists = existingRelations.stream()
                .anyMatch(pc -> pc.getCategoria().equals(categoria));

        if (categoriaExists) {
            throw new IllegalArgumentException("El perfil ya tiene esta categoría.");
        }

        // Crear la nueva relación
        PerfilCategoria perfilCategoria = new PerfilCategoria();
        perfilCategoria.setPerfil(perfil);
        perfilCategoria.setCategoria(categoria);

        // Guardar la nueva relación
        return repository.save(perfilCategoria);
    }

    public void eliminarTodasLasCategoriasPorPerfil(Perfil perfil) {
        // Obtenemos todas las categorías asociadas con este perfil
        List<PerfilCategoria> categoriasPerfil = repository.findByPerfil(perfil);

        // Si existen categorías asociadas, las eliminamos
        if (!categoriasPerfil.isEmpty()) {
            repository.deleteAll(categoriasPerfil);
        }
    }

    public PerfilCategoria anadirCategoriaExistenteAPerfil(Perfil perfil, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findByNombre(categoriaDTO.getNombre());
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }

        return anadirCategoriaAPerfil(perfil, categoria);
    }

    public void eliminarCategoriaPreferenteDePerfil(Perfil perfil, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findByNombre(categoriaDTO.getNombre());
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }

        List<PerfilCategoria> existingRelations = repository.findByPerfil(perfil);
        PerfilCategoria perfilCategoria = existingRelations.stream()
                .filter(pc -> pc.getCategoria().equals(categoria))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El perfil no tiene esta categoría."));

        repository.delete(perfilCategoria);
    }

    public List<CategoriaDTO> verTodasLasCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoria -> {
                    CategoriaDTO dto = new CategoriaDTO();
                    dto.setId(categoria.getId());
                    dto.setNombre(categoria.getNombre());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> verCategoriasElegidasPorPerfil(Perfil perfil) {
        List<PerfilCategoria> perfilCategorias = repository.findByPerfil(perfil);
        return perfilCategorias.stream()
                .map(pc -> {
                    CategoriaDTO dto = new CategoriaDTO();
                    dto.setId(pc.getCategoria().getId());
                    dto.setNombre(pc.getCategoria().getNombre());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public List<CategoriaDTO> verCategoriasNoElegidasPorPerfil(Perfil perfil) {
      List<CategoriaDTO> todas = verTodasLasCategorias();
      List<CategoriaDTO> suyas = verCategoriasElegidasPorPerfil(perfil);
      List<CategoriaDTO> resto = new ArrayList<>();
      for (CategoriaDTO c : todas){
          if(!suyas.contains(c)){
              resto.add(c);
          }
      }
      return resto;
    }



}
