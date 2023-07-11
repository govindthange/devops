#!/bin/bash

# Define plugins and versions
declare -A plugins=(
  ["email-ext"]="2.83"
  # ["slack"]="2.46"
  # Add more plugins and versions as needed
)

# Install plugins
for plugin in "${!plugins[@]}"; do
  version=${plugins[$plugin]}
  echo "Installing plugin: ${plugin}:${version}"
  docker exec devops-jenkins java -jar /usr/share/jenkins/ref/jenkins-plugin-cli.jar --plugins "${plugin}:${version}"
done
