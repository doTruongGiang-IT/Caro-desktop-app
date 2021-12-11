package com.sgu.caro.GUI.MainScreen;

import com.sgu.caro.GUI.MatchScreen.Board;
import com.sgu.caro.GUI.MatchScreen.Cell;
import com.sgu.caro.GUI.MatchScreen.MatchDesign;
import com.sgu.caro.GUI.MatchScreen.ResultMatchScreen;
import com.sgu.caro.GUI.WindowManager;
import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainScreenDesign extends JFrame{
    public static boolean loadMatch = true;
    private static SocketConnection socket = new SocketConnection();
    private static DataSocket dataSocket = new DataSocket();
    private static int userId = new TokenManager().getUser_id();
    
    public MainScreenDesign() {
        // Init socket
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
        
        initComponents();
        
        socket.addListenConnection("get_group", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                leftPanel.removeAll();
                leftPanel.validate();
                JSONArray groups = data.getJSONArray("groups");
                for (int i=0; i<groups.length(); i++){
                    JSONObject element = groups.getJSONObject(i);
                    System.out.println(i + 1 + 
                        String.valueOf(element.getInt("user_1")) +
                        String.valueOf(element.getInt("user_2")) +
                        element.getInt("number_of_watchers"));
                    BanChoi board = new BanChoi(
                        i + 1, 
                        String.valueOf(element.getInt("user_1")), 
                        String.valueOf(element.getInt("user_2")),
                        element.getInt("number_of_watchers")
                    );
                    leftPanel.add(board);
                    leftPanel.validate();
                }
                    
            }
        });
        
        socket.addListenConnection("get_user", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                rightPanel.removeAll();
                JSONArray users = data.getJSONArray("users");
                for (int i=0; i<users.length(); i++){
                    JSONObject element = users.getJSONObject(i);
                    KyThu player = new KyThu(
                        element.getInt("id"), 
                        element.getString("name"),
                        element.getInt("score")
                    );
                    rightPanel.add(player);
                }
                    
            }
        });
        
    }
    private void initComponents() {
        
        this.setTitle("Game Caro");
        try {
            setIconImage((new ImageIcon()).getImage());
            setIconImage((new ImageIcon(ImageIO.read(new File("images/xo.png")))).getImage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        this.setSize(900, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // Set Layout cho Panel chính
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        //================ Phần danh sách bàn
        mainLeftPanel = new JPanel();
        mainLeftPanel.setBackground(Color.white);
        mainLeftPanel.setPreferredSize(new Dimension((int)(getWidth()*60/100), getHeight()));
        mainLeftPanel.setLayout(new BoxLayout(mainLeftPanel, BoxLayout.Y_AXIS));
        
        lblBanCo = new JLabel("Bàn cờ");
        
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftScroll = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        btnNewGame = new JButton("Vào chơi");
        btnNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(loadMatch) {
                    String data = dataSocket.exportDataGoMatch(userId);
                    socket.sendData(data);
                    new LoadMatch().setVisible(true);
                    loadMatch = false;
                }
            }
        });
        
        btnRank = new JButton("Xếp Hạng");
        btnRank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new UserRankScreen().setVisible(true);
            }
        });
        mainLeftPanel.add(btnRank);
        mainLeftPanel.add(lblBanCo);
        mainLeftPanel.add(leftScroll);
        mainLeftPanel.add(btnNewGame);
        mainLeftPanel.add(Box.createRigidArea(new Dimension(130, 0)));
        
    
        //=============== Phần danh sách người chơi
        mainRightPanel = new JPanel();
        mainRightPanel.setBackground(Color.white);
        mainRightPanel.setPreferredSize(new Dimension((int)(getWidth()*40/100), getHeight()));
        mainRightPanel.setLayout(new BoxLayout(mainRightPanel, BoxLayout.Y_AXIS));
        
        lblKyThu = new JLabel("Kỳ thủ");
        
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.white);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightScroll = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainRightPanel.add(lblKyThu);
        mainRightPanel.add(rightScroll);
        mainRightPanel.add(Box.createRigidArea(new Dimension(90, 0)));
        
        // Thêm mainLeftPanel và mainRightPanel vào mainPanel
        mainPanel.add(mainLeftPanel);
        mainPanel.add(mainRightPanel);
        
        // Thêm panel chính vào Jframe
        this.add(mainPanel);
        
        // Set vị trí ở giữa
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth()/2 - this.getWidth()/2;
        int y = (int) dimension.getHeight()/2 - this.getHeight()/2;
        this.setLocation(x, y);
        
        this.pack();  
        
    }
    
    public static void main(String[] args) {
        new MainScreenDesign().setVisible(true);
    }
    
    // mainPanel là panel chính
    private JPanel mainPanel;
    // mainLeftPanel là panel chính chứa các component phần Danh sách bàn chơi
    private JPanel mainLeftPanel;
    // leftPanel, leftScroll để liệt kê danh sách bàn chơi
    private JPanel leftPanel;
    private JScrollPane leftScroll;
    private JButton btnNewGame, btnRank;
    private JLabel lblBanCo;
    
    // mainRightPanel là panel chính chứa các component phần Danh sách kỳ thủ
    private JPanel mainRightPanel;
    // rightPanel, rightScroll để liệt kê danh sách kỳ thủ
    private JPanel rightPanel;
    private JScrollPane rightScroll;
    private JLabel lblKyThu;
}
