@echo off

SETLOCAL

set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_02
set path=%JAVA_HOME%\bin;%path%

"C:\Program Files\ant\bin\ant" %1 %2 %3 %4