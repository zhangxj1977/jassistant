@echo off
@setlocal enabledelayedexpansion

ECHO -------------------------------
ECHO Running jasssitant client
ECHO -------------------------------

set basepath=%~dp0
set jarfiles=%basepath%lib\*.jar
set classpath=.\jassistant.jar
for %%i in (%jarfiles%) do (set classpath=!classpath!;%%i)

if exist "C:\Program Files\Java\jre7" (
    set JAVA_HOME=C:\Program Files\Java\jre7
) else if exist "C:\Program Files\Java\jre6" (
    set JAVA_HOME=C:\Program Files\Java\jre6
)

@start "jassistant" "%JAVA_HOME%\bin\javaw.exe" -Dsun.java2d.ddoffscreen=false -cp %classpath% org.jas.gui.Main config
