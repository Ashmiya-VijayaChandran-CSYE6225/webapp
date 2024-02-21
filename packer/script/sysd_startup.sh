#!/bin/bash

sudo chown -R csye6225:csye6225 /tmp/start-webapp.service
sudo cp /tmp/start-webapp.service /etc/systemd/system/start-webapp.service
sudo systemctl daemon-reload
sudo systemctl enable start-webapp.service