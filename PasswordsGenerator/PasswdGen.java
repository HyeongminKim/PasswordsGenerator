import java.net.*;
import java.time.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.IOException;

import javax.swing.*;

public class PasswdGen extends WindowAdapter {
    private Frame mainView, resultView;
    private Panel header, passCnt, context, action, footer;
    private Label welcomeText, pwdCnt, placeholderText, cmdPrompt;
    private TextField passwordCount;
    private JTextField cmdInput;
    private Checkbox containCaps, containNum, containUniqueSymbol;
    private Button generate, clear, openWeb;
    private TextArea resultOutput;
    private boolean generated, exceptSimilarSymbol, generalSymbol;
    private int createPassword = 1;
    private String similarSymbol = "1l|Ii!joO0;:9gqxX.,", exceptUniqueSymbol = "";

    public PasswdGen() {
        init();
        setAction();
    }

    private String truncate(String value, int length) {
        if(value.length() > length) {
            return value.substring(0, length - 3) + "...";
        } else {
            return value;
        }
    }

    private String generatePassword(Frame target, int length, boolean caps, boolean num, boolean unique) throws NumberFormatException {
        String result = "";

        if(length <= 0 || length > 256) {
            throw new NumberFormatException("For input number: " + length);
        }

        if(length < 8 || !(caps && num && unique)) {
            formatedLogcat("WRN", "비밀번호 정책이 규정에 부합하지 않음");
            showAlert(target, mainView.getTitle() + "- 경고", "현재 설정된 비밀번호 정책은 대부분의 사이트에서 허용하지 않으며 보안에 치명적이므로 이 비밀번호를 사용하는 것은 권장하지 않습니다. ");
        }

        int resultCount = 0;
        while (true) {
            if (resultCount == length) break;

            int trigger = (int)(Math.random() * 3 + 1);
            switch(trigger) {
                case 1:
                    if(caps) {
                        int select = (int)(Math.random() * 2 + 1);
                        char table = select == 1 ? (char)(Math.random() * 26 + 65) : (char)(Math.random() * 26 + 97);
                        boolean flag = false;
                        if(exceptSimilarSymbol) {
                            for(int i = 0; i < similarSymbol.length(); i++) {
                                if(similarSymbol.charAt(i) == table) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag) continue;
                        }
                        result += table;
                    } else {
                        char table = (char)(Math.random() * 26 + 97);
                        boolean flag = false;
                        if(exceptSimilarSymbol) {
                            for(int i = 0; i < similarSymbol.length(); i++) {
                                if(similarSymbol.charAt(i) == table) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag) continue;
                        }
                        result += table;
                    }
                    resultCount++;
                    break;
                case 2:
                    if(num) {
                        char table = (char)(Math.random() * 10 + 48);
                        boolean flag = false;
                        if(exceptSimilarSymbol) {
                            for(int i = 0; i < similarSymbol.length(); i++) {
                                if(similarSymbol.charAt(i) == table) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag) continue;
                        }
                        result += table;
                        resultCount++;
                    }
                    break;
                case 3:
                    if(unique && !generalSymbol) {
                        int select = (int)(Math.random() * 4 + 1);
                        char table;
                        boolean flag = false;
                        switch(select) {
                            case 1:
                                table = (char)(Math.random() * 15 + 33);
                                if(exceptSimilarSymbol) {
                                    for(int i = 0; i < similarSymbol.length(); i++) {
                                        if(similarSymbol.charAt(i) == table) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(flag) continue;
                                }
                                result += table;
                                break;
                            case 2:
                                table = (char)(Math.random() * 7 + 58);
                                if(exceptSimilarSymbol) {
                                    for(int i = 0; i < similarSymbol.length(); i++) {
                                        if(similarSymbol.charAt(i) == table) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(flag) continue;
                                }
                                result += table;
                                break;
                            case 3:
                                table = (char)(Math.random() * 6 + 91);
                                if(exceptSimilarSymbol) {
                                    for(int i = 0; i < similarSymbol.length(); i++) {
                                        if(similarSymbol.charAt(i) == table) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(flag) continue;
                                }
                                result += table;
                                break;
                            case 4:
                                table = (char)(Math.random() * 4 + 123);
                                if(exceptSimilarSymbol) {
                                    for(int i = 0; i < similarSymbol.length(); i++) {
                                        if(similarSymbol.charAt(i) == table) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(flag) continue;
                                }
                                result += table;
                                break;
                        }
                        resultCount++;
                    } else if(unique && generalSymbol) {
                        int select = (int)(Math.random() * exceptUniqueSymbol.length() + 1);
                        result += exceptUniqueSymbol.charAt(select - 1);
                        resultCount++;
                    }
                    break;
            }
        }

        return result;
    }

    private void showAlert(Frame target, String title, String message) {
        Dialog alert = new Dialog(target, title, true);
        alert.setSize(300, 90);
        alert.setTitle(title);
        alert.setLocation(50, 50);
        alert.setLayout(new BorderLayout(1, 2));

        Label msg = new Label(message, Label.CENTER);
        Button submit = new Button("승인");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                formatedLogcat("INFO", "알럿이 비활성화됨: [" + title + "] " + truncate(message, 14));
                alert.dispose();
            }
        });

        alert.add("North", msg);
        alert.add("South", submit);
        alert.pack();
        formatedLogcat("INFO", "알럿이 활성화됨: [" + title + "] " + truncate(message, 14));
        alert.setVisible(true);
    }

    private void formatedLogcat(String level, String log, boolean newline) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        if(newline) {
            resultOutput.append("\n" + date + " " + time + " [" + level + "]: " + log);
        } else {
            resultOutput.append(date + " " + time + " [" + level + "]: " + log);
        }
    }

