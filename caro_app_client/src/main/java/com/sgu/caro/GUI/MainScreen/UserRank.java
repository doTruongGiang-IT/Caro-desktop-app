package com.sgu.caro.GUI.MainScreen;

public class UserRank extends javax.swing.JPanel{
	private int userId;
	private String name;
	private int win_rate;
	private int win_length;
	private int score;

	public UserRank(int userId, String name, int win_rate, int win_length, int score) {
        this.userId = userId;
        this.name = name;
        this.win_rate = win_rate;
        this.win_length = win_length;
        this.score = score;
        initComponents();
    }
	
	private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblUserWinRate = new javax.swing.JLabel();
        lblUserWinLength = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(500, 50));

        lblId.setText(String.valueOf(userId));
        lblUserName.setText(name);
        lblUserWinRate.setText(String.valueOf(win_rate));
        lblUserWinLength.setText(String.valueOf(win_length));
        lblScore.setText(String.valueOf(score));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUserWinRate, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUserWinLength, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblScore, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblId, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(lblUserWinLength, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(lblUserWinRate, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(lblScore, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addContainerGap())
        );
	}

    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserWinRate;
    private javax.swing.JLabel lblUserWinLength;
    private javax.swing.JLabel lblScore;
}
