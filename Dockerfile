FROM openjdk:8

CMD ["mkdir", "-p", "/home/app/"]
COPY ./mercadolibre-coupon-api-0.1.0.jar /home/app/

EXPOSE 8080