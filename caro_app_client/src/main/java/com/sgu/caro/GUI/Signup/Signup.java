package com.sgu.caro.GUI.Signup;

import com.sgu.caro.GUI.MainScreen.MainScreenDesign;
import com.sgu.caro.GUI.WindowManager;
import com.sgu.caro.GUI.Login.Login;
import com.sgu.caro.GUI.Login.RoundedCornerBorder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sgu.caro.api_connection.DataAPI;
import com.sgu.caro.api_connection.TokenManager;
import com.toedter.calendar.JCalendar;

import ch.qos.logback.core.status.ViewStatusMessagesServletBase;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.miginfocom.swing.MigLayout;

public class Signup extends JFrame{
    private JPanel headPanel;
    private JLabel lblSignup;
    private JPanel mainPanel;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel emailError;
    private JLabel lblFirstname;
    private JTextField txtFirstname;
    private JLabel firstnameError;
    private JLabel lblLastname;
    private JTextField txtLastname;
    private JLabel lastnameError;
    private JLabel lblGender;
    private JComboBox cmbGender;
    private JLabel lblDayOfBirth;
    private JLabel dayOfBirthError;
    private JLabel lblPassword;
    private JPasswordField txtPass;
    private JLabel passError;
    private JLabel lblConfirmPass;
    private JPasswordField txtConfirmPass;
    private JCalendar calendar;
    private JLabel confirmPassError;
    private JButton btnSignUp;
    private JButton btnLogIn;
    private JLabel lblSpace;

