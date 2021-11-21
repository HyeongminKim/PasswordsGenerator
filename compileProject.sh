#!/bin/bash

echo -e "LICENSE: This script is licensed under the MIT License\n"

if [ "$(uname -s)" = "Darwin" ]; then
    pgrep WindowServer > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "\033[31mERROR! No GUI detected. This application requires a GUI.\033[m"
        exit 3
    fi
elif [ "$(uname -s)" = "Linux" ]; then
    type Xorg > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "\033[31mERROR! No GUI detected. This application requires a GUI. \033[m"
        exit 3
    fi
else
    echo -e "\033[31mERROR! This script only support macOS or Linux.\033[m"
    exit 3
fi

which javac > /dev/null 2>&1
if [ $? != 0 ]; then
    echo -e "\033[31mERROR! This script requires JDK.\033[m"
    exit 1
fi

cd PasswordsGenerator 2>/dev/null
if [ $? != 0 ]; then
    echo -e "\033[31mFATAL ERROR! This script obviously shouldn't be outside the project."
    echo -e "This script was aborted, but it didn't affect the current working directory. \033[m"
    exit 2
fi

echo "Compiling passwdGen.java file..."
javac passwdGen.java
if [ $? != 0 ]; then
    echo -e "\nThe build seems to have failed. Please check again for syntax errors in the recently edited line."
    exit 2
fi

echo "Generating passwdGen.jar file..."
jar -cmf Manifest.mf passwdGen.jar *.class
if [ $? != 0 ]; then
    echo -e "\033[31mjar file couldn't be created. Please check if you are targeting the wrong class file.\033[m"
    exit 2
fi

echo "Cleanup cache files..."
rm *.class

if [ "$(uname -s)" == "Darwin" ]; then
    open .
else
    xdg-open .
fi
