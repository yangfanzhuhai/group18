#!/bin/sh

# Install aptitude
sudo apt-get update
sudo apt-get install -y aptitude

# Upgrade system
sudo aptitude update
sudo aptitude -y upgrade

# Install Java
sudo aptitude purge openjdk*
sudo add-apt-repository ppa:webupd8team/java
sudo aptitude update
sudo aptitude install oracle-java7-installer

# Install Jenkins
#cd jenkins
#sudo sh installation.sh


