package gamemenudraft;

import javax.swing.*;
import java.awt.*;

public class View extends ApplicationFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private EventHandling eventHandling;

    public View() {
        add(root);
    }

    public void setEventHandling(EventHandling eventHandling) {
        this.eventHandling = eventHandling;
    }

    public void showMainMenu() {
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(new Color(20, 24, 40));

        JPanel box = new JPanel(new GridLayout(0, 1, 0, 14));
        box.setOpaque(false);

        JLabel title = new JLabel("MY GAME", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 56));
        title.setForeground(Color.WHITE);
        box.add(title);
        box.add(Box.createVerticalStrut(20));
        box.add(menuButton("Play", "PLAY"));
        box.add(menuButton("Level Selection", "LEVELS"));
        box.add(menuButton("Quit", "QUIT"));

        menu.add(box);
        show(menu, "main");
    }

    public void showLevelSelection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(20, 24, 40));

        JPanel box = new JPanel(new GridLayout(0, 1, 0, 14));
        box.setOpaque(false);
        JLabel title = new JLabel("Select Level", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        box.add(title);
        for (int i = 1; i <= 3; i++) box.add(menuButton("Level " + i, "LEVEL_" + i));
        box.add(menuButton("Back", "BACK"));

        panel.add(box);
        show(panel, "levels");
    }

    public void showLevel(GameEnvironment env) {
        JPanel game = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                showPlayerUI(g, env.getPlayer());
            }
        };
        game.setBackground(new Color(35, 60, 45));
        JLabel label = new JLabel("  Level " + env.getMapData().level, SwingConstants.LEFT);
        label.setForeground(Color.WHITE);
        game.add(label, BorderLayout.NORTH);
        game.add(menuButton("Back to Menu", "BACK"), BorderLayout.SOUTH);
        show(game, "game");
    }

    private void showPlayerUI(Graphics g, Player player) {
        g.setColor(Color.CYAN);
        g.fillRect(player.x, player.y, 32, 32);
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.health, 20, 50);
    }

    private JButton menuButton(String text, String command) {
        JButton b = new JButton(text);
        b.setActionCommand(command);
        b.setFont(new Font("SansSerif", Font.PLAIN, 22));
        b.setFocusPainted(false);
        b.addActionListener(eventHandling);
        return b;
    }

    private void show(JPanel panel, String name) {
        root.add(panel, name);
        cards.show(root, name);
        root.revalidate();
        root.repaint();
    }
}
