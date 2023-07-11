#!/bin/bash

# Copy the local config file to the container's /site/conf location
cp /usr/src/reviewboard/settings_local.py /site/conf/settings_local.py

# # Start the Review Board container
# exec "$@"
/serve.sh