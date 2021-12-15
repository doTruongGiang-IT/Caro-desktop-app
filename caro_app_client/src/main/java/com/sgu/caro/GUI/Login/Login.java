package com.sgu.caro.GUI.Login;

import com.sgu.caro.GUI.MainScreen.MainScreenDesign;
import com.sgu.caro.GUI.WindowManager;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sgu.caro.api_connection.DataAPI;
import com.sgu.caro.api_connection.TokenManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login {

    JFrame jframe;
    JButton loginButton;
    JTextField email;
    JPasswordField password;
    JLabel usernameError;
    JLabel passwordError;
    JLabel picLabel;
    DataAPI dataAPI = new DataAPI();

    public Login() throws IOException {
        BufferedImage myPicture = ImageIO.read(new File("./images/logo.png"));
        ImageIcon newImage = new ImageIcon(myPicture);
        Image image = newImage.getImage();
        Image newimg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        newImage = new ImageIcon(newimg);
        picLabel = new JLabel(newImage);

        jframe = new JFrame("Đăng nhập");
        email = new JTextField() {
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(Color.decode("#99f1f5"));
                    g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                            0, 0, getWidth() - 1, getHeight() - 1));
                    g2.dispose();
                }
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setOpaque(false);
                setBorder(new RoundedCornerBorder());
            }
        };

        password = new JPasswordField() {
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(Color.decode("#99f1f5"));
                    g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                            0, 0, getWidth() - 1, getHeight() - 1));
                    g2.dispose();
                }
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setOpaque(false);
                setBorder(new RoundedCornerBorder());
            }
        };

        loginButton = new JButton("LOGIN") {
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(getBackground());
                    g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                            0, 0, getWidth() - 1, getHeight() - 1));
                    g2.dispose();
                }
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setOpaque(false);
                setBorder(new RoundedCornerBorder());
            }
        };
        usernameError = new JLabel();
        passwordError = new JLabel();

        init();
    }

    public void addEventListeners() {
        //submit button action listener
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String API_URL = TokenManager.getHOST() + "/caro_api/auth";

                String requestData = dataAPI.exportLoginAPI(email.getText(), String.valueOf(password.getPassword()));
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(API_URL))
                            .headers("Content-Type", "application/json;charset=UTF-8")
                            .POST(HttpRequest.BodyPublishers.ofString(requestData))
                            .build();
                    HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject responseData = dataAPI.importData(response.body().toString());
                    System.out.println(response.body().toString());

                    if (responseData.has("access_token")) {
                        String jwt = responseData.getString("access_token");
                        int userId = Integer.parseInt(responseData.getString("user_id"));
                        System.out.println("access_token: " + jwt);
                        TokenManager.setJwt(jwt);
                        TokenManager.setUser_id(userId);
                        WindowManager.mainScreen = new MainScreenDesign();
                        WindowManager.mainScreen.setVisible(true);
                        jframe.setVisible(false);
                    } else {
                        usernameError.setForeground(Color.RED);
                        usernameError.setText("Đăng nhập thất bại");
                        passwordError.setText("");
                    }
                } catch (URISyntaxException e1) {
                } catch (IOException e2) {
                } catch (InterruptedException e2) {
                }
            }

        });

        //email validation listener
        email.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                if (email.getText().length() > 0 && !email.getText().equals("Nhập email")) {
                    if (validateMail(email.getText())) {
                        usernameError.setForeground(new Color(50, 168, 58));
                        usernameError.setText("Email hợp lệ");
                    } else {
                        usernameError.setForeground(Color.RED);
                        usernameError.setText("Email không hợp lệ");
                    }
                } else {
                    usernameError.setText("");
                }
            }

            public void insertUpdate(DocumentEvent e) {
                if (email.getText().length() > 0 && !email.getText().equals("Nhập email")) {
                    if (validateMail(email.getText())) {
                        usernameError.setForeground(new Color(50, 168, 58));
                        usernameError.setText("Email hợp lệ");
                    } else {
                        usernameError.setForeground(Color.RED);
                        usernameError.setText("Email không hợp lệ");
                    }
                } else {
                    usernameError.setText("");
                }
            }

            public void changedUpdate(DocumentEvent e) {
                if (email.getText().length() > 0 && !email.getText().equals("Nhập email")) {
                    if (validateMail(email.getText())) {
                        usernameError.setForeground(new Color(50, 168, 58));
                        usernameError.setText("Email hợp lệ");
                    } else {
                        usernameError.setForeground(Color.RED);
                        usernameError.setText("Email không hợp lệ");
                    }
                } else {
                    usernameError.setText("");
                }
            }
        });

        //password validation listener
        password.getDocument().addDocumentListener(new DocumentListener() {

            @SuppressWarnings("deprecation")
            public void removeUpdate(DocumentEvent e) {
                if (password.getText().length() > 0 && !password.getText().equals("Nhập mật khẩu")) {
                    if (validatePassword(password.getText())) {
                        passwordError.setForeground(new Color(50, 168, 58));
                        passwordError.setText("Mật khẩu hợp lệ");
                    }
                } else {
                    passwordError.setText("");
                }
            }

            @SuppressWarnings("deprecation")
            public void insertUpdate(DocumentEvent e) {
                if (password.getText().length() > 0 && !password.getText().equals("Nhập mật khẩu")) {
                    if (validatePassword(password.getText())) {
                        passwordError.setForeground(new Color(50, 168, 58));
                        passwordError.setText("Mật khẩu hợp lệ");
                    }
                } else {
                    passwordError.setText("");
                }
            }

            @SuppressWarnings("deprecation")
            public void changedUpdate(DocumentEvent e) {
                if (password.getText().length() > 0 && !password.getText().equals("Nhập mật khẩu")) {
                    if (validatePassword(password.getText())) {
                        passwordError.setForeground(new Color(50, 168, 58));
                        passwordError.setText("Mật khẩu hợp lệ");
                    }
                } else {
                    passwordError.setText("");
                }
            }
        });

        email.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                if (email.getText().equals("")) {
                    email.setText("Nhập email");
                    email.setForeground(Color.gray);
                }
            }

            public void focusGained(FocusEvent e) {
                if (email.getText().equals("Nhập email")) {
                    email.setText("");
                    email.setForeground(Color.black);
                }
            }
        });

        password.addFocusListener(new FocusListener() {

            @SuppressWarnings("deprecation")
            public void focusLost(FocusEvent e) {
                if (password.getText().equals("")) {
                    password.setText("Nhập mật khẩu");
                    password.setForeground(Color.gray);
                    password.setEchoChar((char) 0);
                }
            }

            @SuppressWarnings("deprecation")
            public void focusGained(FocusEvent e) {
                if (password.getText().equals("Nhập mật khẩu")) {
                    password.setText("");
                    password.setEchoChar('*');
                    password.setForeground(Color.black);
                }
            }
        });
    }

    private boolean validateMail(String mail) {
        String regExp = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regExp);
        return pattern.matcher(mail).matches();
    }

    private boolean validatePassword(String text) {
        passwordError.setForeground(Color.RED);
        if (text.length() < 8) {
            passwordError.setText("Độ dài mật khẩu ít nhất là 8");
            return false;
        } else if (!text.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
            passwordError.setText("Mật khẩu phải có ký tự alphabel");
            return false;
        } else {
            return true;
        }
    }

    public void init() {
        email.setPreferredSize(new Dimension(250, 35));
        password.setPreferredSize(new Dimension(250, 35));
        loginButton.setPreferredSize(new Dimension(250, 35));
        loginButton.setBackground(Color.decode("#00649F"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        email.setText("Nhập email");
        email.setForeground(Color.gray);
        password.setText("Nhập mật khẩu");
        password.setForeground(Color.gray);
        password.setEchoChar((char) 0);

        usernameError.setFont(new Font("SansSerif", Font.BOLD, 11));
        usernameError.setForeground(Color.RED);

        passwordError.setFont(new Font("SansSerif", Font.BOLD, 11));
        passwordError.setForeground(Color.RED);

        jframe.setLayout(new GridBagLayout());
        jframe.getContentPane().setBackground(Color.WHITE);
        Insets textInsets = new Insets(10, 10, 5, 10);
        Insets buttonInsets = new Insets(20, 10, 10, 10);
        Insets errorInsets = new Insets(0, 20, 0, 0);

        GridBagConstraints input = new GridBagConstraints();
        input.anchor = GridBagConstraints.CENTER;
        input.gridy = 1;
        jframe.add(picLabel, input);

        input.anchor = GridBagConstraints.CENTER;
        input.insets = textInsets;
        input.gridy = 2;
        jframe.add(email, input);

        input.gridy = 3;
        input.insets = errorInsets;
        input.anchor = GridBagConstraints.WEST;
        jframe.add(usernameError, input);

        input.gridy = 4;
        input.insets = textInsets;
        input.anchor = GridBagConstraints.CENTER;
        jframe.add(password, input);

        input.gridy = 5;
        input.insets = errorInsets;
        input.anchor = GridBagConstraints.WEST;
        jframe.add(passwordError, input);

        input.insets = buttonInsets;
        input.anchor = GridBagConstraints.WEST;
        input.gridx = 0;
        input.gridy = 6;
        jframe.add(loginButton, input);

        jframe.setSize(350, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);

        jframe.requestFocus();
        addEventListeners();
    }

    public static void main(String args[]) {
        try {
            new Login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
