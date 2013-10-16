#!/bin/sh

# Install aptitude
sudo apt-get update
sudo apt-get install -y aptitude

# Upgrade system
sudo aptitude update
sudo aptitude -y upgrade
