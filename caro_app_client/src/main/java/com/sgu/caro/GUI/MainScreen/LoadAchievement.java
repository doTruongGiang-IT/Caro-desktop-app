package com.sgu.caro.GUI.MainScreen;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sgu.caro.GUI.WindowManager;
import com.sgu.caro.api_connection.TokenManager;

import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;

public class LoadAchievement extends JFrame {
    private static long userId = new TokenManager().getUser_id();
    private static String token = new TokenManager().getJwt();

	public LoadAchievement() throws IOException {
		initComponents();
		
		String API_URL = "http://localhost:8080/caro_api/stats/11";
		
		 try { 
				System.out.println(API_URL);
				System.out.println("user - " + userId);
				System.out.println("token - "+ token);
				
			 HttpClient client = HttpClient.newHttpClient();
			 HttpRequest request = HttpRequest.newBuilder() .uri(new URI(API_URL)) 
					 .headers("Content-Type", "application/json;charset=UTF-8") 
					 .headers("Authorization", token) 
					 .GET()
					 .build();
			 HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			 BufferedReader reader = new BufferedReader(new InputStreamReader(((URLConnection) response).getInputStream()));
			 String line = "";
			 StringBuilder res = new StringBuilder();
			 while ((line = reader.readLine()) != null) {
				 res.append(line);
			 }
			 reader.close();
			 String result = res.toString(); 
			 try {
				 JSONObject jo = new JSONObject(result);
				 txtScore.setText(String.valueOf(jo.getInt("score")));
				 txtWinRate.setText(String.valueOf(jo.getInt("win_rate")));
				 txtWinCount.setText(String.valueOf(jo.getInt("win_count")));
				 txtWinLength.setText(String.valueOf(jo.getInt("win_length")));
				 txtLoseCount.setText(String.valueOf(jo.getInt("lose_count")));
				 txtLoseLength.setText(String.valueOf(jo.getInt("lose_length"))); 
			 } catch (Exception ex) {
				 ex.printStackTrace(); 
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 
	//
        this.setTitle("Thành tích");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth() / 2 - this.getWidth() / 2;
        int y = (int) dimension.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);
	}
    private void initComponents() {
    	txtScore = new JLabel();
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(500, 500));
		setBounds(new Rectangle(100, 30, 500, 500));
		getContentPane().setSize(new Dimension(500, 500));
		getContentPane().setBounds(new Rectangle(100, 50, 500, 500));
		
		headPanel = new JPanel();
		headPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		bodyPanel = new JPanel();
		bodyPanel.setLayout(new GridLayout(0, 2, 0, 0));

		footPanel = new JPanel();
		
		lblThanhTich = new JLabel("Thành tích");
		lblThanhTich.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		lblScore = new JLabel("Score");
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		txtScore = new JLabel("???");
		
		lblWinRate = new JLabel("Tỷ lệ thắng");
		lblWinRate.setHorizontalAlignment(SwingConstants.CENTER);		
		txtWinRate = new JLabel("???");
		
		lblWinCount = new JLabel("Số trận thắng");
		lblWinCount.setHorizontalAlignment(SwingConstants.CENTER);		
		txtWinCount = new JLabel("???");
		
		lblWinLength = new JLabel("Chuỗi trận thắng");
		lblWinLength.setHorizontalAlignment(SwingConstants.CENTER);		
		txtWinLength = new JLabel("???");
		
		lblLoseCount = new JLabel("Số trận thua");
		lblLoseCount.setHorizontalAlignment(SwingConstants.CENTER);		
		txtLoseCount = new JLabel("???");
		
		lblLoseLength = new JLabel("Chuỗi trận thua");
		lblLoseLength.setHorizontalAlignment(SwingConstants.CENTER);		
		txtLoseLength = new JLabel("???");
		
		btnExit = new JButton("Thoát");
		btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });
		
		headPanel.add(lblThanhTich);
		bodyPanel.add(lblScore);
		bodyPanel.add(txtScore);
		bodyPanel.add(lblWinRate);
		bodyPanel.add(txtWinRate);
		bodyPanel.add(lblWinCount);
		bodyPanel.add(txtWinCount);
		bodyPanel.add(lblWinLength);
		bodyPanel.add(txtWinLength);
		bodyPanel.add(lblLoseCount);
		bodyPanel.add(txtLoseCount);
		bodyPanel.add(lblLoseLength);
		bodyPanel.add(txtLoseLength);
		footPanel.add(btnExit);
		
		getContentPane().add(headPanel, BorderLayout.NORTH);
		getContentPane().add(bodyPanel, BorderLayout.CENTER);
		getContentPane().add(footPanel, BorderLayout.SOUTH);
		
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    }
    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {
    	this.setVisible(false);
    }
	public static void main(String[] args) throws IOException {
        new LoadAchievement().setVisible(true);
	}
	private JPanel headPanel, bodyPanel, footPanel;
	private JLabel lblThanhTich, lblScore, lblWinRate, lblWinCount, lblWinLength, lblLoseCount, lblLoseLength;
	private JLabel txtScore, txtWinRate, txtWinCount, txtWinLength, txtLoseCount, txtLoseLength;
	private JButton btnExit;
}
