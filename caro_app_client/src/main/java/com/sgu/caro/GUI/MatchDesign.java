package com.sgu.caro.GUI;

import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;


public class MatchDesign extends JFrame{
    public MatchDesign() {
        initComponents();
        
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
        this.setSize(1100, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // Set Layout cho Panel chính
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Set các Panel
        board = new Board();
        userPanel = new UserPanel();
        chatPanel = new ChatPanel();
        
        // add các panel vào panel chính
        
        panel.add(userPanel, BorderLayout.CENTER);
        panel.add(chatPanel, BorderLayout.EAST);
        panel.add(board, BorderLayout.WEST);
        
        
        // Thêm panel chính vào Jframe
        this.add(panel);
        
        // Set vị trí ở giữa
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth()/2 - this.getWidth()/2;
        int y = (int) dimension.getHeight()/2 - this.getHeight()/2;
        this.setLocation(x, y);
        
        this.pack();  
        this.setVisible(true);
        
    }
    
    private JPanel panel;
    private Board board;
    private UserPanel userPanel;
    private ChatPanel chatPanel;
    
    public static void main(String[] args) {
        new MatchDesign();
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
    }
}
