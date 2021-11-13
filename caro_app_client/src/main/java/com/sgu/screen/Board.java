package com.sgu.screen;

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

public class Board extends JPanel{
    private static int N = 20;
    private static int M = 20;
    
    private Image imgX, imgO, imgOCurrent, imgXCurrent;
    private String currentPlayer = Cell.O_VALUE;
    
    private Cell[][] matrix = new Cell[N][M];
    Cell currentCell;
    
    public Board() {
        this.setPreferredSize(new Dimension(605, 600));
        
        for(int i = 0; i < N; ++i) {
            for(int j = 0; j < M; ++j) {
                Cell cell = new Cell();
                matrix[i][j] = cell;
                
            }
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e); 
                int xClick = e.getX();
                int yClick = e.getY();
                
                for(int i = 0; i < N; ++i) {
                    for(int j = 0; j < M; ++j) {
                        Cell cell = matrix[i][j];
                        if(xClick > cell.getX() && xClick <= (cell.getW() + cell.getX()) && yClick > cell.getY() && yClick <= (cell.getY() + cell.getH())){
                            System.out.println(cell.getX() + " " + cell.getY());
                            if(cell.getValue().equals("")){
                                cell.setValue(currentPlayer);
                                currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
                                currentCell = cell;
                                validate();
                                repaint();
                            }
                        }
                    }
                }
            }
        });
        
        try{
            ImageIcon img; 
            img = new ImageIcon(ImageIO.read(new File("images/x.png")));
            imgX = img.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            
            img = new ImageIcon(ImageIO.read(new File("images/x-current.png")));
            imgXCurrent = img.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
     
            img = new ImageIcon(ImageIO.read(new File("images/o.png")));
            imgO = img.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            
            img = new ImageIcon(ImageIO.read(new File("images/o-current.png")));
            imgOCurrent = img.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth() / 20;
        int h = getHeight() / 20;
        
        
        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setColor(Color.red);
        
        int k = 0;
        int x = 0, y = 0;
        Color color = Color.white;
        
        Rectangle cellpaint;
        
        for(int i = 0; i < N; ++i) {
            x = i * w;
            for(int j = 0; j < M; ++ j) {
                y = j * h;
                
                Cell cell = matrix[i][j];
                cell.setY(y);
                cell.setX(x);
                cell.setW(w);
                cell.setH(h);
                
                graphics2D.setColor(color);
                
                
                cellpaint = new Rectangle(x, y, w, h);
                graphics2D.draw(cellpaint);
                
                if(cell.getValue().equals(Cell.O_VALUE)) {
                    graphics2D.drawImage(imgO, x, y, w, h, this);
                }
                else if(cell.getValue().equals(Cell.X_VALUE)) {
                    graphics2D.drawImage(imgX, x, y, w, h, this);
                }
                ++k;
            }
            ++k;
        }
        
    }
    
}
