@echo off

if "%~1"=="" (
  echo Usage: %0 ^<action^>
  echo Available actions: install, start
  exit /b 1
)

setlocal enabledelayedexpansion
set "action=%~1"

if "!action!" == "install" (
  echo Performing 'install' action...
  pushd TreeManager
  mvn clean install
  popd
  echo Installation completed.
) else if "!action!" == "start" (
  echo Performing 'start' action...
  start java -jar TreeManager\target\TreeManager-0.0.1-SNAPSHOT.jar
  popd
) else (
  echo Invalid action. Available actions: install, start
  exit /b 1
)

endlocal