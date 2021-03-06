package com.sgu.caro.GUI.MatchScreen;

import static com.sgu.caro.GUI.MatchScreen.UserPanel.is_timer_running;
import static com.sgu.caro.GUI.MatchScreen.UserPanel.schedulerTotalTime;
import com.sgu.caro.api_connection.TokenManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

public class Board extends JPanel {

    private static int N = 20;
    private static int M = 20;
    private int width = 600, height = 600;

    private Image imgX, imgO, imgOCurrent, imgXCurrent;
    private String currentPlayer = Cell.X_VALUE;
    private SocketConnection socket;
    private DataSocket dataSocket;
    private boolean isFree = (currentPlayer.equals(Cell.X_VALUE));

    private Cell[][] matrix = new Cell[N][M];
    Cell currentCell;

    /* For testing */
    private final int userID = TokenManager.getUser_id();

    public Board(String stepType) {

        if (stepType.equals("X")) {
            currentPlayer = Cell.X_VALUE;
        } else if (stepType.equals("O")) {
            currentPlayer = Cell.O_VALUE;
        } else {
            currentPlayer = Cell.EMPTY_VALUE;
        }
        isFree = (currentPlayer.equals(Cell.X_VALUE));

        this.setPreferredSize(new Dimension(width, height));
        socket = new SocketConnection();
        dataSocket = new DataSocket();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Cell cell = new Cell();
                matrix[i][j] = cell;
            }
        }

        if (isFree) {
            UserPanel.is_timer_running = true;
            UserPanel.toogle_status = true;
        } else {
            UserPanel.is_timer_running = false;
            UserPanel.toogle_status = true;
        }

        socket.addListenConnection("go_step", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                int currentUserID = data.getInt("user");
                int posX, posY;
                posX = data.getJSONArray("pos").getInt(0);
                posY = data.getJSONArray("pos").getInt(1);

                Cell cell = matrix[posX - 1][posY - 1];

                if (!currentPlayer.equals(Cell.EMPTY_VALUE)) {
                    if (currentUserID == userID) {
                        cell.setValue(currentPlayer);
                        UserPanel.is_timer_running = false;
                        UserPanel.toogle_status = true;
                    } else {
                        cell.setValue(currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE);
                        isFree = true;
                        UserPanel.is_timer_running = true;
                        UserPanel.toogle_status = true;
                    }
                } else {
                    if (Integer.parseInt(stepType) == currentUserID) {
                        cell.setValue(Cell.X_VALUE);
                    } else {
                        cell.setValue(Cell.O_VALUE);
                    }
                }
                validate();
                repaint();
            }
        });

        socket.addListenConnection("result_match", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                try {
                    int currentUserID = data.getInt("user");
                    int posX, posY;
                    boolean win = false;
                    isFree = false;

                    UserPanel.scheduler.shutdown();
                    UserPanel.scheduler = Executors.newScheduledThreadPool(1);
                    UserPanel.schedulerTotalTime.shutdown();
                    UserPanel.schedulerTotalTime = Executors.newScheduledThreadPool(1);
                    
                    UserPanel.is_running = false;
                    
                    
                    if (data.getJSONArray("res_pos").length() > 0) {
                        for (int i = 0; i < 5; i++) {
                            JSONArray e = data.getJSONArray("res_pos").getJSONArray(i);
                            posX = e.getInt(0);
                            posY = e.getInt(1);

                            Cell cell = matrix[posX - 1][posY - 1];
                            if (!currentPlayer.equals(Cell.EMPTY_VALUE)) {
                                if (currentUserID == userID) {
                                    cell.setValue(currentPlayer.equals(Cell.O_VALUE) ? Cell.O_VALUE_WON : Cell.X_VALUE_WON);
                                    win = true;
                                } else {
                                    cell.setValue(currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE_WON : Cell.O_VALUE_WON);
                                }
                            } else {
                                if (Integer.parseInt(stepType) == currentUserID) {
                                    cell.setValue(Cell.X_VALUE_WON);
                                } else {
                                    cell.setValue(Cell.O_VALUE_WON);
                                }
                            }
                        }
                    }
                    Thread.sleep(500);

                    String message;
                    TokenManager userManagement = new TokenManager();
                    if (!currentPlayer.equals(Cell.EMPTY_VALUE)) {
                        if (currentUserID == 0) {
                            message = "B???n ???? h??a.";
                            new ResultMatchScreen(1, "Th??ng B??o", message, "?????ng ??").setVisible(true);
                            userManagement.setScore(userManagement.getScore() + 1);
                        } else if (currentUserID == userID || win) {
                            message = "B???n ???? chi???n th???ng.";
                            new ResultMatchScreen(1, "Th??ng B??o", message, "?????ng ??").setVisible(true);
                            userManagement.setScore(userManagement.getScore() + 3);
                        } else {
                            message = "R???t ti???c b???n ???? thua";
                            new ResultMatchScreen(0, "Th??ng B??o", message, "?????ng ??").setVisible(true);
                            userManagement.setScore(Math.max(0, userManagement.getScore() - 1));
                        }
                    } else {
                        message = "Tr???n ?????u ???? k???t th??c";
                        new ResultMatchScreen(1, "Th??ng B??o", message, "?????ng ??").setVisible(true);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        int cellSize = width / N;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int xClick = e.getX();
                int yClick = e.getY();

                for (int i = 0; i < N; ++i) {
                    for (int j = 0; j < M; ++j) {
                        Cell cell = matrix[i][j];
                        if (xClick > cell.getPosX() && xClick <= (cell.getWidth() + cell.getPosX()) && yClick > cell.getPosY() && yClick <= (cell.getPosY() + cell.getHeight())) {
                            int cellPosX = cell.getPosX() / cellSize + 1;
                            int cellPosY = cell.getPosY() / cellSize + 1;
                            System.out.println(cellPosX + " " + cellPosY);
                            System.out.println(currentPlayer);

                            if (!currentPlayer.equals(Cell.EMPTY_VALUE) && cell.getValue().equals("") && isFree) {
                                String data = dataSocket.exportDataGoStep(userID, cellPosX, cellPosY);
                                socket.sendData(data);
                                isFree = false;
//                                cell.setValue(currentPlayer);
//                                currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
                                currentCell = cell;
//                                validate();
//                                repaint();
                            }
                        }
                    }
                }
            }
        });

        try {
            ImageIcon img;
            img = new ImageIcon(ImageIO.read(new File("images/x.png")));
            imgX = img.getImage().getScaledInstance(width / N, height / M, Image.SCALE_SMOOTH);

            img = new ImageIcon(ImageIO.read(new File("images/x-current.png")));
            imgXCurrent = img.getImage().getScaledInstance(width / N, height / M, Image.SCALE_SMOOTH);

            img = new ImageIcon(ImageIO.read(new File("images/o.png")));
            imgO = img.getImage().getScaledInstance(width / N, height / M, Image.SCALE_SMOOTH);

            img = new ImageIcon(ImageIO.read(new File("images/o-current.png")));
            imgOCurrent = img.getImage().getScaledInstance(width / N, height / M, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMatrix(Cell[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth() / 20;
        int h = getHeight() / 20;

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(Color.red);

        int k = 0;
        int x = 0, y = 0;
        Color color = Color.white;

        Rectangle cellpaint;

        for (int i = 0; i < N; ++i) {
            x = i * w;
            for (int j = 0; j < M; ++j) {
                y = j * h;

                Cell cell = matrix[i][j];
                cell.setPosY(y);
                cell.setPosX(x);
                cell.setWidth(w);
                cell.setHeight(h);

                graphics2D.setColor(color);

                cellpaint = new Rectangle(x, y, w, h);
                graphics2D.draw(cellpaint);

                if (cell.getValue().equals(Cell.O_VALUE)) {
                    graphics2D.drawImage(imgO, x, y, w, h, this);
                } else if (cell.getValue().equals(Cell.X_VALUE)) {
                    graphics2D.drawImage(imgX, x, y, w, h, this);
                } else if (cell.getValue().equals(Cell.O_VALUE_WON)) {
                    graphics2D.drawImage(imgOCurrent, x, y, w, h, this);
                } else if (cell.getValue().equals(Cell.X_VALUE_WON)) {
                    graphics2D.drawImage(imgXCurrent, x, y, w, h, this);
                }
                ++k;
            }
            ++k;
        }

    }

}
