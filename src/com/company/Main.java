package com.company;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Moje okno!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Panel());

        frame.setPreferredSize(new Dimension(800,800));
        frame.pack();
        frame.setVisible(true);



    }
}