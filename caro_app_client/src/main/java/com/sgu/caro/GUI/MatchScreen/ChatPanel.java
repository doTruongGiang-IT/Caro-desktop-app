package com.sgu.caro.GUI.MatchScreen;

import com.sgu.caro.api_connection.TokenManager;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import org.json.JSONObject;
import java.awt.event.*;

public class ChatPanel extends javax.swing.JPanel {

    private SocketConnection socket;
    private DataSocket dataSocket;
    private int userId = TokenManager.getUser_id();

    public ChatPanel() {
        initComponents();
    }
                   
    private void initComponents() {
        socket = new SocketConnection();
        dataSocket = new DataSocket();

        scrollPaneChat = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextArea();
        inputText = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(250, 600));
        setPreferredSize(new java.awt.Dimension(250, 600));

        scrollPaneChat.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Trò chuyện"));

        txtChat.setColumns(20);
        txtChat.setLineWrap(true);
        txtChat.setRows(5);
//        Font bolder = new Font("serif", Font.BOLD, 20);
//        txtChat.setFont(bolder);
        scrollPaneChat.setViewportView(txtChat);

        btnSubmit.setText("Gửi");

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Khi gui
                String message = inputText.getText();
                inputText.setText("");
                String dataSend = dataSocket.exportDataSendMessage(userId, message);
                System.out.println(dataSend);
                socket.sendData(dataSend);
            }
        });

        inputText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = inputText.getText();
                    inputText.setText("");
                    String dataSend = dataSocket.exportDataSendMessage(userId, message);
                    System.out.println(dataSend);
                    socket.sendData(dataSend);
                }
            }
        });

        socket.addListenConnection("send_message", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                    txtChat.append(data.getInt("user") + ":   " + data.getString("message") + "\n");
//                    String received = in.readLine();
//                    JSONObject obj = new JSONObject(received);
//                    System.out.println("obj - " + obj);
//                    txtChat.append(obj.getJSONObject("data").getInt("user") + ": " + obj.getJSONObject("data").getString("message") + "\n");
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneChat, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addComponent(btnSubmit)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(inputText)
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(scrollPaneChat, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                    .addComponent(inputText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
    }// </editor-fold>                          

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnSubmit;
    private javax.swing.JTextField inputText;
    private javax.swing.JScrollPane scrollPaneChat;
    private javax.swing.JTextArea txtChat;
    // End of variables declaration                   
}
