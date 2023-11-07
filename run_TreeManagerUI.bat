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
  pushd TreeManagerUI
  npm run build
  popd
  echo Installation completed.
) else if "!action!" == "start" (
  echo Performing 'start' action...
  pushd TreeManagerUI
  start npm run start
) else (
  echo Invalid action. Available actions: install, start
  exit /b 1
)

endlocal