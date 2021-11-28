@ECHO OFF

echo [7mLICENSE: This script is licensed under the MIT License[0m

if "'c(mode)'" == "batch" (
    echo.
    echo [31mERROR! No GUI detected. This application requires a GUI.[0m
    exit
)

echo [1mNOTE: This window closes automatically.[0m
echo.

where javac > NUL 2>&1
if "%ERRORLEVEL%" == "1" (
    echo [31mERROR! This script requires JDK.[0m
    timeout 10 > NUL
    exit
)

if not exist PasswordsGenerator (
    echo [91mFATAL ERROR! This script obviously shouldn't be outside the project.[0m
    echo [31mThis script was aborted, but it didn't affect the current working directory.[0m
    echo [1mThis window will close automatically after 15 seconds...[0m
    timeout 15 > NUL
    exit
)

chcp 65001
cd PasswordsGenerator
echo Compiling PasswdGen.java file...

javac -encoding utf-8 PasswdGen.java
if "%ERRORLEVEL%" == "1" (
    echo.
    echo [31mThe build seems to have failed. Please check again for syntax errors in the recently edited line.[0m
    echo [31mIf you want to see StackTrace for a long time, try compiling it manually.[0m
    echo [1mThis window will close automatically after 60 seconds...[0m
    timeout 60 > NUL
    exit
)

echo Generating passwdGen.jar file...
jar -cmf Manifest.mf passwdGen.jar *.class
if "%ERRORLEVEL%" == "1" (
    echo.
    echo [31mjar file couldn't be created. Please check once again that the jar file is completely closed.[0m
    echo [31mIf you want to see log for a long time, try it manually.[0m
    echo [1mThis window will close automatically after 60 seconds...[0m
    timeout 60 > NUL

    echo Cleanup cache files...
    del *.class
    exit
)

echo Cleanup cache files...
del *.class

%SystemRoot%\explorer.exe .

echo [1mThis window will close automatically after 5 seconds...[0m
timeout 5 > NUL

exit
