@echo off
REM Script para crear estructura hexagonal
REM Colocar en: src\main\java\com\example\demo\

echo ====================================
echo CREANDO ESTRUCTURA HEXAGONAL
echo ====================================

echo Directorio actual: %CD%
echo.

echo Creando carpetas...
mkdir domain 2>nul
mkdir domain\model 2>nul
mkdir domain\service 2>nul
mkdir domain\repository 2>nul

mkdir application 2>nul
mkdir application\dto 2>nul
mkdir application\service 2>nul
mkdir application\port 2>nul
mkdir application\port\in 2>nul
mkdir application\port\out 2>nul

mkdir infrastructure 2>nul
mkdir infrastructure\controller 2>nul
mkdir infrastructure\controller\dto 2>nul
mkdir infrastructure\persistence 2>nul
mkdir infrastructure\persistence\entity 2>nul
mkdir infrastructure\persistence\repository 2>nul

mkdir common 2>nul
mkdir common\exception 2>nul

echo.
echo ====================================
echo ESTRUCTURA CREADA:
echo ====================================
dir /b /ad
echo.
pause