#!/bin/bash

#paso 0 = crear el ejecutable

mvn clean;
mvn package;

#paso 1 = construir la imagen
sudo docker build -t dilanbernabe/tp1pt1:latest .

#paso 2 subir al dockerhub la imagen creada
sudo docker push dilanbernabe/tp1pt1:latest

sudo docker run --name prueba3 -d -p 9090:9090 dilanbernabe/tp1pt1:latest
