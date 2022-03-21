# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/focal64"
  config.vm.hostname = "hexlet-correction"
  config.vm.define "hexlet-correction"
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.provider "virtualbox" do |vb|
    vb.name = "hexlet-correction"
    vb.memory = "1024"
  end

  config.vm.provision "shell", inline: <<-SHELL
    apt update
    apt install -y make
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh

    groupadd docker
    usermod -aG docker vagrant
    docker run hello-world

    curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose

    apt update
    apt install -y software-properties-common

    # Java
    add-apt-repository ppa:openjdk-r/ppa
    apt install -y openjdk-17-jdk

    # Сборщик проектов
    add-apt-repository ppa:cwchien/gradle
    apt install -y gradle
  SHELL
end
