#!/usr/bin/bash

#Changing ownership of the JAR file
sudo chown -R csye6225:csye6225 /tmp/webapp-1.0-SNAPSHOT.jar

sudo mv /tmp/webapp-1.0-SNAPSHOT.jar /opt/webapp-1.0-SNAPSHOT.jar