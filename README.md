# PasswordsGenerator
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://paypal.me/hmDonate)
### 제작 동기 및 사용방법
- 회원 가입시 매우 다양한 비밀번호 규칙으로 인해 비밀번호를 정하는데 시간이 오래걸려 이 프로그램을 제작하게 되었습니다.
- 하단 프롬프트에 `help` 를 입력하면 사용가능한 명령을 표시해 줍니다.

### 요구사항
- 프로젝트를 빌드하기 위해 JDK 또는 OpenJDK가 필요합니다. 
- 이 프로그램은 GUI를 사용하므로 CLI기반 시스템에서는 빌드할 수 없습니다. 

### 설치 방법
- **macOS 및 Linux**
  - [compileProject.sh](https://github.com/HyeongminKim/PasswordsGenerator/blob/master/compileProject.sh) 스크립트를 사용해서 java 파일을 빌드합니다.
  - 또는 아래의 명령어를 사용해서 java 파일을 직접 빌드합니다. 
    ``` bash
    javac PasswdGen.java
    jar -cmf Manifest.mf passwdGen.jar *.class
    chmod 755 passwdGen.jar
    ```
  - 생성된 jar 파일을 더블 클릭하여 실행하거나 `java -jar passwdGen.jar` 명령어를 사용해서 상세 로그를 얻을 수 있습니다.

- **Windows**
  - [compileProject.bat](https://github.com/HyeongminKim/PasswordsGenerator/blob/master/compileProject.bat) 스크립트를 사용해서 java 파일을 빌드합니다. 
  - 생성된 jar 파일을 '새로운 가상본 만들기' 기능을 사용하여 아래와 같이 설정한 후 사용합니다.
    ```
        Target: "%javapath%\bin\javaw.exe" -jar -Dfile.encoding=ms949 passwdGen.jar
        Start in : %path%\PasswordsGenerator\PasswordsGenerator
    ```
  - 또는 `java -jar -Dfile.encoding=ms949 passwdGen.jar` 명령어를 사용해서 상세 로그를 얻을 수 있습니다. 

### 주의사항
- Windows에서는 인코딩 문제로 화면이 제대로 표시되지 않을 수 있습니다. 이 문제가 발생할 경우 상단의 제공된 스크립트를 사용하여 다시 시도해 봅시다.

### 문제 해결
- **컴파일 도구**
  - `FATAL ERROR!`: 프로젝트 구조가 손상되어 있습니다. 
  - `ERROR! No GUI detected`: GUI가 감지되지 않았습니다. 현재 사용하고 계신 컴퓨터가 GUI를 지원하는지, 또 컴퓨터가 GUI를 지원하지만 시스템이 CLI 기반으로 운영되고 있거나, WSL(aka. Windows Subsystem for Linux)에서 빌드를 시도하진 않았는지 다시 한번 확인하세요. 
  - `ERROR! This script only support...`: 지원하지 않는 OS이거나 컴파일 도구를 잘못 실행했습니다. 
  - `ERROR! This script requires JDK`: 계속 진행하려면 JDK가 있어야 합니다. [여기](https://github.com/HyeongminKim/PasswordsGenerator#%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD)를 참조하세요. 
  - `jar file couldn't be created`: 프로그램이 이미 열려있어서 쓰기 권한이 없거나 .class 파일을 찾을 수 없습니다. 
- **프로그램 에러**
  - The file passwdGen.jar isn't marked as executable: `chmod 755 passwdGen.jar`를 터미널에서 실행한 후 다시 시도하세요. 
  - 알아볼 수 없는 □ 문자가 표시될 경우 [여기](https://github.com/HyeongminKim/PasswordsGenerator#%EC%A3%BC%EC%9D%98%EC%82%AC%ED%95%AD)를 참조하세요. 
  - 제보를 권장하는 예외가 발생했을 경우 [여기로 제보](https://github.com/HyeongminKim/PasswordsGenerator/issues)해 주세요. 

### Demo
<img width="758" alt="Screen Shot 2021-11-21 at 0 05 24" src="https://user-images.githubusercontent.com/25660580/142732280-c83b466b-8764-4a0d-bde9-ffd300e81dee.png">

