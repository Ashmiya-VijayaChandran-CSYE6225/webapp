#!/bin/bash

#Install Java 
sudo dnf install java-17-openjdk-devel.x86_64 -y

#Install mySQL
sudo dnf install mysql mysql-server -y

#Start mySQL
sudo systemctl enable mysqld
sudo systemctl start mysqld

#Alter Password of mySQL
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'Password#1';"