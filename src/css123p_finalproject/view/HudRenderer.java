package css123p_finalproject.view;

import css123p_finalproject.model.Player;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/** Heads-up display: HP bar + floor name (top-left), weapon hotbar (bottom-right). */
public class HudRenderer {

    private static final Color GOLD = new Color(214, 170, 62);
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 15);
    private static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font KEY_FONT = new Font("SansSerif", Font.BOLD, 11);

    private static final int SLOT = 44;
    private static final int GAP = 8;

    private final BufferedImage portrait;

    public HudRenderer(BufferedImage portrait) {
        this.portrait = portrait;
    }

    public void draw(Graphics2D g, Player p, String floorTitle, int panelWidth, int panelHeight) {
        Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object oldTxt = g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawStatusPanel(g, p, floorTitle);
        drawHotbar(g, p, panelWidth, panelHeight);

        if (oldAA != null) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
        if (oldTxt != null) g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldTxt);
    }

    // ------------------------------------------------------------------ top-left panel

    private void drawStatusPanel(Graphics2D g, Player p, String floorTitle) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(10, 8, 296, 72, 14, 14);
        g.setColor(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 180));
        g.drawRoundRect(10, 8, 296, 72, 14, 14);

        // hero portrait
        if (portrait != null) {
            g.setColor(new Color(36, 30, 52));
            g.fillRect(18, 16, 56, 56);
            g.drawImage(portrait, 18, 16, 56, 56, null);
            g.setColor(GOLD);
            g.setStroke(new BasicStroke(2f));
            g.drawRect(18, 16, 56, 56);
            g.setStroke(new BasicStroke(1f));
        }

        int bx = 84, by = 16, bw = 212, bh = 20;
        double ratio = Math.max(0, Math.min(1, p.getHP() / p.MAXHP));

        g.setColor(new Color(40, 10, 12));
        g.fillRoundRect(bx, by, bw, bh, 8, 8);

        Color bar = ratio > 0.5 ? new Color(70, 190, 80)
                : ratio > 0.25 ? new Color(230, 180, 40)
                : new Color(210, 50, 50);
        int fill = (int) ((bw - 4) * ratio);
        if (fill > 0) {
            g.setColor(bar);
            g.fillRoundRect(bx + 2, by + 2, fill, bh - 4, 6, 6);
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRoundRect(bx + 2, by + 2, fill, (bh - 4) / 2, 6, 6);
        }
        g.setColor(new Color(0, 0, 0, 160));
        g.drawRoundRect(bx, by, bw, bh, 8, 8);

        g.setFont(SMALL_FONT);
        String hp = "HP " + p.getHP() + " / " + (int) p.MAXHP;
        int tw = g.getFontMetrics().stringWidth(hp);
        shadowText(g, hp, bx + (bw - tw) / 2, by + 15, Color.WHITE);

        g.setFont(TITLE_FONT);
        shadowText(g, floorTitle, 84, 54, new Color(255, 226, 140));
        g.setFont(SMALL_FONT);
        shadowText(g, "Weapon: " + p.getEquippedWeapon().getName(), 84, 71, new Color(225, 225, 235));
    }

    // ------------------------------------------------------------------ bottom-right hotbar

    private void drawHotbar(Graphics2D g, Player p, int panelWidth, int panelHeight) {
        int total = 3 * SLOT + 2 * GAP;
        int x0 = panelWidth - 14 - total;
        int y0 = panelHeight - 10 - SLOT;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x0 - 8, y0 - 8, total + 16, SLOT + 16, 14, 14);

        for (int i = 0; i < 3; i++) {
            int x = x0 + i * (SLOT + GAP);
            boolean active = i == p.getActiveSlot();

            g.setColor(active ? new Color(70, 56, 24) : new Color(28, 28, 36));
            g.fillRoundRect(x, y0, SLOT, SLOT, 10, 10);
            g.setColor(active ? new Color(255, 214, 92) : new Color(110, 110, 125));
            g.setStroke(new BasicStroke(active ? 2.5f : 1.2f));
            g.drawRoundRect(x, y0, SLOT, SLOT, 10, 10);
            g.setStroke(new BasicStroke(1f));

            switch (i) {
                case 0: drawSword(g, x, y0); break;
                case 1: drawGun(g, x, y0); break;
                default: drawStaff(g, x, y0); break;
            }

            g.setFont(KEY_FONT);
            shadowText(g, String.valueOf(i + 1), x + 5, y0 + 13, active ? new Color(255, 226, 140) : Color.LIGHT_GRAY);
        }
    }

    private void drawSword(Graphics2D g, int x, int y) {
        g.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(190, 200, 215));
        g.drawLine(x + 14, y + 31, x + 35, y + 10);
        g.setStroke(new BasicStroke(1.5f));
        g.setColor(Color.WHITE);
        g.drawLine(x + 16, y + 29, x + 34, y + 11);

        g.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(GOLD);
        g.drawLine(x + 11, y + 26, x + 20, y + 35);
        g.setColor(new Color(110, 70, 30));
        g.drawLine(x + 12, y + 34, x + 8, y + 38);
        g.setStroke(new BasicStroke(1f));
        g.setColor(GOLD);
        g.fillOval(x + 5, y + 36, 5, 5);
    }

    private void drawGun(Graphics2D g, int x, int y) {
        g.setColor(new Color(60, 60, 70));
        g.fillRect(x + 7, y + 17, 30, 9);
        g.setColor(new Color(150, 155, 170));
        g.fillRect(x + 8, y + 18, 28, 4);
        g.setColor(new Color(40, 40, 48));
        g.fillRect(x + 32, y + 19, 8, 4);
        g.setColor(new Color(120, 78, 40));
        int[] gx = {x + 11, x + 20, x + 17, x + 9};
        int[] gy = {y + 26, y + 26, y + 38, y + 38};
        g.fillPolygon(gx, gy, 4);
        g.setColor(new Color(60, 60, 70));
        g.drawArc(x + 19, y + 24, 9, 8, 180, 180);
        g.fillRect(x + 8, y + 14, 3, 3);
    }

    private void drawStaff(Graphics2D g, int x, int y) {
        g.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(130, 88, 48));
        g.drawLine(x + 12, y + 37, x + 30, y + 15);
        g.setStroke(new BasicStroke(1f));

        float cx = x + 32, cy = y + 12;
        g.setPaint(new RadialGradientPaint(cx, cy, 11f, new float[]{0f, 1f},
                new Color[]{new Color(200, 140, 255, 200), new Color(200, 140, 255, 0)}));
        g.fill(new Ellipse2D.Float(cx - 11, cy - 11, 22, 22));
        g.setColor(new Color(150, 80, 230));
        g.fill(new Ellipse2D.Float(cx - 6, cy - 6, 12, 12));
        g.setColor(new Color(240, 220, 255));
        g.fill(new Ellipse2D.Float(cx - 3, cy - 4, 4, 4));
    }

    private void shadowText(Graphics2D g, String s, int x, int y, Color c) {
        g.setColor(new Color(0, 0, 0, 200));
        g.drawString(s, x + 1, y + 1);
        g.setColor(c);
        g.drawString(s, x, y);
    }
}
