# Dockerfile para el frontend Angular
# Etapa 1: Construcción
FROM node:20-alpine AS build

# Metadatos
LABEL maintainer="Proyecto Avanzada"
LABEL description="Frontend Angular para sistema de venta de tenis"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Node.js
COPY package*.json ./

# Instalar dependencias (incluyendo devDependencies para Angular CLI)
RUN npm ci

# Copiar código fuente
COPY . .

# Construir la aplicación para producción (AWS)
RUN npm run build --configuration=production

# Etapa 2: Servidor web
FROM nginx:alpine

# Copiar archivos construidos desde la etapa anterior
COPY --from=build /app/dist/frontend-app /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto
EXPOSE 80

# Comando para ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]
