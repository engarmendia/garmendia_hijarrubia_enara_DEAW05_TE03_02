FROM nginx:alpine

# Copiar archivos estáticos
COPY public /usr/share/nginx/html

# Configuración para mantener el contenedor activo
CMD ["sh", "-c", "nginx -g 'daemon off;' || tail -f /dev/null"]