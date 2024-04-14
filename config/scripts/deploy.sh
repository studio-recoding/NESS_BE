#!/bin/bash

# Installing docker engine if not exists
if ! type docker > /dev/null
then
  echo "docker does not exist"
  echo "Start installing docker"
  sudo yum update -y
  sudo yum install docker -y
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo usermod -a -G docker $(whoami)
fi

if ! type docker-compose > /dev/null
then
  echo "docker does not exist"
  echo "Start installing docker"
  sudo dnf install -y libxcrypt-compat
  echo "docker-compose does not exist"
  echo "Start installing docker-compose"
  sudo curl -L "https://github.com/docker/compose/releases/download/1.27.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
fi