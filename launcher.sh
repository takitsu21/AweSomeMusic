#!/bin/bash

sudo docker ps -a -q  --filter ancestor=awesomebot
sudo docker rmi awesomebot --force && sudo docker-compose build && sudo docker-compose up -d