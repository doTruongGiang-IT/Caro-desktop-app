package com.sgu.screen;

public class ChatPanel extends javax.swing.JPanel {

    public ChatPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        scrollPaneChat = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextArea();
        inputText = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(250, 600));
        setPreferredSize(new java.awt.Dimension(250, 600));

        scrollPaneChat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, null));

        txtChat.setColumns(20);
        txtChat.setLineWrap(true);
        txtChat.setRows(5);
        txtChat.setText("hhfgejfkgefiugewufgewkgfewgwuwe fewjhiu fewkgufiew fiewgiubk fewjgfiuwge fewkjfiug  fewkhwef fewhfu");
        scrollPaneChat.setViewportView(txtChat);

        btnSubmit.setText("Gửi");

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
                .addComponent(scrollPaneChat, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addGap(27, 27, 27)
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