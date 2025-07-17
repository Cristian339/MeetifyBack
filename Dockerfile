# Usa una imagen oficial de OpenJDK con Maven
FROM amazoncorretto:21-alpine-jdk

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Configura la codificación correcta
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Copia Maven Wrapper correctamente
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd pom.xml ./

# Da permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Añade configuración para manejar el encoding
RUN echo "export MAVEN_OPTS='-Dfile.encoding=UTF-8'" > .mavenrc

# Descarga las dependencias para mejorar la cacheabilidad
RUN ./mvnw dependency:resolve dependency:go-offline

# Copia el código fuente
COPY src/ src/

# Compila la aplicación con configuración de encoding
RUN ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8

# Verifica que el JAR se haya generado
RUN ls -l target/

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación
CMD ["java", "-jar", "target/Meetify-0.0.1-SNAPSHOT.jar"]