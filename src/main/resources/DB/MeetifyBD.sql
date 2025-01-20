-- Dropear tablas existentes (en orden para evitar conflictos por claves foráneas)
DROP TABLE IF EXISTS denuncia CASCADE;
DROP TABLE IF EXISTS notificacion CASCADE;
DROP TABLE IF EXISTS mensaje CASCADE;
DROP TABLE IF EXISTS calificacion CASCADE;
DROP TABLE IF EXISTS publicacion_usuario CASCADE;
DROP TABLE IF EXISTS publicacion CASCADE;
DROP TABLE IF EXISTS usuario_categoria CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS perfil CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- Crear tabla usuario
CREATE TABLE usuario (
                         usuario_id SERIAL PRIMARY KEY,
                         contrasenia VARCHAR(255) NOT NULL,
                         nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
                         rol INT  -- Modificado para ser un número (almacena el valor ordinal del rol)
);


-- Crear tabla perfil
CREATE TABLE perfil (
                        perfil_id SERIAL PRIMARY KEY,
                        usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
                        nombre VARCHAR(100) NOT NULL,  -- Añadido según la clase
                        apellidos VARCHAR(100) NOT NULL,  -- Añadido según la clase
                        correo_electronico VARCHAR(100) UNIQUE NOT NULL,
                        puntaje_total INT DEFAULT 0,
                        fecha_nacimiento DATE,  -- Añadido según la clase
                        pais VARCHAR(50),
                        genero VARCHAR(20),
                        biografia TEXT,
                        privado BOOLEAN DEFAULT FALSE,
                        baneado BOOLEAN DEFAULT FALSE
);


-- Crear tabla categoria
CREATE TABLE categoria (
                           categoria_id SERIAL PRIMARY KEY,
                           nombre VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla usuario_categoria
CREATE TABLE usuario_categoria (
                                   usuario_categoria_id SERIAL PRIMARY KEY,
                                   usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
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

-- Crear tabla publicacion_usuario
CREATE TABLE publicacion_usuario (
                                     publicacion_usuario_id SERIAL PRIMARY KEY,
                                     publicacion_id INT NOT NULL REFERENCES publicacion(publicacion_id) ON DELETE CASCADE,
                                     usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE
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
                              tipo VARCHAR(50) NOT NULL,       -- Tipo de notificación (nuevo seguidor, comentario, like, etc.)
                              usuario_id INT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE, -- Usuario que recibirá la notificación
                              mensaje TEXT NOT NULL,           -- Mensaje de la notificación
                              visto BOOLEAN DEFAULT FALSE      -- Indica si el usuario ya visualizó la notificación
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

/*create table token (
                       id serial primary key,
                       token text not null,
                       fecha_creacion timestamp(6) not null,
                       fecha_expiracion timestamp(6) not null,
                       id_usuario int not null,
                       constraint fk_token_usuario foreign key (id_usuario) references usuario(id)
);*/

-- Crear índices para mejorar rendimiento
CREATE INDEX idx_usuario_categoria ON usuario_categoria(usuario_id, categoria_id);
CREATE INDEX idx_publicacion_categoria ON publicacion(categoria_id);
CREATE INDEX idx_calificacion_publicacion ON calificacion(publicacion_id);
