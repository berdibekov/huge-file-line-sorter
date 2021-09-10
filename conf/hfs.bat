@echo off
chcp 65001 1>nul
java -Dsun.stdout.encoding=UTF-8 -jar "%~dp0Huge-file-sorter-0.0.1-jar-with-dependencies.jar" %*
