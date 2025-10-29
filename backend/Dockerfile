# Dockerfile para el backend Spring Boot
FROM openjdk:17-jdk-slim

# Metadatos
LABEL maintainer="Proyecto Avanzada"
LABEL description="Backend Spring Boot para sistema de venta de tenis"

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Crear directorio de trabajo
WORKDIR /app

# Instalar Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (para aprovechar la cache de Docker)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Crear directorio para logs
RUN mkdir -p /app/logs

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/backend-0.0.1-SNAPSHOT.jar"]
