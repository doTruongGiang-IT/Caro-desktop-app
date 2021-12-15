package com.sgu.caro.GUI.MatchScreen;

import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserPanel extends javax.swing.JPanel {

    private String step_type;
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledExecutorService schedulerTotalTime = Executors.newScheduledThreadPool(1);
    public static boolean is_running = true;
    public static boolean is_timer_running = false;
    public static boolean toogle_status = false;
    public static int countdownStarter = 30;
    public static String userChoice = "";

    public UserPanel(String step_type, int user_1, String username_1, String score_1, int user_2, String username_2, String score_2) {
        this.step_type = step_type;
        this.is_running = true;
        initComponents(user_1, username_1, score_1, user_2, username_2, score_2);

        listUser.setEditable(false);
        listUser.setText("Khán giả 1\nKhán giả 2\nKhán giả 3\n");
        SocketConnection socket = new SocketConnection();
        socket.addListenConnection("get_watcher", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                String watcher_content = "";
                JSONArray watchers = data.getJSONArray("watchers");

                for (int i = 0; i < watchers.length(); i++) {
                    watcher_content += watchers.getString(i) + "\n";
                }
                listUser.setText(watcher_content);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (user_1 == TokenManager.getUser_id()) {
                    setTimer(1);
                } else {
                    setTimer(2);
                }
                System.out.println("Running");
            }
        });
        thread.start();
    }

    public void setTimer(int userOrder) {
        if (!step_type.equals("X") && !step_type.equals("O")) {
            txtTotalTime.setText("");
        }
        else{
            if (!schedulerTotalTime.isShutdown()) {
                schedulerTotalTime.shutdown();
                schedulerTotalTime = Executors.newScheduledThreadPool(1);
            }

            final Runnable runnableTotalTime = new Runnable() {
                int countdownMatch = 600;

                @Override
                public void run() {

                    System.out.println(countdownMatch);
                    txtTotalTime.setText(String.valueOf(countdownMatch / 60) + "m:" + String.valueOf(countdownMatch % 60) + "s");
                    countdownMatch--;

                    if (countdownMatch < 0) {
                        System.out.println("Timer Over!");
                        schedulerTotalTime.shutdown();
                        schedulerTotalTime = Executors.newScheduledThreadPool(1);
                        if (!scheduler.isShutdown()) {
                            scheduler.shutdown();
                            scheduler = Executors.newScheduledThreadPool(1);
                        }
                        
                        if (step_type.equals("X")){
                            SocketConnection socket = new SocketConnection();
                            DataSocket dataSocket = new DataSocket();
                            String dataSend = dataSocket.exportDataTimeoutMatch(new TokenManager().getUser_id());
                            System.out.println(dataSend);
                            socket.sendData(dataSend);
                        }
                    }
                }
            };
            schedulerTotalTime.scheduleAtFixedRate(runnableTotalTime, 0, 1, SECONDS);
        }
        
        while (is_running) {
            if (!step_type.equals("X") && !step_type.equals("O")) {
                txtTime.setText("");
                txtTime2.setText("");
            } else if (is_timer_running) {
                if (toogle_status) {
                    toogle_status = false;
                    countdownStarter = 30;
                    if (!scheduler.isShutdown()) {
                        scheduler.shutdown();
                        scheduler = Executors.newScheduledThreadPool(1);
                    }

                    final Runnable runnable = new Runnable() {

                        @Override
                        public void run() {

                            System.out.println(countdownStarter);
                            if (userOrder == 1) {
                                txtTime.setText(String.valueOf(countdownStarter) + "s");
                                jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true), "X"));
                                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "O"));
                            } else {
                                txtTime2.setText(String.valueOf(countdownStarter) + "s");
                                jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "X"));
                                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true), "O"));
                            }
                            countdownStarter--;

                            if (countdownStarter < 0) {
                                System.out.println("Timer Over!");
                                scheduler.shutdown();
                                scheduler = Executors.newScheduledThreadPool(1);
                                if (!schedulerTotalTime.isShutdown()) {
                                    schedulerTotalTime.shutdown();
                                    schedulerTotalTime = Executors.newScheduledThreadPool(1);    
                                }
                                SocketConnection socket = new SocketConnection();
                                DataSocket dataSocket = new DataSocket();
                                String dataSend = dataSocket.exportDataTimeoutPlayer(new TokenManager().getUser_id());
                                System.out.println(dataSend);
                                socket.sendData(dataSend);
                            }
                        }
                    };
                    scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
                }
            } else {
                if (toogle_status) {
                    toogle_status = false;

                    countdownStarter = 30;
                    if (!scheduler.isShutdown()) {
                        scheduler.shutdown();
                        scheduler = Executors.newScheduledThreadPool(1);
                    }

                    final Runnable runnable = new Runnable() {

                        @Override
                        public void run() {

                            System.out.println(countdownStarter);
                            if (userOrder == 1) {
                                txtTime2.setText(String.valueOf(countdownStarter) + "s");
                                jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "X"));
                                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true), "O"));
                            } else {
                                txtTime.setText(String.valueOf(countdownStarter) + "s");
                                jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true), "X"));
                                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "O"));
                            }
                            countdownStarter--;

                            if (countdownStarter < 0) {
                                System.out.println("Timer Over!");
                                scheduler.shutdown();
                                scheduler = Executors.newScheduledThreadPool(1);
                                if (!schedulerTotalTime.isShutdown()) {
                                    schedulerTotalTime.shutdown();
                                    schedulerTotalTime = Executors.newScheduledThreadPool(1);
                                }
                            }
                        }
                    };
                    scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    private void initComponents(int user_1, String username_1, String score_1, int user_2, String username_2, String score_2) {
        jPanel2 = new javax.swing.JPanel();
        lblUsername1 = new javax.swing.JLabel();
        lblScore1 = new javax.swing.JLabel();
        lblTime1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JLabel();
        txtScore = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblUsername2 = new javax.swing.JLabel();
        lblScore2 = new javax.swing.JLabel();
        lblTime2 = new javax.swing.JLabel();
        txtUsername2 = new javax.swing.JLabel();
        txtScore2 = new javax.swing.JLabel();
        txtTime2 = new javax.swing.JLabel();
        scrollPaneListUser = new javax.swing.JScrollPane();
        listUser = new javax.swing.JTextArea();
        btnVaoChoi = new javax.swing.JButton();
        btnRoiPhong = new javax.swing.JButton();
        panelMain = new javax.swing.JPanel();
        panelToTalTime = new javax.swing.JPanel();
        lblToTalTime = new javax.swing.JLabel();
        txtTotalTime = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 204, 255));
        setMaximumSize(new java.awt.Dimension(250, 600));
        setPreferredSize(new java.awt.Dimension(250, 600));

        panelMain.setBackground(new java.awt.Color(153, 204, 255));
        panelMain.setMaximumSize(new java.awt.Dimension(250, 600));
        panelMain.setPreferredSize(new java.awt.Dimension(250, 600));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "X"));

        lblUsername1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsername1.setText("Username");

        lblScore1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore1.setText("Score");

        lblTime1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTime1.setText("Time");

        txtUsername.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtUsername.setText(username_1);

        txtScore.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtScore.setText(score_1);

        txtTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTime.setForeground(new java.awt.Color(255, 0, 0));
        txtTime.setText("30s");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblUsername1)
                                        .addComponent(lblScore1)
                                        .addComponent(lblTime1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtScore, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblUsername1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblScore1)
                                        .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTime1)
                                        .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "O"));

        lblUsername2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsername2.setText("Username");

        lblScore2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore2.setText("Score");

        lblTime2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTime2.setText("Time");

        txtUsername2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtUsername2.setText(username_2);

        txtScore2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtScore2.setText(score_2);

        txtTime2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTime2.setForeground(new java.awt.Color(255, 0, 0));
        txtTime2.setText("30s");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblUsername2)
                                        .addComponent(lblScore2)
                                        .addComponent(lblTime2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTime2, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                        .addComponent(txtScore2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtUsername2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblUsername2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUsername2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblScore2)
                                        .addComponent(txtScore2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTime2)
                                        .addComponent(txtTime2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        scrollPaneListUser.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Khán giả"));

        listUser.setColumns(16);
        listUser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        listUser.setLineWrap(true);
        listUser.setRows(5);
        scrollPaneListUser.setViewportView(listUser);

        btnRoiPhong.setText("Rời Phòng");

        btnRoiPhong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!step_type.equals("X") && !step_type.equals("O")) {
                    new OutMatchScreen(true, user_1, 1, "Xác nhận", "Bạn có muốn thoát trận đấu? (Người xem) ", "Đồng Ý", "Quay Lại").setVisible(true);
                } else {
                    new OutMatchScreen(false, user_1, 1, "Xác nhận", "Bạn có muốn thoát trận đấu?", "Đồng Ý", "Quay Lại").setVisible(true);
                }
            }
        });

        panelToTalTime.setBackground(new java.awt.Color(255, 255, 255));
        panelToTalTime.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Total Time"));

        lblToTalTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblToTalTime.setText("Total Time:");

        txtTotalTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTotalTime.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalTime.setText("Username");

        javax.swing.GroupLayout panelToTalTimeLayout = new javax.swing.GroupLayout(panelToTalTime);
        panelToTalTime.setLayout(panelToTalTimeLayout);
        panelToTalTimeLayout.setHorizontalGroup(
                panelToTalTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelToTalTimeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblToTalTime)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(19, 19, 19))
        );
        panelToTalTimeLayout.setVerticalGroup(
                panelToTalTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelToTalTimeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelToTalTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTotalTime, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                                        .addComponent(lblToTalTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
                panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMainLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(btnVaoChoi)
                                .addGap(18, 18, 18)
                                .addComponent(btnRoiPhong)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(panelMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPaneListUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelToTalTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
                panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelToTalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(scrollPaneListUser, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnRoiPhong)
                                        .addComponent(btnVaoChoi))
                                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>                         

    // Variables declaration - do not modify  
    private javax.swing.JButton btnRoiPhong;
    private javax.swing.JButton btnVaoChoi;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblScore1;
    private javax.swing.JLabel lblScore2;
    private javax.swing.JLabel lblTime1;
    private javax.swing.JLabel lblTime2;
    private javax.swing.JLabel lblUsername1;
    private javax.swing.JLabel lblUsername2;
    private javax.swing.JTextArea listUser;
    private javax.swing.JScrollPane scrollPaneListUser;
    private javax.swing.JLabel txtScore;
    private javax.swing.JLabel txtScore2;
    private javax.swing.JLabel txtTime;
    private javax.swing.JLabel txtTime2;
    private javax.swing.JLabel txtUsername;
    private javax.swing.JLabel txtUsername2;

    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelToTalTime;
    private javax.swing.JLabel lblToTalTime;
    private javax.swing.JLabel txtTotalTime;
    // End of variables declaration                   
}
