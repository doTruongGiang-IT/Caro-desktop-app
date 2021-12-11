package com.sgu.caro.GUI.MainScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sgu.caro.GUI.Login.RoundedCornerBorder;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;

public class UserRankScreen extends JFrame {

	private SocketConnection socket;
	private DataSocket dataSocket;

	public UserRankScreen() {
		initComponents();
		
//		socket.addListenConnection("get_rank_score", new SocketHandler() {
//			@Override
//			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
//				rankPanel.removeAll();
//				JSONArray users = data.getJSONArray("users");
//				for (int i = 0; i < users.length(); i++) {
//					JSONObject element = users.getJSONObject(i);
//					UserRank player = new UserRank(element.getInt("id"), element.getString("name"),
//							element.getInt("win_rate"), element.getInt("win_length"), element.getInt("score"));
//					rankPanel.add(player);
//				}
//			}
//		});
//
//		socket.addListenConnection("get_rank_win_rate", new SocketHandler() {
//			@Override
//			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
//				rankPanel.removeAll();
//				JSONArray users = data.getJSONArray("users");
//				for (int i = 0; i < users.length(); i++) {
//					JSONObject element = users.getJSONObject(i);
//					UserRank player = new UserRank(element.getInt("id"), element.getString("name"),
//							element.getInt("win_rate"), element.getInt("win_length"), element.getInt("score"));
//					rankPanel.add(player);
//				}
//			}
//		});
//
//		socket.addListenConnection("get_rank_win_length", new SocketHandler() {
//			@Override
//			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
//				rankPanel.removeAll();
//				JSONArray users = data.getJSONArray("users");
//				for (int i = 0; i < users.length(); i++) {
//					JSONObject element = users.getJSONObject(i);
//					UserRank player = new UserRank(element.getInt("id"), element.getString("name"),
//							element.getInt("win_rate"), element.getInt("win_length"), element.getInt("score"));
//					rankPanel.add(player);
//				}
//			}
//		});

	}

	private void initComponents() {

		this.setTitle("Xếp Hạng");
		try {
			setIconImage((new ImageIcon()).getImage());
			setIconImage((new ImageIcon(ImageIO.read(new File("images/xo.png")))).getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setSize(700, 640);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		userPanel = new JPanel();
		userPanel.setBackground(Color.lightGray);

		userPanel.setPreferredSize(new Dimension(500, 750));

		btnScore = new JButton("Điểm thành tích");
		btnScore.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				String data = dataSocket.exportDataRankScore();
//				socket.sendData(data);
				test_mock_data();
			}
		});

		btnWinRate = new JButton("Tỉ lệ thắng");
		btnWinRate.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				String data = dataSocket.exportDataRankWinRate();
//				socket.sendData(data);
				test_mock_data();
			}
		});

		btnWinLength = new JButton("Chuỗi trận thắng");
		btnWinLength.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				String data = dataSocket.exportDataRankWinLength();
//				socket.sendData(data);
				test_mock_data();
			}
		});

		
		label = new JLabel("           Id             Name                            Win Rate            Win Length                   Score");
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.RED);
		titlePanel.setPreferredSize(new Dimension(600, 20));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.add(label);
		//label.setForeground(Color.RED);
		
		btnScore.setBackground(Color.decode("#00649F"));
		btnScore.setForeground(Color.WHITE);
		btnScore.setMargin(new Insets(0, 10, 0, 10));
		btnWinLength.setBackground(Color.decode("#00649F"));
		btnWinLength.setForeground(Color.WHITE);
		btnWinLength.setMargin(new Insets(0, 10, 0, 10));
		btnWinRate.setBackground(Color.decode("#00649F"));
		btnWinRate.setForeground(Color.WHITE);
		btnWinLength.setMargin(new Insets(0, 10, 0, 10));
        
		rankPanel = new JPanel();
		rankPanel.setBackground(Color.white);
		rankPanel.setPreferredSize(new Dimension(700, 600));
		rankPanel.add(titlePanel);
		btnPanel = new JPanel();
		btnPanel.setBackground(Color.white);
		btnPanel.setPreferredSize(new Dimension(700, 40));

		btnPanel.add(btnScore);
		btnPanel.add(btnWinRate);
		btnPanel.add(btnWinLength);
		scroll = new JScrollPane(userPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setPreferredSize(new Dimension(600, 500));
		rankPanel.add(scroll);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setPreferredSize(new Dimension(700, 560));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(btnPanel);
		mainPanel.add(rankPanel);
		this.add(mainPanel);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) dimension.getWidth() / 2 - this.getWidth() / 2;
		int y = (int) dimension.getHeight() / 2 - this.getHeight() / 2;
		this.setLocation(x, y);

	}

	public void test_mock_data() {
		String urlAPI = "https://61b361dcaf5ff70017ca1f03.mockapi.io/users";

		String charset = "UTF-8";
		BufferedReader reader;
		String line;
		StringBuilder response = new StringBuilder();
		URLConnection connection;
		try {
			userPanel.removeAll();
			userPanel.validate();
			connection = new URL(urlAPI).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			String result = response.toString();
			JSONArray jsonArray = new JSONArray(result);

			StringBuilder string = new StringBuilder();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject res = jsonArray.getJSONObject(i);
				UserRank user = new UserRank(res.getInt("id"), res.getString("name"), res.getInt("win_rate"),
						res.getInt("win_length"), res.getInt("score"));
				userPanel.add(user);
				userPanel.validate();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new UserRankScreen().setVisible(true);
	}
	private JLabel label;
	private JPanel btnPanel, userPanel, mainPanel, rankPanel, titlePanel;
	private JButton btnScore, btnWinRate, btnWinLength;
	private JScrollPane scroll;

}