    public Signup() throws IOException {
        
        
       
        init();
        this.setTitle("Đăng ký");
    }      
    public void addEventListeners() {
    	 btnLogIn.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 // TODO Auto-generated method stub  
             	try {
					new Login().setVisible(true);
					setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
             }
         });
        //signup button action listener
        btnSignUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub 
            	int errorCount = 0;
            	String username = "";
            	String firstname = "";
            	String lastname = "";
            	String gender = "";
            	String dayOfBirth = "";
            	String password = "";
            	if(!validateMail(txtEmail.getText())) {
        			emailError.setText("Email không hợp lệ");
        			errorCount++;
        		} else {
        			username = txtEmail.getText();
        		}
            	if(txtFirstname.getText().equals("") || allSpace(txtFirstname.getText())) {
        			firstnameError.setText("Họ không hợp lệ");
        			errorCount++;
        		} else {
        			firstname = txtFirstname.getText();
        		}
            	if(allSpace(txtLastname.getText())  || txtLastname.getText().equals("")) {
        			lastnameError.setText("Tên không hợp lệ");
        			errorCount++;
        		} else {
        			lastname = txtLastname.getText();
        		}
            	gender = String.valueOf(cmbGender.getItemAt(cmbGender.getSelectedIndex()));
            	
            	String dateChoose = calendar.getDate().toString();
            	String[] result = dateChoose.split("\s");
            	String day = result[2];
            	String year = result[5];
            	String month = "";
            	switch(result[1]) {
            	case "Jan":
            		month = "01";
            		break;
        		case "Feb":
        			month = "02";
            		break;
    			case "Mar":
    				month = "03";
            		break;
            	case "Apr":
            		month = "04";
            		break;
        		case "May":
        			month = "05";
            		break;
    			case "Jun":
    				month = "06";
            		break;
            	case "Jul":
            		month = "07";
            		break;
        		case "Aug":
        			month = "08";
            		break;
    			case "Sep":
    				month = "09";
            		break;
            	case "Oct":
            		month = "10";
            		break;
        		case "Nov":
        			month = "11";
            		break;
    			case "Dec":
    				month = "12";
            		break;
            	}
                LocalDate currentDate = LocalDate.now();
                LocalDate dayOfBirthChosen = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int diff = currentDate.compareTo(dayOfBirthChosen);
                if(diff > 0) {
                	dayOfBirth = year + "/" + month + "/" + day;
                } else if (diff < 0) {
                    dayOfBirthError.setText("Ngày tháng năm sinh không hợp lệ");
                } else {
                    dayOfBirthError.setText("Ngày tháng năm sinh không hợp lệ");
                }

            	if(!validatePassword(txtPass.getText())) {
        			passError.setText("Mật khẩu không hợp lệ");
        			errorCount++;
        		} else {
        			passError.setText(" ");
        			if(!txtConfirmPass.getText().equals(txtPass.getText())) {
            			confirmPassError.setText("Mật khẩu không trùng khớp!");
            			errorCount++;
            		} else {
            			confirmPassError.setText(" ");
            			password = txtPass.getText();
            		}	
        		}
            	if(errorCount>0) {
        			JOptionPane.showMessageDialog(null, "Không thể đăng ký");
            	} else {
            //call API caro
                    JSONObject jo = new JSONObject();      
                    jo.put("username", username);
                    jo.put("password", password);
                    jo.put("firstName", firstname);
                    jo.put("lastName", lastname);
                    jo.put("gender", gender);
                    jo.put("dayOfBirth", dayOfBirth);
                    
                    String API_URL = "http://localhost:8080/caro_api/users";
                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(API_URL))
                                .headers("Content-Type", "application/json;charset=UTF-8")
                                .POST(HttpRequest.BodyPublishers.ofString(jo.toString()))
                                .build();
                        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        JSONObject responseObj = new JSONObject(response.body().toString());
                        JSONArray keys = responseObj.names();
                        for (int i = 0; i < keys.length (); i++) {
                           String key = keys.getString (i);
                           String value = responseObj.getString (key);
                           if(key.equals("role")) {
                           		new Login().setVisible(true);
                           		setVisible(false);
                        	   break;
                           } 
                           if (key.equals("message")) {
                        	   JOptionPane.showMessageDialog(null, "User đã tồn tại");
                        	   break;
                           }
                        }
                    } catch (Exception ee) {
                    }
            	}
            }
        });
    }
    
    class EventMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent me) {

        }
 
        public void mousePressed(MouseEvent me) {

        }
 
        public void mouseReleased(MouseEvent me) {
        }
 
        public void mouseEntered(MouseEvent me) {
        }
 
        public void mouseExited(MouseEvent me) {
        	if(me.getSource() == txtEmail) {
        		if(!validateMail(txtEmail.getText())) {
        			emailError.setText("Email không hợp lệ");
        		} else {
        			emailError.setText(" ");
        		}
        	}  
        	if(me.getSource() == txtFirstname) {
        		if(txtFirstname.getText().equals("") || allSpace(txtFirstname.getText())) {
        			firstnameError.setText("Họ không hợp lệ");
        		} else {
        			firstnameError.setText(" ");
        		}
        	}
        	if(me.getSource() == txtLastname) {
        		if(allSpace(txtLastname.getText())  || txtLastname.getText().equals("")) {
        			lastnameError.setText("Tên không hợp lệ");
        		} else {
        			lastnameError.setText(" ");
        		}
        	}
        	
        	if(me.getSource() == txtPass) {
        		if(!validatePassword(txtPass.getText())) {
        			passError.setText("Mật khẩu không hợp lệ");
        		} else {
        			passError.setText(" ");
        		}
        	}
        	if(me.getSource() == txtConfirmPass) {
        		if(!txtConfirmPass.getText().equals(txtPass.getText())) {
        			confirmPassError.setText("Mật khẩu không trùng khớp!");
        		} else {
        			confirmPassError.setText(" ");
        		}	
        	}
        }
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
        passError.setForeground(Color.RED);
        if (text.length() < 8) {
        	passError.setText("Độ dài mật khẩu ít nhất là 8");
            return false;
        } else if (!text.matches(".*[a-zA-Z]+.*")) {
        	passError.setText("Mật khẩu phải có ký tự alphabel");
            return false;
        } else {
            return true;
        }
    }

    private boolean allSpace(String text) {
    	String regex = "^\s+";
    	Pattern pattern = Pattern.compile(regex);
    	return pattern.matcher(text).matches();
    }
    public void init() {
        this.getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        headPanel = new JPanel();
        getContentPane().add(headPanel, BorderLayout.NORTH);
        
        lblSignup = new JLabel("Đăng ký tài khoản");
        lblSignup.setFont(new Font("Tahoma", Font.BOLD, 14));
        headPanel.add(lblSignup);
        
        mainPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new MigLayout("", "[][][][][grow]", "[][][][][][][][][][][][][][][][]"));
        
        lblEmail = new JLabel("Email:");
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblEmail, "cell 1 0,alignx left");
        
        txtEmail = new JTextField();
        mainPanel.add(txtEmail, "cell 2 0 3 1,growx");
        txtEmail.setColumns(10);
        txtEmail.addMouseListener(new EventMouseListener());
        
        emailError = new JLabel(" ");
        emailError.setForeground(Color.RED);
        mainPanel.add(emailError, "cell 1 1 4 1");
        
        lblFirstname = new JLabel("Firstname:");
        mainPanel.add(lblFirstname, "cell 1 2,alignx left");
        
        txtFirstname = new JTextField();
        mainPanel.add(txtFirstname, "cell 2 2 3 1,growx");
        txtFirstname.setColumns(10);
        txtFirstname.addMouseListener(new EventMouseListener());
        
        firstnameError = new JLabel(" ");
        firstnameError.setForeground(Color.RED);
        mainPanel.add(firstnameError, "cell 1 3 4 1");
        
        lblLastname = new JLabel("Lastname:");
        mainPanel.add(lblLastname, "cell 1 4,alignx left");
        
        txtLastname = new JTextField();
        mainPanel.add(txtLastname, "cell 2 4 3 1,growx");
        txtLastname.setColumns(10);
        txtLastname.addMouseListener(new EventMouseListener());
        
        lastnameError = new JLabel(" ");
        lastnameError.setForeground(Color.RED);
        mainPanel.add(lastnameError, "cell 1 5 4 1");
        
        lblGender = new JLabel("Gender:");
        mainPanel.add(lblGender, "cell 1 6");
        
        String gender[] = {"male", "female"};
        cmbGender = new JComboBox(gender);
        mainPanel.add(cmbGender, "cell 2 6 3 1,growx");
        
        lblDayOfBirth = new JLabel("Day Of Birth");
        mainPanel.add(lblDayOfBirth, "cell 1 7 4 2,alignx left");
        
        dayOfBirthError = new JLabel(" ");
        dayOfBirthError.setForeground(Color.RED);
        mainPanel.add(dayOfBirthError, "cell 1 11 4 1");
        
        lblPassword = new JLabel("Password:");
        mainPanel.add(lblPassword, "cell 1 12,alignx left");
        
        txtPass = new JPasswordField();
        mainPanel.add(txtPass, "cell 2 12 3 1,growx");
        txtPass.addMouseListener(new EventMouseListener());
        
        passError = new JLabel(" ");
        passError.setForeground(Color.RED);
        mainPanel.add(passError, "flowx,cell 1 13 4 1,alignx left");
        
        calendar = new JCalendar();
        mainPanel.add(calendar, "cell 1 9 4 1,alignx center,aligny center");
        
        lblConfirmPass = new JLabel("Confirm password:");
        mainPanel.add(lblConfirmPass, "cell 1 14");
        
        txtConfirmPass = new JPasswordField();
        mainPanel.add(txtConfirmPass, "cell 2 14 3 1,growx");
        txtConfirmPass.addMouseListener(new EventMouseListener());
        
        confirmPassError = new JLabel(" ");
        confirmPassError.setForeground(Color.RED);
        mainPanel.add(confirmPassError, "cell 1 15 4 1,alignx left");
        
        btnSignUp = new JButton("Sign up");
        mainPanel.add(btnSignUp, "cell 1 16,alignx center");
        btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        btnLogIn = new JButton("Login now");
        mainPanel.add(btnLogIn, "cell 4 16,alignx center");

        this.setSize(350, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.requestFocus();
        addEventListeners();  
    }

    public static void main(String args[]) {
        try {
            new Signup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
