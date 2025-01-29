-- Dropear tablas existentes (en orden para evitar conflictos por claves foráneas)
DROP TABLE IF EXISTS denuncia CASCADE;
DROP TABLE IF EXISTS notificacion CASCADE;
DROP TABLE IF EXISTS mensaje CASCADE;
DROP TABLE IF EXISTS calificacion CASCADE;
DROP TABLE IF EXISTS publicacion_perfil CASCADE;
DROP TABLE IF EXISTS publicacion CASCADE;
DROP TABLE IF EXISTS perfil_categoria CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS perfil CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS seguidores CASCADE;
-- Crear tabla usuario
CREATE TABLE usuario (
                                 usuario_id SERIAL PRIMARY KEY,
                                 contrasenia VARCHAR(255) NOT NULL,
                                 nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
                                 correo_electronico VARCHAR(100) UNIQUE NOT NULL,
                                 rol INT NOT NULL
);

-- Crear tabla perfil
CREATE TABLE perfil (
                        perfil_id SERIAL PRIMARY KEY,
                        usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                        nombre VARCHAR(100) NOT NULL,
                        apellidos VARCHAR(100) NOT NULL,
                        correo_electronico VARCHAR(100) UNIQUE NOT NULL,
                        imagen_url VARCHAR(255),
                        puntaje_total INT DEFAULT 0,
                        fecha_nacimiento DATE,
                        pais VARCHAR(50),
                        genero INT,
                        biografia TEXT,
                        privado BOOLEAN DEFAULT FALSE,
                        baneado BOOLEAN DEFAULT FALSE
);

-- Crear tabla categoria
CREATE TABLE categoria (
                                   categoria_id SERIAL PRIMARY KEY,
                                   nombre VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla perfil_categoria
CREATE TABLE perfil_categoria (
                                          perfil_categoria_id SERIAL PRIMARY KEY,
                                          perfil_id INT NOT NULL REFERENCES perfil(perfil_id) ON DELETE CASCADE,
                                          categoria_id INT NOT NULL REFERENCES categoria(categoria_id) ON DELETE CASCADE
);

-- Crear tabla publicacion
CREATE TABLE publicacion (
                                     publicacion_id SERIAL PRIMARY KEY,
                                     categoria_id INT NOT NULL REFERENCES categoria(categoria_id) ON DELETE SET NULL,
                                     titulo VARCHAR(100) NOT NULL,
                                     descripcion TEXT NOT NULL,
                                     ubicacion VARCHAR(255),
                                     fecha_ini TIMESTAMP NOT NULL,
                                     fecha_fin TIMESTAMP NOT NULL,
                                     imagen_url TEXT,
                                     usuario_creador_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE
);

-- Crear tabla publicacion_perfil
CREATE TABLE publicacion_perfil (
                                            publicacion_perfil_id SERIAL PRIMARY KEY,
                                            publicacion_id INT NOT NULL REFERENCES publicacion(publicacion_id) ON DELETE CASCADE,
                                            perfil_id INT NOT NULL REFERENCES perfil(perfil_id) ON DELETE CASCADE
);

-- Crear tabla calificacion
CREATE TABLE calificacion (
                                      calificacion_id SERIAL PRIMARY KEY,
                                      usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                      publicacion_id INT NOT NULL REFERENCES publicacion(publicacion_id) ON DELETE CASCADE,
                                      puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
                                      comentario TEXT
);

-- Crear tabla mensaje
CREATE TABLE mensaje (
                                 mensaje_id SERIAL PRIMARY KEY,
                                 contenido TEXT NOT NULL,
                                 usuario_emisor_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                 usuario_receptor_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                 publicacion_id INT REFERENCES publicacion(publicacion_id) ON DELETE SET NULL,
                                 enviado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla notificacion
CREATE TABLE notificacion (
                                      notificacion_id SERIAL PRIMARY KEY,
                                      tipo VARCHAR(50) NOT NULL,
                                      usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                      mensaje TEXT NOT NULL,
                                      visto BOOLEAN DEFAULT FALSE
);

-- Crear tabla denuncia
CREATE TABLE denuncia (
                                  denuncia_id SERIAL PRIMARY KEY,
                                  usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                  publicacion_id INT REFERENCES publicacion(publicacion_id) ON DELETE SET NULL,
                                  usuario_reportado_id INT REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                                  descripcion TEXT NOT NULL,
                                  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE seguidores (
                                    seguidor_id INT NOT NULL REFERENCES perfil(perfil_id) ON DELETE CASCADE,
                                    seguido_id INT NOT NULL REFERENCES perfil(perfil_id) ON DELETE CASCADE,
                                    PRIMARY KEY (seguidor_id, seguido_id),
                                    fecha_seguimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar rendimiento
CREATE INDEX idx_perfil_categoria ON meetify.perfil_categoria(perfil_id, categoria_id);
CREATE INDEX idx_publicacion_categoria ON meetify.publicacion(categoria_id);
CREATE INDEX idx_calificacion_publicacion ON meetify.calificacion(publicacion_id);