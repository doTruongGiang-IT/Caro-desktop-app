package com.sgu.caro.GUI.MainScreen;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class KyThu extends javax.swing.JPanel {

    private ImageIcon imgOnline, imgOffLine;
    
    private int userId;
    private String name;
    private int score;
    
    public KyThu(int userId, String name, int score) {
        this.userId = userId;
        this.name = name;
        this.score = score;
        
        try{
            Image img; 
            imgOnline = new ImageIcon(ImageIO.read(new File("images/icon-online.png")));
            img = imgOnline.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            imgOnline = new ImageIcon(img);
            
            imgOffLine = new ImageIcon(ImageIO.read(new File("images/icon-offline.png")));
            img = imgOffLine.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            imgOffLine = new ImageIcon(img);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        initComponents();
        lblIcon.setIcon(imgOnline);
    }
                     
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lblId.setText("Id: " + String.valueOf(userId));
        lblUserName.setText("Name: " + name);
        lblScore.setText("Score: " + String.valueOf(score));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblScore, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(lblIcon)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblId, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(lblScore, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblScore;
    // End of variables declaration                   
}