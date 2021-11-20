@ECHO OFF

echo LICENSE: This script is licensed under the MIT License

echo NOTE: This window closes automatically.
echo.
chcp 65001

cd PasswordsGenerator
echo Compiling passwdGen.java file...

javac -encoding utf-8 passwdGen.java

echo Generating passwdGen.jar file...
jar -cmf Manifest.mf passwdGen.jar *.class

echo Cleanup cache files...
del *.class

exit
