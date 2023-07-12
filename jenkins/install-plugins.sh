#!/bin/bash

# Define plugins and versions
declare -A plugins=(
  ["email-ext"]="2.83"
  ["configuration-as-code"]="1647.ve39ca_b_829b_42"
  ["config-file-provider"]="951.v0461b_87b_721b_"
)

# Install plugins
for plugin in "${!plugins[@]}"; do
  version=${plugins[$plugin]}
  echo "Installing plugin: ${plugin}:${version}"
  docker exec devops-jenkins java -jar /usr/share/jenkins/ref/jenkins-plugin-cli.jar --plugins "${plugin}:${version}"
done
