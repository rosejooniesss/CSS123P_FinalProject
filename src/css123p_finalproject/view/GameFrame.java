package css123p_finalproject.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import css123p_finalproject.model.GameEnvironment;
import css123p_finalproject.model.Mob;
import css123p_finalproject.model.Skeleton;
import css123p_finalproject.model.Player;
import css123p_finalproject.model.Projectile;
import css123p_finalproject.controller.Controller;
import css123p_finalproject.controller.events.EventHandling;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("CSS123P Final Project - Arena Crawler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); 
        
        GamePanel panel = new GamePanel();
        add(panel);
        
        // --- JAVAMENUBAR & MENUITEMS (Menu Events Requirement) ---
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Game");
        JMenuItem startItem = new JMenuItem("Start / Restart");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        startItem.addActionListener(e -> {
            panel.environment.restartGame();
            panel.environment.startGame();
            panel.requestFocusInWindow();
        });
        
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(startItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem controlsItem = new JMenuItem("Controls Guide");
        controlsItem.addActionListener(e -> JOptionPane.showMessageDialog(
            this, 
            "Controls:\n- WASD / Arrow Keys: Move\n- Right-Click: Dash\n- Keys 1, 2, 3: Switch Weapons (Sword, Gun, Staff)\n- Left-Click (Hold on Enemy): Continuous Attack", 
            "How to Play", 
            JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(controlsItem);
        
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        // ---------------------------------------------------------
    }
}

class GamePanel extends JPanel implements ActionListener {
    public GameEnvironment environment;
    private Controller controller;
    private EventHandling inputHandler;

    private java.awt.image.BufferedImage playerSprite;
    private java.awt.image.BufferedImage goblinSprite;
    private java.awt.image.BufferedImage skeletonSprite; // Added skeleton image reference
    private Timer gameLoop;

    private boolean isMouseDown = false;
    private int mouseX = 0;
    private int mouseY = 0;

    public GamePanel() {
        setBackground(Color.DARK_GRAY);
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();

        environment = new GameEnvironment();
        controller = new Controller(environment);
        inputHandler = new EventHandling();

        addKeyListener(inputHandler);

        // UI Pause Button
        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(680, 15, 90, 30);
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> {
            environment.togglePause();
            pauseButton.setText(environment.isPaused() ? "Resume" : "Pause");
            requestFocusInWindow();
        });
        add(pauseButton);

        // Load graphical sprites into the View
        try {
            java.net.URL pUrl = getClass().getResource("/css123p_finalproject/model/character/sprites/players/playerOneIdle.jpg");
            if (pUrl != null) playerSprite = javax.imageio.ImageIO.read(pUrl);

            java.net.URL gUrl = getClass().getResource("/css123p_finalproject/model/character/sprites/mobs/goblinIdle.jpg");
            if (gUrl != null) goblinSprite = javax.imageio.ImageIO.read(gUrl);

            java.net.URL sUrl = getClass().getResource("/css123p_finalproject/model/character/sprites/mobs/skeletonIdle.jpg");
            if (sUrl != null) skeletonSprite = javax.imageio.ImageIO.read(sUrl);
        } catch (Exception e) {
            System.err.println("Failed to load sprites.");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (environment.isTitleScreen() || environment.isPaused() || environment.isWaitingForFloorPrompt()) return;
                if (SwingUtilities.isRightMouseButton(e)) {
                    inputHandler.dashRequested = true;
                } else {
                    isMouseDown = true;
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    isMouseDown = false;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // --- TITLE SCREEN VIEW ---
        if (environment.isTitleScreen()) {
            g2d.setColor(new Color(20, 20, 30));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.ORANGE);
            g2d.setFont(new Font("Arial", Font.BOLD, 42));
            g2d.drawString("DUNGEON ARENA CRAWLER", 130, 200);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString("Use the top menu bar (Game -> Start) to begin your quest!", 170, 270);
            return;
        }
        // -------------------------

        g2d.setColor(Color.WHITE);
        if (environment.isGameWon()) {
            g2d.drawString("YOU CLEARED ALL FLOORS! YOU WIN!", 300, 300);
            return;
        }

        Player p = environment.getPlayer();
        g2d.drawString("FLOOR " + environment.getCurrentFloor() + " | Weapon: " + p.getEquippedWeapon().getName(), 20, 20);

        if (playerSprite != null) {
            g2d.drawImage(playerSprite, p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);
        } else {
            g2d.setColor(Color.BLUE);
            g2d.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        }
        g2d.setColor(Color.GREEN);
        g2d.drawRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("HP: " + p.getHP() + "/" + (int) p.MAXHP, p.getX(), p.getY() - 10);

        // Render Enemies (Polymorphic checks for Goblins vs Skeletons)
        for (Mob mob : environment.getEnemies()) {
            if (mob.isAlive()) {
                if (mob instanceof Skeleton) {
                    if (skeletonSprite != null) {
                        g2d.drawImage(skeletonSprite, mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight(), null);
                    } else {
                        g2d.setColor(Color.MAGENTA);
                        g2d.fillRect(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight());
                    }
                } else {
                    if (goblinSprite != null) {
                        g2d.drawImage(goblinSprite, mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight(), null);
                    } else {
                        g2d.setColor(Color.RED);
                        g2d.fillRect(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight());
                    }
                }
                g2d.setColor(Color.WHITE);
                g2d.drawString("HP: " + mob.getHP(), mob.getX(), mob.getY() - 5);
            }
        }

        for (Projectile pr : environment.getProjectiles()) {
            pr.draw(g2d);
        }

        for (Projectile ePr : environment.getEnemyProjectiles()) {
            ePr.draw(g2d);
        }

        if (environment.isPaused()) {
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.drawString("PAUSED", 325, 280);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (environment.isTitleScreen() || environment.isPaused()) {
            repaint();
            return;
        }

        // --- CHECK FOR FLOOR 1 PROMPT ---
        if (environment.isWaitingForFloorPrompt()) {
            int choice = JOptionPane.showConfirmDialog(
                this, 
                "Floor 1 Cleared! Would you like to proceed to Floor 2?", 
                "Floor 1 Complete", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                environment.loadFloor(2);
                environment.setWaitingForFloorPrompt(false);
            } else {
                System.exit(0);
            }
            requestFocusInWindow();
            return;
        }
        // --------------------------------

        controller.processInput(inputHandler);

        if (isMouseDown) {
            controller.handleAttack(mouseX, mouseY);
        }

        controller.update();
        repaint();
    }
}