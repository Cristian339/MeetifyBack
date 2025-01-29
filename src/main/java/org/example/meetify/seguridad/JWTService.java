package org.example.meetify.seguridad;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;

    /**
     * Método para generar token de acceso a través de los datos
     * de un usuario
     *
     * @param usuario
     * @return
     */
    public String generateToken(Usuario usuario){
        TokenDataDTO tokenDataDTO = TokenDataDTO
                .builder()
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .fecha_creacion(System.currentTimeMillis())
                .fecha_expiracion(System.currentTimeMillis() + 1000 * 60 * 60 * 3)
                .build();

        return Jwts
                .builder()
                .claim("tokenDataDTO", tokenDataDTO)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractDatosToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public TokenDataDTO extractTokenData(String token){
        Claims claims = extractDatosToken(token);
        Map<String, Object> mapa =  (LinkedHashMap<String,Object>) claims.get("tokenDataDTO");
        return TokenDataDTO.builder()
                .username((String) mapa.get("username"))
                .fecha_creacion((Long) mapa.get("fecha_creacion"))
                .fecha_expiracion((Long) mapa.get("fecha_expiracion"))
                .rol((String) mapa.get("rol"))
                .build();
    }

    /**
     * Método que me dicaWDAe si el token a expirado
     * @param token
     * @return
     */
    public boolean isExpired(String token){
        return new Date(extractTokenData(token).getFechaExpiracion()).before(new Date());
    }


    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Perfil extraerPerfilToken(String token){
        String tokenSinCabecera = token.substring(7);
        TokenDataDTO tokenDataDTO = extractTokenData(tokenSinCabecera);
        Usuario usuarioLogueado = (Usuario) usuarioService.loadUserByUsername(tokenDataDTO.getUsername());
        Perfil perfilUsuarioLogueado = perfilService.buscarPorUsuario(usuarioLogueado);
        return perfilUsuarioLogueado;

    }
}