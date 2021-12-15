package com.sgu.caro.GUI.MatchScreen;

import com.sgu.caro.GUI.MainScreen.MainScreenDesign;
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

public class MatchDesign extends JFrame {

    public static int user2;
    public Cell[][] matrix;
    public boolean is_watcher = false;
    
    public MatchDesign(String stepType, int user_1, String username_1, String score_1, int user_2, String username_2, String score_2) {
        initComponents(stepType, user_1, username_1, score_1, user_2, username_2, score_2);
    }
    
    public MatchDesign(String stepType, int user_1, String username_1, String score_1, int user_2, String username_2, String score_2, Cell[][] matrix) {
        this.matrix = matrix;
        this.is_watcher = true;
        initComponents(stepType, user_1, username_1, score_1, user_2, username_2, score_2);
    }

    private void initComponents(String stepType, int user_1, String username_1, String score_1, int user_2, String username_2, String score_2) {

        this.setTitle("Game Caro");
        try {
            setIconImage((new ImageIcon()).getImage());
            setIconImage((new ImageIcon(ImageIO.read(new File("images/xo.png")))).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSize(1100, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Set Layout cho Panel chính
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Set các Panel
        board = new Board(stepType);
        if (is_watcher){
            board.setMatrix(matrix);
        }
        userPanel = new UserPanel(stepType, user_1, username_1, score_1, user_2, username_2, score_2);
        chatPanel = new ChatPanel();

        // add các panel vào panel chính
        panel.add(userPanel, BorderLayout.CENTER);
        panel.add(chatPanel, BorderLayout.EAST);
        panel.add(board, BorderLayout.WEST);

        // Thêm panel chính vào Jframe
        this.add(panel);

        // Set vị trí ở giữa
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth() / 2 - this.getWidth() / 2;
        int y = (int) dimension.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);

        this.pack();
        this.setVisible(true);
    }

    private JPanel panel;
    private Board board;
    private UserPanel userPanel;
    private ChatPanel chatPanel;

    public static void main(String[] args) {
//        new MatchDesign("O");
//        SocketConnection socket = new SocketConnection();
//        socket.startConnection();
    }
}
