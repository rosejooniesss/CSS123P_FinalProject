/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package css123p_finalproject;

import javax.swing.*;
import css123p_finalproject.view.GameFrame;

/**
 * Author: Group 3
 *
 */
public class Main {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){

            public void run(){

                initializeApplicationFrame();

            }
        });
    }
    private static void initializeApplicationFrame(){

        GameFrame frame = new GameFrame();
        frame.setVisible(true);
        JButton ExitButton = new JButton();

    }
}
