package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    static GamePanel gamePanel = null;

    public static void main(String[] args) {
        JFrame window = new JFrame("Chess");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setResizable(true);

        gamePanel = new GamePanel();
        window.add(gamePanel, BorderLayout.CENTER);

        window.pack();
        window.setLocationRelativeTo(null); 
        window.setVisible(true);

        System.out.println("Game started!");

        gamePanel.LaunchGame();
    }
}
