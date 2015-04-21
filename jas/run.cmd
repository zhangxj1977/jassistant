@echo off
setlocal enabledelayedexpansion
set basepath=%~dp0
set jarfiles=%basepath%lib\*.jar
set classpath=.\jassistant.jar
for %%i in (%jarfiles%) do (set classpath=!classpath!;%%i)
java org.jas.gui.Main config
