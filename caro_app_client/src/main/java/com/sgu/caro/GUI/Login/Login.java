package com.sgu.caro.GUI.Login;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;


public class Login{
	JFrame jframe;
	JButton loginButton;
	JTextField email;
	JPasswordField password;
	JLabel usernameError;
	JLabel passwordError;
	
	public Login() throws IOException {
		jframe = new JFrame("Đăng nhập");
		email = new JTextField() {
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
		password = new JPasswordField() {
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
				
			}
			
			
		});
		
		//email validation listener
		email.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				if(email.getText().length() > 0 && !email.getText().equals("Nhập email")) {
					if(validateMail(email.getText())) {
						usernameError.setForeground(new Color(50, 168, 58));
						usernameError.setText("Email hợp lệ");
					}
					else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("Email không hợp lệ");
					}
				}
				else {
					usernameError.setText("");
				}
			}
			
			public void insertUpdate(DocumentEvent e) {
				if(email.getText().length() > 0 && !email.getText().equals("Nhập email")) {
					if(validateMail(email.getText())) {
						usernameError.setForeground(new Color(50, 168, 58));
						usernameError.setText("Email hợp lệ");
					}
					else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("Email không hợp lệ");
					}
				}
				else {
					usernameError.setText("");
				}
			}
			
			public void changedUpdate(DocumentEvent e) {
				if(email.getText().length() > 0  && !email.getText().equals("Nhập email")) {
					if(validateMail(email.getText())) {
						usernameError.setForeground(new Color(50, 168, 58));
						usernameError.setText("Email hợp lệ");
					}
					else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("Email không hợp lệ");
					}
				}
				else {
					usernameError.setText("");
				}
			}
		});
		
		
		
		
		
		//password validation listener
		password.getDocument().addDocumentListener(new DocumentListener() {
			
			@SuppressWarnings("deprecation")
			public void removeUpdate(DocumentEvent e) {
				if(password.getText().length() > 0  && !password.getText().equals("Nhập mật khẩu")) {
					if(validatePassword(password.getText())) {
						passwordError.setForeground(new Color(50, 168, 58));
						passwordError.setText("Mật khẩu hợp lệ");
					}
				}
				else {
					passwordError.setText("");
				}
			}
			
			@SuppressWarnings("deprecation")
			public void insertUpdate(DocumentEvent e) {
				if(password.getText().length() > 0  && !password.getText().equals("Nhập mật khẩu")) {
					if(validatePassword(password.getText())) {
						passwordError.setForeground(new Color(50, 168, 58));
						passwordError.setText("Mật khẩu hợp lệ");
					}
				}
				else {
					passwordError.setText("");
				}
			}
			
			@SuppressWarnings("deprecation")
			public void changedUpdate(DocumentEvent e) {
				if(password.getText().length() > 0  && !password.getText().equals("Nhập mật khẩu")) {
					if(validatePassword(password.getText())) {
						passwordError.setForeground(new Color(50, 168, 58));
						passwordError.setText("Mật khẩu hợp lệ");
					}
				}
				else {
					passwordError.setText("");
				}
			}
		});
		
		email.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {
				if(email.getText().equals("")) {
					email.setText("Nhập email");
					email.setForeground(Color.gray);
				}
			}
			
			public void focusGained(FocusEvent e) {
				if(email.getText().equals("Nhập email")) {
					email.setText("");
					email.setForeground(Color.black);
				}
			}
		});
		
		password.addFocusListener(new FocusListener() {
			
			@SuppressWarnings("deprecation")
			public void focusLost(FocusEvent e) {
				if(password.getText().equals("")) {
					password.setText("Nhập mật khẩu");
					password.setForeground(Color.gray);
					password.setEchoChar((char)0);
				}
			}
			
			@SuppressWarnings("deprecation")
			public void focusGained(FocusEvent e) {
				if(password.getText().equals("Nhập mật khẩu")) {
					password.setText("");
					password.setEchoChar('*');
					password.setForeground(Color.black);
				}
			}
		});
	}
	
	private boolean validateMail(String mail) {
		String regExp = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                        "[a-zA-Z0-9_+&*-]+)*@" + 
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                        "A-Z]{2,7}$";
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(mail).matches();
	}
	
	private boolean validatePassword(String text) {
		passwordError.setForeground(Color.RED);
		if(text.length() < 8) {
			passwordError.setText("Độ dài mật khẩu ít nhất là 8");
			return false;
		}
		else if(!text.matches(".*[a-zA-Z]+.*")) {
			passwordError.setText("Mật khẩu phải có ký tự alphabel");
			return false;
		}
		
		else 
			return true;
	}
	
	
	
	public void init() {
		email.setPreferredSize(new Dimension(250,35));
		password.setPreferredSize(new Dimension(250,35));
		loginButton.setPreferredSize(new Dimension(250,35));
		loginButton.setBackground(new Color(0, 186, 250));
		loginButton.setFocusPainted(false);
		
		email.setText("Nhập email");
		email.setForeground(Color.gray);
		password.setText("Nhập mật khẩu");
		password.setForeground(Color.gray);
		password.setEchoChar((char)0);
		
		usernameError.setFont(new Font("SansSerif", Font.BOLD, 11));
		usernameError.setForeground(Color.RED);
		
		passwordError.setFont(new Font("SansSerif", Font.BOLD, 11));
		passwordError.setForeground(Color.RED);
		
		jframe.setLayout(new GridBagLayout());
		
		Insets textInsets = new Insets(10, 10, 5, 10);
		Insets buttonInsets = new Insets(20, 10, 10, 10);
		Insets errorInsets = new Insets(0,20,0,0);
		
		GridBagConstraints input = new GridBagConstraints();
		input.anchor = GridBagConstraints.CENTER;
		input.insets = textInsets;
		input.gridy = 1;
		jframe.add(email,input);
		
		input.gridy = 2;
		input.insets = errorInsets;
		input.anchor = GridBagConstraints.WEST;
		jframe.add(usernameError,input);
		
		input.gridy = 3;
		input.insets = textInsets;
		input.anchor = GridBagConstraints.CENTER;
		jframe.add(password,input);
		
		input.gridy = 4;
		input.insets = errorInsets;
		input.anchor = GridBagConstraints.WEST;
		jframe.add(passwordError,input);
		
		input.insets = buttonInsets;
		input.anchor = GridBagConstraints.WEST;
		input.gridx = 0;
		input.gridy = 5;
		jframe.add(loginButton,input);
		
		jframe.setSize(350,250);
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
