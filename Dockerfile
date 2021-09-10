FROM openjdk:11-jre-slim

WORKDIR /app

RUN apt update

COPY . .