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

    public UpdateAcc() {
        setTitle("Registration Form");
        setBounds(400, 120, 450, 400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

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
        c.add(txtFirstName);

        lblGender = new JLabel("Giới tính");
        lblGender.setFont(new Font("Arial", Font.PLAIN, 15));
        lblGender.setSize(100, 20);
        lblGender.setLocation(50, 200);
        c.add(lblGender);

        rbMale = new JRadioButton("Nam");
        rbMale.setFont(new Font("Arial", Font.PLAIN, 15));
        rbMale.setSelected(true);
        rbMale.setBackground(Color.decode("#a5d2f2"));
        rbMale.setSize(75, 20);
        rbMale.setLocation(200, 200);
        c.add(rbMale);

        rbFemale = new JRadioButton("Nữ");
        rbFemale.setFont(new Font("Arial", Font.PLAIN, 15));
        rbFemale.setSelected(false);
        rbFemale.setBackground(Color.decode("#a5d2f2"));
        rbFemale.setSize(80, 20);
        rbFemale.setLocation(275, 200);
        c.add(rbFemale);

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
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
            dayOfBirth.setDate(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        user = dataAPI.getInfoUserByID(TokenManager.getUser_id(), TokenManager.getJwt());
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sub) {
            if ((txtFirstName.getText().length() != 0) && (txtLastName.getText().length() != 0)) {

                String gender;
                String data = "";
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                if (rbMale.isSelected()) {
                    gender = "male";
                } else {
                    gender = "female";
                }
                SimpleDateFormat formatDay = new SimpleDateFormat("dd-MM-yyyy");
                String strDayOfBirth = formatDay.format(dayOfBirth.getDate());
                //JSONObject user = dataAPI.getInfoUserByID(TokenManager.getUser_id(), TokenManager.getJwt());
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
                txtNotification.setText("Vui lòng điền đầy đủ thông tin!");
            }

        }

        if (e.getSource() == reset) {
            String def = "";
            txtFirstName.setText(def);
            txtLastName.setText(def);
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
