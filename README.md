# PasswordsGenerator
### 요구사항
- 프로젝트를 빌드하기 위해 JDK 또는 OpenJDK가 필요합니다. 
- 이 프로그램은 GUI를 사용하기 때문에 CLI기반 시스템에서는 빌드할 수 없습니다. 

### 설치 방법
#### macOS 및 Linux
- [compileProject.sh](https://github.com/HyeongminKim/PasswordsGenerator/blob/master/compileProject.sh) 스크립트를 사용해서 java 파일을 빌드합니다.
- 또는 아래의 명령어를 사용해서 java 파일을 직접 빌드합니다. 생성된 jar 파일을 더블 클릭하여 실행할 수 있을 것입니다.
``` bash
javac PasswdGen.java
jar -cmf Manifest.mf passwdGen.jar *.class
```

#### Windows
- [compileProject.bat](https://github.com/HyeongminKim/PasswordsGenerator/blob/master/compileProject.bat) 스크립트를 사용해서 java 파일을 빌드합니다. 
- 생성된 jar 파일을 '새로운 가상본 만들기' 기능을 사용하여 아래와 같이 설정한 후 사용합니다.
```
    Target: "%javapath%\bin\javaw.exe" -jar -Dfile.encoding=ms949 passwdGen.jar
    Start in : %path%\PasswordsGenerator\PasswordsGenerator
```

### 주의사항
- Windows에서는 인코딩 문제로 화면이 제대로 표시되지 않을 수 있습니다. 이 문제가 발생할 경우 위 스크립트를 사용하여 다시 시도해 봅시다.

### Demo
#### macOS
<img width="758" alt="Screen Shot 2021-11-21 at 0 05 24" src="https://user-images.githubusercontent.com/25660580/142732280-c83b466b-8764-4a0d-bde9-ffd300e81dee.png">

#### Linux
<img width="758" alt="Capture" src="https://user-images.githubusercontent.com/25660580/142799879-7b302269-7f29-4f27-900f-cdcea1f74827.PNG">

#### Windows
<img width="758" alt="Capture" src="https://user-images.githubusercontent.com/25660580/142732286-06e0b63a-34bf-4b6c-8020-f3c4ce87e0d0.PNG">
