import java.time.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

public class passwdGen implements WindowListener {
    private Frame frame;
    private Panel header, passCnt, context, action, footer;
    private Label welcomeText, pwdCnt, placeholderText;
    private TextField passwordCount;
    private Checkbox containCaps, containNum, containUniqueSymbol;
    private Button generate, clear;
    private TextArea guideText, resultOutput;
    private boolean generated;

    public passwdGen() {
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

        if(length <= 0) {
            throw new NumberFormatException();
        }

        if(length < 8 || !(caps && num && unique)) {
            resultOutput.append("\n" + formatedLogcat("WRN", "비밀번호 정책이 규정에 부합하지 않음"));
            showAlert(target, frame.getTitle() + "- 경고", "현재 설정된 비밀번호 정책은 대부분의 사이트에서 허용하지 않으며 보안에 치명적이므로 이 비밀번호를 사용하는 것은 권장하지 않습니다. ");
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
                        result += table;
                    } else {
                        char table = (char)(Math.random() * 26 + 97);
                        result += table;
                    }
                    resultCount++;
                    break;
                case 2:
                    if(num) {
                        char table = (char)(Math.random() * 10 + 48);
                        result += table;
                        resultCount++;
                    }
                    break;
                case 3:
                    if(unique) {
                        int select = (int)(Math.random() * 4 + 1);
                        char table;
                        switch(select) {
                            case 1:
                                table = (char)(Math.random() * 15 + 33);
                                result += table;
                                break;
                            case 2:
                                table = (char)(Math.random() * 7 + 58);
                                result += table;
                                break;
                            case 3:
                                table = (char)(Math.random() * 6 + 91);
                                result += table;
                                break;
                            case 4:
                                table = (char)(Math.random() * 4 + 123);
                                result += table;
                                break;
                        }
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
                resultOutput.append("\n" + formatedLogcat("INFO", "알럿이 비활성화됨: [" + title + "] " + truncate(message, 10)));
                alert.dispose();
            }
        });

        
        alert.add("North", msg);
        alert.add("South", submit);
        alert.pack();
        resultOutput.append("\n" + formatedLogcat("INFO", "알럿이 활성화됨: [" + title + "] " + truncate(message, 10)));
        alert.setVisible(true);
    }

    private String formatedLogcat(String level, String log) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        
        return date + " " + time + " [" + level + "]: " + log;
    }

    private void init() {
        frame = new Frame("비밀번호 생성기");
        header = new Panel();
        passCnt = new Panel();
        context = new Panel();
        action = new Panel();
        footer = new Panel();

        welcomeText = new Label("비밀번호 생성기에 오신것을 환영합니다!");
        pwdCnt = new Label("아래 규칙을 설정하여 조건에 맞는 비밀번호를 생성하세요");

        placeholderText = new Label("비밀번호 생성 길이: ");

        passwordCount = new TextField("5", 5);

        containCaps = new Checkbox("대소문자 구분", false);
        containNum  = new Checkbox("숫자 포함", false);
        containUniqueSymbol = new Checkbox("특수문자 포함", false);

        generate = new Button("생성");
        clear = new Button("지우기");

        guideText = new TextArea(
            "--- 무차별 대입공격에 의한 해킹으로부터 계정을 보호하려면 다음 사항에 주의하세요. ---" + "\n"
            + "- 다수의 계정에 대해 동일하거나 유사한 비밀번호를 사용하지 마세요. " + "\n"
            + "- 최소 8자리의 비밀번호를 사용하고 숫자, 대소문자, 특수기호를 하나 이상 포함하세요. " + "\n"
            + "- 비밀번호에 우편번호, 전화번호, 생일 등 의미가 있는 숫자를 사용하지 마세요. " + "\n"
            + "- 쉽게 추측할 수 있는 비밀번호를 사용하지 마세요. " + "\n"
            + "- 브라우저나 포스트잇에 저장된 모든 비밀번호는 쉽게 확인할 수 있으니 주의하세요. " + "\n"
            + "- 공개 Wi-Fi, 무료 VPN, 다른 컴퓨터나 웹 프록시의 연결을 신뢰할 수 없다면 로그인하지 마세요. " + "\n"
            + "- 가급적 암호화 되지 않은 연결을 통해 민감한 정보를 전송하지 마세요. " + "\n"
        );
        guideText.setEditable(false);

        resultOutput = new TextArea(formatedLogcat("INFO", "passwdGen 초기화... [" + frame.getTitle() + "] 윈도우 생성됨"));
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
        footer.add("North", guideText);
        footer.add("South", resultOutput);

        frame.setSize(600, 150);
        frame.setLayout(new BorderLayout(1, 2));

        frame.add("North", header);
        frame.add("West", passCnt);
        frame.add("Center", context);
        frame.add("East", action);
        frame.add("South", footer);

        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(this);
    }

    private void setAction() {
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String result = generatePassword(frame, Integer.parseInt(passwordCount.getText()), containCaps.getState(), containNum.getState(), containUniqueSymbol.getState());
                    if (result == null) {
                        throw new NullPointerException();
                    }
                    StringSelection selection = new StringSelection(result);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    generated = true;
                    resultOutput.append("\n" + formatedLogcat("INFO", "비밀번호가 성공적으로 생성되었습니다. "));
                    showAlert(frame, frame.getTitle(), "생성된 비밀번호는: " + result + " 입니다. 이 비밀번호는 클립보드에 이미 저장되었으므로 스크린 샷을 따로 촬영하지 않아도 됩니다. ");
                } catch(NumberFormatException exception) {
                    resultOutput.append("\n" + formatedLogcat("ERR", "비밀번호를 생성하는 도중 예외 발생: " + passwordCount.getText() + " (은)는 비밀번호 길이를 생성할 수 있는 올바른 숫자가 아닙니다. " + "\n" + exception.getStackTrace()));
                    showAlert(frame, frame.getTitle() + "- 오류", "비밀번호 길이 필드에는 자연수만 입력해야 합니다. 비밀번호 길이 필드를 다시 한번 확인하시길 바랍니다. ");
                } catch(NullPointerException exception) {
                    resultOutput.append("\n" + formatedLogcat("ERR", "비밀번호를 생성하는 도중 예외 발생: " + "이 예외를 https://github.com/HyeongminKim (@HyeongminKim) 에게 제보하세요. " + "\n" + exception.getStackTrace()));
                    showAlert(frame, frame.getTitle() + "- 오류", "알 수 없는 이유로 비밀번호가 생성되지 않았습니다. 주로 잘못된 규칙을 설정했거나 프로그램 버그로 인해 이 문제가 발생합니다. 설정하신 규칙과 이 메시지를 함께 스크린 샷을 촬영하여 이슈를 제보하여 주세요. ");
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!generated) {
                    resultOutput.append("\n" + formatedLogcat("ERR", "클립보드를 지울 수 없습니다. 먼저 비밀번호를 생성하시길 바랍니다. "));
                    showAlert(frame, frame.getTitle() + "- 오류", "비밀번호를 한 번도 생성하지 않았거나 이미 클립보드가 비워진 상태입니다. ");;
                } else {
                    StringSelection selection = new StringSelection("");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    generated = false;
                    resultOutput.setText(formatedLogcat("INFO", "클립보드 지우기 완료"));
                    showAlert(frame, frame.getTitle(), "생성된 비밀번호가 클립보드에서 제거되었습니다. 만약 비밀번호가 유실되었을 경우 해당 홈페이지 비밀번호 찾기 기능을 이용하시길 바랍니다. ");;
                }
            }
        });
    }

    public static void main(String[] args) {
        new passwdGen();
    }

    @Override
    public void windowActivated(WindowEvent arg0) { }

    @Override
    public void windowClosed(WindowEvent arg0) { }

    @Override
    public void windowClosing(WindowEvent arg0) {
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) { }

    @Override
    public void windowDeiconified(WindowEvent arg0) { }

    @Override
     public void windowIconified(WindowEvent arg0) { }

    @Override
    public void windowOpened(WindowEvent arg0) { }
}
