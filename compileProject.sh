#!/bin/bash

echo -e "\e[7mLICENSE: This script is licensed under the MIT License\e[m\n"

if [ "$(uname -s)" = "Darwin" ]; then
    pgrep WindowServer > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "\e[31mERROR! No GUI detected. This application requires a GUI.\e[m"
        exit 3
    fi
elif [ "$(uname -s)" = "Linux" ]; then
    type Xorg > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "\e[31mERROR! No GUI detected. This application requires a GUI. \e[m"
        exit 3
    fi
else
    echo -e "\e[31mERROR! This script only support macOS or Linux.\e[m"
    exit 3
fi

which javac > /dev/null 2>&1
if [ $? != 0 ]; then
    echo -e "\e[31mERROR! This script requires JDK.\e[m"
    exit 1
fi

cd PasswordsGenerator 2>/dev/null
if [ $? != 0 ]; then
    echo -e "\e[31mFATAL ERROR! This script obviously shouldn't be outside the project."
    echo -e "This script was aborted, but it didn't affect the current working directory. \e[m"
    exit 2
fi

echo "Compiling PasswdGen.java file..."
javac PasswdGen.java
if [ $? != 0 ]; then
    echo -e "\n\e[31mThe build seems to have failed. Please check again for syntax errors in the recently edited line.\e[m"
    exit 2
fi

echo "Generating passwdGen.jar file..."
jar -cmf Manifest.mf passwdGen.jar *.class
if [ $? != 0 ]; then
    echo -e "\e[31mjar file couldn't be created. Please check if you are targeting the wrong class file.\e[m"

    echo "Cleanup cache files..."
    rm *.class
    exit 2
fi
chmod 755 passwdGen.jar

echo "Cleanup cache files..."
rm *.class

if [ "$DISABLE_OPEN_DIRECTORY" != "true" ]; then
    if [ "$(uname -s)" == "Darwin" ]; then
        open .
    else
        xdg-open .
    fi
else
    echo "NOTE: The Open Working Directory feature is currently disabled."
fi

