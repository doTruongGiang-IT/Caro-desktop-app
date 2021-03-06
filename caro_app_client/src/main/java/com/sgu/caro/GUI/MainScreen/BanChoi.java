package com.sgu.caro.GUI.MainScreen;

import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.json.JSONObject;

public class BanChoi extends javax.swing.JPanel {

    private int stt;
    private String user_1, user_2, username_1, username_2;
    private int numberOfWatchers;

    public BanChoi(int stt, String user_1, String username_1, String user_2, String username_2, int numberOfWatchers) {
        this.stt = stt;
        this.user_1 = user_1;
        this.user_2 = user_2;
        this.username_1 = username_1;
        this.username_2 = username_2;
        this.numberOfWatchers = numberOfWatchers;
        initComponents();
    }

    private void initComponents() {

        lblStt = new javax.swing.JLabel();
        lblUser1 = new javax.swing.JLabel();
        lblUser2 = new javax.swing.JLabel();
        lblBanChoi = new javax.swing.JLabel();
        btnWatch = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMaximumSize(new java.awt.Dimension(600, 87));

        lblStt.setText(String.valueOf(stt));
        lblUser1.setText("User 1: " + username_1);
        lblUser2.setText("User 2: " + username_2);
        lblBanChoi.setText("Watchers: " + String.valueOf(numberOfWatchers));

        btnWatch.setText("Vào Xem");
        btnWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SocketConnection socket = new SocketConnection();
                DataSocket dataSocket = new DataSocket();
                socket.sendData(dataSocket.exportDataGoWatch(new TokenManager().getUser_id(), Integer.parseInt(user_1)));
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblBanChoi, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(lblUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblUser2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(btnWatch)
                                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(21, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblUser2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblBanChoi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnWatch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19))
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnWatch;
    private javax.swing.JLabel lblBanChoi;
    private javax.swing.JLabel lblStt;
    private javax.swing.JLabel lblUser1;
    private javax.swing.JLabel lblUser2;
    // End of variables declaration                   
}
