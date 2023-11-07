#!/bin/bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <action>"
  echo "Available actions: install, start"
  exit 1
fi

action="$1"

if [ "$action" = "install" ]; then
  echo "Performing 'install' action..."
  (cd TreeManager && mvn clean install)
  echo "Installation completed."

elif [ "$action" = "start" ]; then
  echo "Performing 'start' action..."
  (cd TreeManager && java -jar target/TreeManager-0.0.1-SNAPSHOT.jar)

else
  echo "Invalid action. Available actions: install, start"
  exit 1
fi