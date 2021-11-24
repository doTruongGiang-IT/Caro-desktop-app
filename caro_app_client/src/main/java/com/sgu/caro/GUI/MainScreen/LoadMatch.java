package com.sgu.caro.GUI.MainScreen;

import com.sgu.caro.GUI.Login.Login;
import com.sgu.caro.GUI.MatchScreen.Cell;
import com.sgu.caro.GUI.MatchScreen.MatchDesign;
import com.sgu.caro.GUI.WindowManager;
import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.Socket;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.json.JSONObject;

public class LoadMatch extends javax.swing.JFrame {

    private static SocketConnection socket = new SocketConnection();
    private static DataSocket dataSocket = new DataSocket();
    private static int userId = new TokenManager().getUser_id();
    private boolean getPairing = false;
    private boolean clickHuyBtn = false;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LoadMatch() {
        clickHuyBtn = true;
        socket.addListenConnection("send_invitation", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                getPairing = true;
                System.out.println(data.toString());
                int userId = data.getInt("user");
                String displayname = data.getString("display_name");
                int score = data.getInt("score");
                btnDongY.setEnabled(true);
                txtUserName.setText(displayname);
                txtScore.setText(String.valueOf(score));

                MatchDesign.user2 = userId;
                
                final Runnable runnable = new Runnable() {
                    int countdownStarter = 30;
                    @Override
                    public void run() {

                        System.out.println(countdownStarter);
                        txtTime.setText(String.valueOf(countdownStarter) + "s");
                        countdownStarter--;

                        if (countdownStarter < 0) {
                            System.out.println("Timer Over!");
                            btnHuyMouseClicked(null);
                            scheduler.shutdown();
                        }
                    }
                };
                scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
            }
        });

        socket.addListenConnection("start_match", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                boolean is_started = data.getBoolean("is_started");
                String step_type = data.getString("step_type");
                System.out.println(data.toString());
                if (is_started) {
                    startMatchScreen(step_type);
                } else {
                    System.out.println("Not accept");
                    
                    if (clickHuyBtn){
                        btnHuyMouseClicked(null);
                    }
                }
            }
        });

        initComponents();

        this.setTitle("Thời gian");
        try {
            setIconImage((new ImageIcon()).getImage());
            setIconImage((new ImageIcon(ImageIO.read(new File("images/xo.png")))).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) dimension.getWidth() / 2 - this.getWidth() / 2;
        int y = (int) dimension.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (!getPairing) {
            btnDongY.setEnabled(false);
        }
    }

    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        lblClock = new javax.swing.JLabel();
        btnHuy = new javax.swing.JButton();
        btnDongY = new javax.swing.JButton();
        lblDoiThu = new javax.swing.JLabel();
        lblDiem = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        txtUserName = new javax.swing.JLabel();
        txtScore = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        mainPanel.setBackground(new java.awt.Color(204, 204, 255));
        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblClock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblClock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblClock.setText("THỜI GIAN CHỜ:");

        btnHuy.setText("HỦY");
        btnHuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHuyMouseClicked(evt);
            }
        });

        btnDongY.setText("ĐỒNG Ý");
        btnDongY.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDongYMouseClicked(evt);
            }
        });

        lblDoiThu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDoiThu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDoiThu.setText("Đối thủ:");

        lblDiem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDiem.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDiem.setText("Điểm:");

        txtTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtTime.setText("30s");

        txtUserName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtUserName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtUserName.setText("???");

        txtScore.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtScore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtScore.setText("???");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDongY, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblDoiThu, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(lblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblDoiThu, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                                .addComponent(lblDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDongY, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void btnHuyMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        scheduler.shutdown();
        clickHuyBtn = false;
        if (getPairing) {
            String dataSend = dataSocket.exportDataAcceptPairing(userId, false);
            System.out.println("Sending: " + dataSend);
            socket.sendData(dataSend);
        }
        else{
            String dataSend = dataSocket.exportDataOutMatch(userId);
            System.out.println("Sending: " + dataSend);
            socket.sendData(dataSend);
        }
        this.setVisible(false);
        this.dispose();
        MainScreenDesign.loadMatch = true;
    }

    private void btnDongYMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        scheduler.shutdown();
        
        btnDongY.setEnabled(false);
        
        String dataSend = dataSocket.exportDataAcceptPairing(userId, true);
        System.out.println(dataSend);
        socket.sendData(dataSend);

        // Giao dienj waiting for other
    }

    private void startMatchScreen(String step_type) {
        WindowManager.matchScreen = new MatchDesign(step_type);
        WindowManager.matchScreen.setVisible(true);
        WindowManager.mainScreen.setVisible(false);
        MainScreenDesign.loadMatch = true;
        this.setVisible(false);
        this.dispose();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnDongY;
    private javax.swing.JButton btnHuy;
    private javax.swing.JLabel lblClock;
    private javax.swing.JLabel lblDiem;
    private javax.swing.JLabel lblDoiThu;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel txtScore;
    private javax.swing.JLabel txtTime;
    private javax.swing.JLabel txtUserName;
    // End of variables declaration                   
}