    private void formatedLogcat(String level, String log) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        resultOutput.append("\n" + date + " " + time + " [" + level + "]: " + log);
    }

    private void init() {
        mainView = new Frame("비밀번호 생성기");
        header = new Panel();
        passCnt = new Panel();
        context = new Panel();
        action = new Panel();
        footer = new Panel();

        welcomeText = new Label("비밀번호 생성기에 오신것을 환영합니다!");
        pwdCnt = new Label("아래 규칙을 설정하여 조건에 맞는 비밀번호를 생성하세요");

        placeholderText = new Label("비밀번호 생성 길이: ");
        cmdPrompt = new Label("> ");

        passwordCount = new TextField("5", 5);
        cmdInput = new JTextField(70);

        containCaps = new Checkbox("대소문자 구분", false);
        containNum  = new Checkbox("숫자 포함", false);
        containUniqueSymbol = new Checkbox("특수문자 포함", false);

        generate = new Button("생성");
        clear = new Button("지우기");
        openWeb = new Button("GitHub");

        resultOutput = new TextArea();
        formatedLogcat("INFO", "passwdGen 초기화... [" + mainView.getTitle() + "] 윈도우 생성됨", false);
        resultOutput.setEditable(false);

        header.add(welcomeText);
        header.add(pwdCnt);

        passCnt.add(placeholderText);
        passCnt.add(passwordCount);

        context.add(containCaps);
        context.add(containNum);
        context.add(containUniqueSymbol);

        action.add(generate);
        action.add(clear);

        footer.setLayout(new BorderLayout(1, 2));
        footer.add("North", resultOutput);
        Panel commandLine = new Panel();
        commandLine.setLayout(new FlowLayout(FlowLayout.LEFT));
        commandLine.add(cmdPrompt);
        commandLine.add(cmdInput);
        commandLine.add(openWeb);
        footer.add("South", commandLine);

        mainView.setSize(600, 150);
        mainView.setLayout(new BorderLayout(1, 2));
        mainView.setResizable(false);

        mainView.add("North", header);
        mainView.add("West", passCnt);
        mainView.add("Center", context);
        mainView.add("East", action);
        mainView.add("South", footer);

        mainView.pack();
        mainView.setVisible(true);
        mainView.addWindowListener(this);
    }

    private void setAction() {
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] result = new String[20];

                    passwordCount.setText(passwordCount.getText().trim());
                    if (passwordCount.getText().equals("")) {
                        throw new NumberFormatException("No length value entered.");
                    }
                    for(int i = 0; i < createPassword; i++) {
                        result[i] = generatePassword(mainView, Integer.parseInt(passwordCount.getText()), containCaps.getState(), containNum.getState(), containUniqueSymbol.getState());
                        if (result[i] == null || result[i].length() != Integer.parseInt(passwordCount.getText())) {
                            throw new NullPointerException();
                        }
                    }
                    formatedLogcat("INFO", "비밀번호가 성공적으로 생성되었습니다. ");

                    resultView = new Frame("비밀번호 생성 결과");
                    Choice resultContainer = new Choice();
                    resultContainer.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            resultContainer.repaint();
                        }

                        public void paint(Graphics g) {
                            resultContainer.paint(g);
                            g.setColor(Color.BLUE);
                            String msg = "비밀번호 선택";
                            msg += resultContainer.getSelectedItem();
                            g.drawString(msg, 30, 120);
                        }
                    });

                    for(int i = 0; i < createPassword; i++) {
                        resultContainer.add(result[i]);
                    }

                    Button copyPassword = new Button("복사 후 닫기");
                    copyPassword.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            StringSelection selection = new StringSelection(result[resultContainer.getSelectedIndex()]);
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, selection);
                            generated = true;
                            formatedLogcat("INFO", "비밀번호 생성 결과창이 비활성화 됨");
                            resultView.dispose();
                        }
                    });
                    Button dismiss = new Button("닫기");
                    dismiss.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            formatedLogcat("INFO", "비밀번호 생성 결과창이 비활성화 됨");
                            resultView.dispose();
                        }
                    });

                    Panel up = new Panel();
                    Panel down = new Panel();
                    resultView.setLayout(new BorderLayout(1, 2));
                    down.setLayout(new BorderLayout(1, 2));
                    up.add(resultContainer);
                    down.add("West", copyPassword);
                    down.add("East", dismiss);
                    resultView.add("North", up);
                    resultView.add("South", down);
                    resultView.setSize(300, 200);
                    resultView.pack();
                    resultView.setLocation(50, 50);
                    formatedLogcat("INFO", "비밀번호 생성 결과창이 활성화 됨");
                    resultView.setVisible(true);

                } catch(NumberFormatException exception) {
                    formatedLogcat("ERR", "비밀번호를 생성하는 도중 예외 발생: " + exception.toString() + "\n\t" + passwordCount.getText() + " (은)는 비밀번호 길이를 생성할 수 있는 올바른 숫자가 아닙니다. ");
                    showAlert(mainView, mainView.getTitle() + "- 오류", "비밀번호 길이 필드에는 자연수만 입력해야 합니다. 비밀번호 길이 필드를 다시 한번 확인하시길 바랍니다. ");
                } catch(NullPointerException exception) {
                    formatedLogcat("ERR", "비밀번호를 생성하는 도중 예외 발생: " + exception.toString() + "\n\t" + "이 예외를 https://github.com/HyeongminKim (@HyeongminKim) 에게 제보하세요. ");
                    showAlert(mainView, mainView.getTitle() + "- 오류", "알 수 없는 이유로 비밀번호가 생성되지 않았습니다. 주로 잘못된 규칙을 설정했거나 프로그램 버그로 인해 이 문제가 발생합니다. 설정하신 규칙과 이 메시지를 함께 스크린 샷을 촬영하여 이슈를 제보하여 주세요. ");
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!generated) {
                    formatedLogcat("ERR", "클립보드를 지울 수 없습니다. 먼저 비밀번호를 생성하시길 바랍니다. ");
                    showAlert(mainView, mainView.getTitle() + "- 오류", "비밀번호를 한 번도 복사하지 않았거나 이미 클립보드가 비워진 상태입니다. ");;
                } else {
                    StringSelection selection = new StringSelection("");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    generated = false;
                    resultOutput.setText(null);
                    formatedLogcat("INFO", "클립보드 지우기 완료", false);
                    showAlert(mainView, mainView.getTitle(), "생성된 비밀번호가 클립보드에서 제거되었습니다. 만약 비밀번호가 유실되었을 경우 해당 홈페이지 비밀번호 찾기 기능을 이용하시길 바랍니다. ");;
                }
            }
        });

        openWeb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = "https://github.com/HyeongminKim/PasswordsGenerator";
                try {
                    if(Desktop.isDesktopSupported()) {
                        formatedLogcat("INFO", "페이지 열기 데몬: Desktop.browse");
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(new URI(url));
                    } else {
                        Runtime rt = Runtime.getRuntime();
                        String os = System.getProperty("os.name").toLowerCase();
                        if(os.indexOf("mac") >= 0) {
                            formatedLogcat("INFO", "페이지 열기 명령(macOS): open");
                            rt.exec("open " + url);
                        } else {
                            formatedLogcat("INFO", "페이지 열기 명령(Linux): xdg-open");
                            rt.exec("xdg-open " + url);
                        }
                    }
                } catch(IOException | URISyntaxException exception) {
                    formatedLogcat("ERR", "페이지를 여는 중 예외 발생: " + exception.toString() + "\n\t수동으로 개발자 홈페이지에 방문해 주세요. " + url);
                    showAlert(mainView, mainView.getTitle() + "- 오류", "이 OS는 현재 지원하지 않습니다. 로그캣에 작성된 URL을 복사하여 수동으로 여시기 바랍니다. ");;
                }
            }
        });

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        cmdInput.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
        cmdInput.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String[] parameter = cmdInput.getText().trim().toLowerCase().split(" ");
                    switch(parameter[0]) {
                        case "help":
                            if(exceptSimilarSymbol && generalSymbol) {
                                formatedLogcat("INFO", "도움말" +
                                            "\n\t" + "help - 이 도움말 표시" +
                                            "\n\t" + "count - 생성할 갯수 (현재 " + createPassword + "개)" +
                                            "\n\t" + "exceptsym {on/off} - 비슷한 문자 제외 켜짐" +
                                            "\n\t" + "unisym {on @#!$_-/off} - 특수문자 간략화 켜짐(" + exceptUniqueSymbol + ")" +
                                            "\n\t" + "clear - 버퍼 청소" +
                                            "\n\t" + "exit - 이 프로그램 종료"
                                );
                            } else if(!exceptSimilarSymbol && generalSymbol) {
                                formatedLogcat("INFO", "도움말" +
                                            "\n\t" + "help - 이 도움말 표시" +
                                            "\n\t" + "count - 생성할 갯수 (현재 " + createPassword + "개)" +
                                            "\n\t" + "exceptsym {on/off} - 비슷한 문자 제외 꺼짐" +
                                            "\n\t" + "unisym {on @#!$_-/off} - 특수문자 간략화 켜짐(" + exceptUniqueSymbol + ")" +
                                            "\n\t" + "clear - 버퍼 청소" +
                                            "\n\t" + "exit - 이 프로그램 종료"
                                );
                            } else if(exceptSimilarSymbol && !generalSymbol) {
                                formatedLogcat("INFO", "도움말" +
                                            "\n\t" + "help - 이 도움말 표시" +
                                            "\n\t" + "count - 생성할 갯수 (현재 " + createPassword + "개)" +
                                            "\n\t" + "exceptsym {on/off} - 비슷한 문자 제외 켜짐" +
                                            "\n\t" + "unisym {on @#!$_-/off} - 특수문자 간략화 꺼짐" +
                                            "\n\t" + "clear - 버퍼 청소" +
                                            "\n\t" + "exit - 이 프로그램 종료"
                                );
                            } else {
                                formatedLogcat("INFO", "도움말" +
                                            "\n\t" + "help - 이 도움말 표시" +
                                            "\n\t" + "count - 생성할 갯수 (현재 " + createPassword + "개)" +
                                            "\n\t" + "exceptsym {on/off} - 비슷한 문자 제외 꺼짐" +
                                            "\n\t" + "unisym {on @#!$_-/off} - 특수문자 간략화 꺼짐" +
                                            "\n\t" + "clear - 버퍼 청소" +
                                            "\n\t" + "exit - 이 프로그램 종료"
                                );
                            }
                            break;
                        case "count":
                            try {
                                int input = Integer.parseInt(parameter[1].trim());
                                if(input > 0 && input < 17) {
                                    createPassword = input;
                                    formatedLogcat("INFO", "비밀번호 생성 수가 성공적으로 변경됨");
                                } else {
                                    throw new NumberFormatException("For input number: " + input);
                                }
                            } catch (NumberFormatException e) {
                                formatedLogcat("ERR", "비밀번호 생성 수를 설정하는 중 예외 발생: " + e.toString() + "\n\t" + parameter[1].trim() + " (은)는 비밀번호 생성 수를 설정할 수 있는 올바른 숫자가 아닙니다. ");
                            }
                            break;
                        case "exceptsym":
                            if(!parameter[1].equals("on") && !parameter[1].equals("off")) {
                                throw new NullPointerException("Command not found.");
                            }

                            if(parameter[1].equals("on")) {
                                exceptSimilarSymbol = true;
                                formatedLogcat("INFO", "비슷한 문자 제외 기능이 활성화 되었음");
                            } else {
                                exceptSimilarSymbol = false;
                                formatedLogcat("INFO", "비슷한 문자 제외 기능이 비활성화 되었음");
                            }
                            break;
                        case "unisym":
                            if(!parameter[1].equals("on") && !parameter[1].equals("off")) {
                                throw new NullPointerException("Command not found.");
                            }

                            if(parameter[1].equals("on") && !parameter[2].equals("")) {
                                boolean isEdited = false;
                                StringBuilder convert = new StringBuilder(parameter[2].toLowerCase());
                                boolean flag = false;
                                for(int i = 0; i < convert.length(); i++) {
                                    char selectedSymbol = convert.charAt(i);
                                    if(selectedSymbol > 96 && selectedSymbol < 123) {
                                        convert.deleteCharAt(i);
                                        i--;
                                        isEdited = true;
                                        continue;
                                    }
                                    if(selectedSymbol > -1 && selectedSymbol < 33) {
                                        convert.deleteCharAt(i);
                                        i--;
                                        isEdited = true;
                                       continue;
                                    }
                                    if(selectedSymbol > 47 && selectedSymbol < 58) {
                                        convert.deleteCharAt(i);
                                        i--;
                                        isEdited = true;
                                        continue;
                                    }
                                    if(selectedSymbol == 127) {
                                        convert.deleteCharAt(i);
                                        i--;
                                        isEdited = true;
                                        continue;
                                    }

                                    for(int j = i; j < convert.length(); j++) {
                                        if(convert.charAt(i) == convert.charAt(j) && i != j) {
                                            convert.deleteCharAt(j);
                                            j--;
                                            isEdited = true;
                                        } 
                                    }

                                    if(exceptSimilarSymbol) {
                                        for(int j = 0; j < convert.length(); j++) {
                                            for(int k = 0; k < similarSymbol.length(); k++) {
                                                if(convert.toString().charAt(j) == similarSymbol.charAt(k)) {
                                                    flag = true;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (flag) {
                                    formatedLogcat("WRN", "비슷한 문자 제외기능이 활성화 되어 있으므로 " + similarSymbol + " 문자들은 비밀번호 조합시 사용하지 않을 예정입니다. ");
                                }

                                if(isEdited) {
                                    exceptUniqueSymbol = convert.toString();
                                } else {
                                    exceptUniqueSymbol = parameter[2];
                                }

                                if(convert.length() == 0) {
                                    generalSymbol = false;
                                    containUniqueSymbol.setLabel("특수문자 포함");
                                    throw new NullPointerException("두번째 파라미터에는 특수문자만 제공해야 합니다. ");
                                } else if (isEdited && !flag) {
                                    formatedLogcat("INFO", "특수문자 선별기능이 활성화 되었지만 적합하지 않은 문자를 제외하고 다음 문자만 적용됨: " + exceptUniqueSymbol);
                                } else if (isEdited && flag) {
                                    formatedLogcat("INFO", "특수문자 선별기능이 활성화 되었지만 적합하지 않은 문자를 제외하고도 다음 문자가 무시될 수 있음: " + exceptUniqueSymbol);
                                } else if (!isEdited && flag) {
                                    formatedLogcat("INFO", "특수문자 선별기능이 활성화 되었지만 다음 문자가 무시될 수 있음: " + exceptUniqueSymbol);
                                } else {
                                    formatedLogcat("INFO", "특수문자 선별기능이 활성화 되었으며 다음 문자만 표시됨: " + exceptUniqueSymbol);
                                }
                                generalSymbol = true;
                                containUniqueSymbol.setLabel("일부 특수문자 포함");
                            } else if(parameter[1].equals("on") && parameter[2].equals("")) {
                                generalSymbol = false;
                                containUniqueSymbol.setLabel("특수문자 포함");
                                throw new NullPointerException("The parameter options are ambiguous.");
                            } else {
                                generalSymbol = false;
                                exceptUniqueSymbol = "";
                                containUniqueSymbol.setLabel("특수문자 포함");
                                formatedLogcat("INFO", "특수문자 선별기능이 비활성화 되었음");
                            }
                            break;
                        case "clear":
                            resultOutput.setText(null);
                            break;
                        case "":
                            break;
                        case "exit":
                            System.exit(0);
                            break;
                        default:
                            throw new NullPointerException("Command not found.");
                    }
                } catch(NullPointerException e) {
                    formatedLogcat("ERR", cmdInput.getText() + ": 명령어를 실행하는 도중 예외가 발생하였습니다. " + e.toString());
                } finally {
                    cmdInput.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        new PasswdGen();
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        System.exit(0);
    }
}
