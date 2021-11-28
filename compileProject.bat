@ECHO OFF

echo LICENSE: This script is licensed under the MIT License

echo NOTE: This window closes automatically.
echo.

if not exist PasswordsGenerator (
    echo FATAL ERROR! This script obviously shouldn't be outside the project.
    echo This script was aborted, but it didn't affect the current working directory.
    echo This window will close automatically after 15 seconds...
    timeout 15 > NUL
    exit
)

chcp 65001
cd PasswordsGenerator
echo Compiling PasswdGen.java file...

javac -encoding utf-8 PasswdGen.java
if "%ERRORLEVEL%" == "1" (
    echo.
    echo The build seems to have failed. Please check again for syntax errors in the recently edited line.
    echo If you want to see StackTrace for a long time, try compiling it manually.
    echo This window will close automatically after 60 seconds...
    timeout 60 > NUL
    exit
)

echo Generating passwdGen.jar file...
jar -cmf Manifest.mf passwdGen.jar *.class
if "%ERRORLEVEL%" == "1" (
    echo.
    echo jar file couldn't be created. Please check once again that the jar file is completely closed.
    echo If you want to see log for a long time, try it manually.
    echo This window will close automatically after 60 seconds...
    timeout 60 > NUL

    echo Cleanup cache files...
    del *.class
    exit
)

echo Cleanup cache files...
del *.class

%SystemRoot%\explorer.exe .

echo This window will close automatically after 5 seconds...
timeout 5 > NUL

exit
