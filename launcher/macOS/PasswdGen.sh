#!/bin/bash

debugOutput=~/Library/Logs/PasswdGen/launch.log

if [ -d ~/Library/Logs/PasswdGen ]; then
    echo "로그 디렉토리가 이미 생성됨" > $debugOutput
else
    mkdir ~/Library/Logs/PasswdGen
    if [ $? != 0 ]; then
        echo "$HOME/Library/Logs 에 디렉토리를 생성할 수 없음: 쓰기 권한이 있는지 확인해 주세요"
        osascript -e 'tell app "System Events" to display alert "로그 디렉토리에 접근할 수 없어 실행할 수 없습니다. "'
        exit 1
    else
        echo "로그 디렉토리가 생성됨" > $debugOutput
    fi
fi

targetDir="$(echo $0 | sed "s/\/PasswdGen//g")"
cd "$targetDir" >> $debugOutput 2>&1
if [ $? != 0 ]; then
    osascript -e 'tell app "System Events" to display alert "프로그램 현재 위치에 접근할 수 없어 실행할 수 없습니다. "'
    exit 1
fi

echo -n "현재 JVM 경로: " >> $debugOutput
which java >> $debugOutput 2>&1
if [ $? != 0 ]; then
    osascript -e 'tell app "System Events" to display alert "이 프로그램은 Java이(가) 필요합니다. 자세한 사항은 홈페이지 README를 참고하시기 바랍니다. "'
    exit 1
fi

echo -n "--프로그램 실행-- " >> $debugOutput
java -jar ../Resources/passwdGen.jar >> $debugOutput 2>&1
if [ $? != 0 ]; then
    osascript -e 'tell app "System Events" to display alert "프로그램을 실행하는 도중 에러가 발생하였습니다. "'
    exit 1
fi

