# mercadolibre-coupon-api
API para maximizar el uso de un cupon de Mercado Libre

## Requerimiento

Mercado Libre está implementando un nuevo beneficio para los usuarios que más usan la plataforma con un cupón de cierto
monto gratis que les permitirá comprar tantos items marcados como favoritos que no excedan el monto total. Para esto se
está analizando construir una API que dado una lista de item_id y el monto total pueda darle la lista de items que
maximice el total gastado sin excederlo. 

### Aclaraciones:
- Sólo se puede comprar una unidad por item_id.
- No hay preferencia en la cantidad total de items siempre y cuando gasten el máximo posible.
- El precio puede contener hasta 2 decimales.

## Análisis del problema y Diseño de solución

[En el documento](Design.md) se incluye  el analisis y el diseño realizado.

## Calidad del código

### Validación de _Code_ _Coverage_
![Reporte de Coverage](doc/coverage_report.png "Reporte de Coverage")

### Validación de Estilo
![Reporte de validación de estilo](doc/checkstyle_report.png "Reporte de validación de estilo")

## Posibles mejoras futuras

- [ ] Habilitar metricas de latencia mediante actuator y prometeus.
- [ ] Modificar para convertir en API reactiva empleando WebFlux and WebClient.
- [ ] Emplear cache en el cálculo de la solución para grupo de items de tamaño significativo.
- [ ] Emplear memcache en Google App Engine para compartir el cache entre las instancias.
- [ ] Emplear Github Actions para generar release y desplegar a Google App Engine automaticamente posterior al merge a master.
