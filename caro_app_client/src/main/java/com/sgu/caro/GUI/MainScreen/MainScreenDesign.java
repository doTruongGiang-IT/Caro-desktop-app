package com.sgu.caro.GUI.MainScreen;

import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
        
        // Cái này test chơi thôi - sau này load bàn chơi và kỳ thủ
        for(int i = 0; i <= 20; ++i) {
            BanChoi cc = new BanChoi();
            leftPanel.add(cc);
        }
        
        for(int i = 0; i <= 20; ++i) {
            KyThu cc = new KyThu();
            rightPanel.add(cc);
        }
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
    private JButton btnNewGame;
    private JLabel lblBanCo;
    
    // mainRightPanel là panel chính chứa các component phần Danh sách kỳ thủ
    private JPanel mainRightPanel;
    // rightPanel, rightScroll để liệt kê danh sách kỳ thủ
    private JPanel rightPanel;
    private JScrollPane rightScroll;
    private JLabel lblKyThu;
}
