package com.sgu.caro.GUI.MainScreen;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class KyThu extends javax.swing.JPanel {

    private ImageIcon imgOnline, imgOffLine;
    
    public KyThu() {
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
        lblIcon.setIcon(imgOffLine);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        lblUserName = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lblUserName.setText("Username1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(lblIcon)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblUserName;
    // End of variables declaration                   
}