package com.sgu.caro.GUI.MainScreen;

import javax.swing.*;

import org.json.JSONObject;

import com.sgu.caro.api_connection.DataAPI;
import com.sgu.caro.api_connection.TokenManager;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class UpdateAcc extends JFrame implements ActionListener {

    /*
	 * users day_of_birth, first_name, gender, last_name
	 */

	private Container c;
	private JLabel title;
	private JLabel lblFirstName;
	private JTextField txtFirstName;
	private JLabel lblLastName;
	private JTextField txtLastName;

	private JLabel lblGender;
	private JRadioButton rbMale;
	private JRadioButton rbFemale;

	private ButtonGroup genderGroup;
	private JLabel dob;
	private JDateChooser dayOfBirth;
	private JPanel panel;

	// private JPasswordField txtPassword, txtConfirmPassword;

	private JButton sub;
	private JButton reset;
	private JLabel txtNotification;
	DataAPI dataAPI = new DataAPI();
	final JSONObject user; 
	final String tempDay;
	public UpdateAcc() {
		setTitle("Registration Form");
		setBounds(400, 120, 450, 400);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		
		c = getContentPane();
		c.setLayout(null);
		user = dataAPI.getInfoUserByID(TokenManager.getUser_id(), TokenManager.getJwt());
		
		title = new JLabel("Cập nhật tài khoản");
		title.setFont(new Font("Arial", Font.PLAIN, 28));
		title.setSize(250, 30);
		title.setLocation(100, 30);
		c.add(title);

		lblLastName = new JLabel("Họ");
		lblLastName.setFont(new Font("Arial", Font.PLAIN, 15));
		lblLastName.setSize(100, 20);
		lblLastName.setLocation(50, 100);
		c.add(lblLastName);

		txtLastName = new JTextField();
		txtLastName.setFont(new Font("Arial", Font.PLAIN, 15));
		txtLastName.setSize(190, 20);
		txtLastName.setLocation(200, 100);
		txtLastName.setText(user.getString("lastName"));
		c.add(txtLastName);

		lblFirstName = new JLabel("Tên");
		lblFirstName.setFont(new Font("Arial", Font.PLAIN, 15));
		lblFirstName.setSize(100, 20);
		lblFirstName.setLocation(50, 150);
		c.add(lblFirstName);

		txtFirstName = new JTextField();
		txtFirstName.setFont(new Font("Arial", Font.PLAIN, 15));
		txtFirstName.setSize(190, 20);
		txtFirstName.setLocation(200, 150);
		txtFirstName.setText(user.getString("firstName"));
		c.add(txtFirstName);

		lblGender = new JLabel("Giới tính");
		lblGender.setFont(new Font("Arial", Font.PLAIN, 15));
		lblGender.setSize(100, 20);
		lblGender.setLocation(50, 200);
		c.add(lblGender);

		rbMale = new JRadioButton("Nam");
		rbMale.setFont(new Font("Arial", Font.PLAIN, 15));
		rbMale.setBackground(Color.decode("#a5d2f2"));
		rbMale.setSize(75, 20);
		rbMale.setLocation(200, 200);
		c.add(rbMale);

		rbFemale = new JRadioButton("Nữ");
		rbFemale.setFont(new Font("Arial", Font.PLAIN, 15));
		//rbFemale.setSelected(false);
		rbFemale.setBackground(Color.decode("#a5d2f2"));
		rbFemale.setSize(80, 20);
		rbFemale.setLocation(275, 200);
		c.add(rbFemale);
		
		if(user.getString("gender").equals("male")) {
			rbMale.setSelected(true);
			rbFemale.setSelected(false);
		} else {
			rbMale.setSelected(false);
			rbFemale.setSelected(true);
		}

		genderGroup = new ButtonGroup();
		genderGroup.add(rbMale);
		genderGroup.add(rbFemale);

		dob = new JLabel("Ngày sinh");
		dob.setFont(new Font("Arial", Font.PLAIN, 15));
		dob.setSize(100, 20);
		dob.setLocation(50, 250);
		c.add(dob);

		dayOfBirth = new JDateChooser();
		dayOfBirth.setSize(190, 20);
		dayOfBirth.setLocation(200, 250);
		Date date;
        tempDay = user.getString("dayOfBirth");
		try {
			date = new SimpleDateFormat("yyyy/MM/dd").parse(tempDay);
			dayOfBirth.setDate(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		c.add(dayOfBirth);

		sub = new JButton("Đổi");
		sub.setFont(new Font("Arial", Font.PLAIN, 15));
		sub.setSize(100, 20);
		sub.setLocation(100, 300);
		sub.addActionListener(this);
		c.add(sub);

		reset = new JButton("Reset");
		reset.setFont(new Font("Arial", Font.PLAIN, 15));
		reset.setSize(100, 20);
		reset.setLocation(250, 300);
		reset.addActionListener(this);
		c.add(reset);

		txtNotification = new JLabel("");
		txtNotification.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNotification.setForeground(Color.RED);
		txtNotification.setSize(500, 25);
		txtNotification.setLocation(100, 320);
		c.add(txtNotification);

		panel = new JPanel();
		panel.setBounds(0, 0, 450, 400);
		panel.setBackground(Color.decode("#a5d2f2"));
		c.add(panel);
		
		setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == sub) {
			if ((txtFirstName.getText().length() != 0) && (txtLastName.getText().length() != 0)) {

				String gender;
				String data = "";
				String firstName = txtFirstName.getText();
				String lastName = txtLastName.getText();
				String strDayOfBirth;
				if (rbMale.isSelected())
					gender = "male";
				else
					gender = "female";
				SimpleDateFormat formatDay = new SimpleDateFormat("yyyy/MM/dd");
				Date dateUserInput  = dayOfBirth.getDate();
				Date checkLeftDay;
				Date checkRightDay;
				try {
					checkLeftDay = new SimpleDateFormat("yyyy/MM/dd").parse("1900/01/01");
					checkRightDay = new SimpleDateFormat("yyyy/MM/dd").parse("2021/01/01");
					
					if((dateUserInput != null) && dateUserInput.after(checkLeftDay) && dateUserInput.before(checkRightDay)) {
						strDayOfBirth = formatDay.format(dayOfBirth.getDate());
						JSONObject jsonUpdateUser = new JSONObject();
						/*
						 * { "id": 2, "username": "duongdong@gmail.com", "password":
						 * "55e68311694e6bd07e53c68429a4f600aea3bdd880d25d559f166cb30d17a9de34c88c6f8f1bc5f4",
						 * "firstName": "Do Truong", "lastName": "Giang dep trai", "gender": "male",
						 * "dayOfBirth": "2000/07/12", "score": 1, "role": "user", "name": "Do Giang",
						 * "active": true, "win_length": 1, "win_rate": 9.0 }
		                 */
		                String userNameString = user.getString("username");
		                String passwordString = user.getString("password");
		                int scoreString = user.getInt("score");
		                String roleString = user.getString("role");
		                String nameString = user.getString("name");
		                boolean active = true;
		                int win_length = user.getInt("win_length");
		                int win_rate = user.getInt("win_rate");

		                jsonUpdateUser.put("id", TokenManager.getUser_id());
		                jsonUpdateUser.put("username", userNameString);
		                jsonUpdateUser.put("password", passwordString);
		                jsonUpdateUser.put("firstName", firstName);
		                jsonUpdateUser.put("lastName", lastName);
		                jsonUpdateUser.put("gender", gender);
		                jsonUpdateUser.put("dayOfBirth", strDayOfBirth);
		                jsonUpdateUser.put("score", scoreString);
		                jsonUpdateUser.put("role", roleString);
		                jsonUpdateUser.put("active", active);
		                jsonUpdateUser.put("win_length", win_length);
		                jsonUpdateUser.put("win_rate", win_rate);
		                jsonUpdateUser.put("name", nameString);

		                boolean isUpdated = updateUserCallAPI(jsonUpdateUser);
		                if (isUpdated == true) {

		                    txtNotification.setText("Thay đổi thành công");
		                } else {
		                    txtNotification.setText("Thay đổi không thành công");
		                }
	    			} else {
	    				txtNotification.setText("Thay đổi không thành công");
	    			}
				} catch (ParseException e1) {
					System.out.println("Get day khong duoc");
				}
				
				//JSONObject user = dataAPI.getInfoUserByID(TokenManager.getUser_id(), TokenManager.getJwt());
				
            } else {
                txtNotification.setText("Vui lòng điền đầy đủ thông tin!");
            }

        }

        if (e.getSource() == reset) {
            String def = "";
            txtFirstName.setText(user.getString("firstName"));
            txtLastName.setText(user.getString("lastName"));
            if(user.getString("gender").equals("male")) {
    			rbMale.setSelected(true);
    			rbFemale.setSelected(false);
    		} else {
    			rbMale.setSelected(false);
    			rbFemale.setSelected(true);
    		}
            Date date;
            String day = user.getString("dayOfBirth");
    		try {
    			date = new SimpleDateFormat("yyyy/MM/dd").parse(tempDay);
    			dayOfBirth.setDate(date);
    		} catch (ParseException e1) {
    			e1.printStackTrace();
    		}
            txtNotification.setText(def);
        }
    }

    public boolean updateUserCallAPI(JSONObject user) {
        boolean isUpdate = false;
        String jsonUserToString = user.toString();
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = TokenManager.getHOST() + "/caro_api/users/" + TokenManager.getUser_id();
        try {
            HttpPut request = new HttpPut(url);
            StringEntity params = new StringEntity(jsonUserToString);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization",
                    TokenManager.getJwt());
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);
            isUpdate = true;
        } catch (Exception ex) {
        } finally {
            // @Deprecated httpClient.getConnectionManager().shutdown();
        }

        return isUpdate;
    }

    public static void main(String[] args) throws Exception {
        new UpdateAcc();
    }

}
