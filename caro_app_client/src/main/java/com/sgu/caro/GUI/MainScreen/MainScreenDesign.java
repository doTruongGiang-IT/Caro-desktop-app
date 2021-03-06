package com.sgu.caro.GUI.MainScreen;

import com.sgu.caro.GUI.MatchScreen.Board;
import com.sgu.caro.GUI.MatchScreen.Cell;
import com.sgu.caro.GUI.MatchScreen.MatchDesign;
import com.sgu.caro.GUI.MatchScreen.OutMatchScreen;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.Rectangle;

public class MainScreenDesign extends JFrame {

    public static boolean loadMatch = true;
//    public static boolean loadAchievement = true;
    private static SocketConnection socket = new SocketConnection();
    private static DataSocket dataSocket = new DataSocket();
    private static int userId = new TokenManager().getUser_id();

    public MainScreenDesign() {
        // Init socket
        SocketConnection socket = new SocketConnection();

        initComponents();

        socket.addListenConnection("get_group", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                leftPanel.removeAll();
                leftPanel.validate();
                JSONArray groups = data.getJSONArray("groups");
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject element = groups.getJSONObject(i);
                    System.out.println(i + 1
                            + String.valueOf(element.getInt("user_1"))
                            + String.valueOf(element.getInt("user_2"))
                            + element.getInt("number_of_watchers"));
                    BanChoi board = new BanChoi(
                            i + 1,
                            String.valueOf(element.getInt("user_1")),
                            element.getString("username_1"),
                            String.valueOf(element.getInt("user_2")),
                            element.getString("username_2"),
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
                for (int i = 0; i < users.length(); i++) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSize(900, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Set Layout cho Panel ch??nh
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        btnUpdateUser = new JButton("C???p nh???t");
        btnUpdateUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new UpdateAcc().setVisible(true);
            }
        });
        

        //================ Ph???n danh s??ch b??n
        mainLeftPanel = new JPanel();
        mainLeftPanel.setBackground(Color.white);
        mainLeftPanel.setPreferredSize(new Dimension((int) (getWidth() * 60 / 100), getHeight()));
        mainLeftPanel.setLayout(new BoxLayout(mainLeftPanel, BoxLayout.Y_AXIS));

        lblBanCo = new JLabel("B??n c???");

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftScroll = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        btnNewGame = new JButton("V??o ch??i");
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (loadMatch) {
                    String data = dataSocket.exportDataGoMatch(userId);
                    socket.sendData(data);
                    new LoadMatch().setVisible(true);
                    loadMatch = false;
                }
            }
        });
        
        btnRank = new JButton("X???p H???ng");
        btnRank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new UserRankScreen().setVisible(true);
            }
        });
        lblSpace = new JLabel("................................");
        lblSpace.setForeground(SystemColor.menu);

        btnThanhTich = new JButton("Xem th??nh t??ch");
        btnThanhTich.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThanhTich.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                if(loadAchievement) {
//                    String data = dataSocket.exportDataGoMatch(userId);
//                    socket.sendData(data);
//                    new LoadAchievement().setVisible(true);
//                    loadAchievement = false;
//                }
                try {
                    LoadAchievement loadAchievement = new LoadAchievement();
                    loadAchievement.setVisible(true);
                    loadAchievement.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        SocketConnection socket = new SocketConnection();

        socket.addListenConnection("accept_watch", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                boolean accept = data.getBoolean("accept");
                if (accept) {
                    int user_1 = data.getInt("user_1");
                    String username_1 = data.getString("username_1");
                    int score_1 = data.getInt("score_1");
                    int user_2 = data.getInt("user_2");
                    String username_2 = data.getString("username_2");
                    int score_2 = data.getInt("score_2");
                    int who_x = data.getInt("who_x");

                    Cell[][] matrix = new Cell[20][20];
                    for (int i = 0; i < 20; i++) {
                        JSONArray e = data.getJSONArray("matrix").getJSONArray(i);
                        for (int j = 0; j < 20; j++) {
                            int status = e.getInt(j);

                            Cell cell = new Cell();
                            if (status != 0) {
                                if (status == user_1 && who_x == 1) {
                                    cell.setValue(Cell.X_VALUE);
                                    matrix[i][j] = cell;
                                } else {
                                    cell.setValue(Cell.O_VALUE);
                                    matrix[i][j] = cell;
                                }
                            } else {
                                cell.setValue(Cell.EMPTY_VALUE);
                                matrix[i][j] = cell;
                            }
                        }
                    }
                    if (who_x == 1) {
                        WindowManager.matchScreen = new MatchDesign(Integer.toString(user_1), user_1, username_1, Integer.toString(score_1), user_2, username_2, Integer.toString(score_2), matrix);
                    } else {
                        WindowManager.matchScreen = new MatchDesign(Integer.toString(user_2), user_2, username_2, Integer.toString(score_2), user_1, username_1, Integer.toString(score_1), matrix);
                    }
                    WindowManager.matchScreen.setVisible(true);
                    WindowManager.matchScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    WindowManager.matchScreen.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                                new OutMatchScreen(true, user_1, 1, "X??c nh???n", "B???n c?? mu???n tho??t tr???n ?????u? (Ng?????i xem) ", "?????ng ??", "Quay L???i").setVisible(true);
                        }
                    });
                    WindowManager.mainScreen.setVisible(false);
                    MainScreenDesign.loadMatch = true;
//                    this.setVisible(false);
//                    this.dispose();
                }
            }
        });
        
        bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new BoxLayout(bottomLeftPanel, BoxLayout.X_AXIS));
        
        bottomLeftPanel.add(btnThanhTich);
        bottomLeftPanel.add(btnNewGame);
        bottomLeftPanel.add(btnRank);

        mainLeftPanel.add(lblBanCo);
        mainLeftPanel.add(leftScroll);
        mainLeftPanel.add(bottomLeftPanel);
        mainLeftPanel.add(Box.createRigidArea(new Dimension(130, 0)));

        //=============== Ph???n danh s??ch ng?????i ch??i
        mainRightPanel = new JPanel();
        mainRightPanel.setBackground(Color.white);
        mainRightPanel.setPreferredSize(new Dimension((int) (getWidth() * 40 / 100), getHeight()));
        mainRightPanel.setLayout(new BoxLayout(mainRightPanel, BoxLayout.Y_AXIS));

        lblKyThu = new JLabel("K??? th???");

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.white);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightScroll = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainRightPanel.add(lblKyThu);
        mainRightPanel.add(rightScroll);
        mainRightPanel.add(btnUpdateUser);
        mainRightPanel.add(Box.createRigidArea(new Dimension(90, 0)));

        // Th??m mainLeftPanel v?? mainRightPanel v??o mainPanel
        mainPanel.add(mainLeftPanel);
        mainPanel.add(mainRightPanel);

        // Th??m panel ch??nh v??o Jframe
        getContentPane().add(mainPanel, BorderLayout.SOUTH);
        this.add(mainPanel);

        // Set v??? tr?? ??? gi???a
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth() / 2 - this.getWidth() / 2;
        int y = (int) dimension.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);

        this.pack();

    }

    public static void main(String[] args) {
        new MainScreenDesign().setVisible(true);
    }

    // mainPanel l?? panel ch??nh
    private JPanel mainPanel;
    
    // ch??a c??c button xem th??nh t??ch, newgame, xem rank
    private JPanel bottomLeftPanel;
    // mainLeftPanel l?? panel ch??nh ch???a c??c component ph???n Danh s??ch b??n ch??i
    private JPanel mainLeftPanel;
    // leftPanel, leftScroll ????? li???t k?? danh s??ch b??n ch??i
    private JPanel leftPanel;
    private JScrollPane leftScroll;
    private JButton btnNewGame, btnUpdateUser, btnRank;
    private JLabel lblBanCo;
    // mainRightPanel l?? panel ch??nh ch???a c??c component ph???n Danh s??ch k??? th???
    private JPanel mainRightPanel;
    // rightPanel, rightScroll ????? li???t k?? danh s??ch k??? th???
    private JPanel rightPanel;
    private JScrollPane rightScroll;
    private JLabel lblKyThu;
    private JButton btnThanhTich;
    private JLabel lblSpace;
}
