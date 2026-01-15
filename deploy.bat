@echo off
cd /d "%~dp0"
copy "build\libs\example-plugin.jar" "%APPDATA%\Hytale\UserData\Mods\" /Y
echo Plugin deployed to Hytale mods folder.
