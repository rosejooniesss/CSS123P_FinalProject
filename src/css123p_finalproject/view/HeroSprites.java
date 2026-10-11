package css123p_finalproject.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Pixel-art hero (silver hair, dark armor, red cape) generated in code, so no
 * image file is needed. Each frame is drawn on a 32x32 pixel grid with an
 * automatic dark outline; GamePanel scales it 2x with nearest-neighbour to the
 * 64x64 player hitbox, which gives the chunky retro look.
 *
 * Frames exist for every combination of:
 *   weapon (0 sword, 1 gun, 2 staff) x attacking (no/yes) x direction x walk frame
 */
public class HeroSprites {

    public static final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;
    /** Walk animation order: stand, step, stand, other step. */
    public static final int[] WALK_CYCLE = {0, 1, 0, 3};

    private static final int S = 32;

    private static final Color OUTLINE = new Color(24, 18, 32);
    private static final Color SKIN = new Color(240, 200, 170);
    private static final Color SKIN_SHADE = new Color(206, 160, 134);
    private static final Color HAIR = new Color(206, 206, 232);
    private static final Color HAIR_LIGHT = new Color(240, 240, 255);
    private static final Color HAIR_SHADE = new Color(148, 148, 188);
    private static final Color EYE = new Color(40, 34, 70);
    private static final Color ARMOR = new Color(62, 66, 90);
    private static final Color ARMOR_LIGHT = new Color(104, 112, 144);
    private static final Color ARMOR_DARK = new Color(40, 42, 62);
    private static final Color GOLD = new Color(226, 180, 64);
    private static final Color CAPE = new Color(164, 32, 44);
    private static final Color CAPE_DARK = new Color(110, 20, 34);
    private static final Color BOOT = new Color(58, 40, 38);
    private static final Color STEEL = new Color(212, 222, 238);
    private static final Color STEEL_SHADE = new Color(140, 152, 180);
    private static final Color WOOD = new Color(128, 84, 46);
    private static final Color GUN = new Color(118, 124, 142);
    private static final Color GUN_DARK = new Color(58, 60, 74);
    private static final Color ORB = new Color(176, 96, 244);
    private static final Color ORB_LIGHT = new Color(244, 220, 255);

    private final BufferedImage[][][][] frames = new BufferedImage[3][2][4][4];
    private final BufferedImage portrait;

    public HeroSprites() {
        for (int w = 0; w < 3; w++) {
            for (int a = 0; a < 2; a++) {
                for (int f = 0; f < 4; f++) {
                    boolean attack = a == 1;
                    frames[w][a][DOWN][f] = draw(w, attack, DOWN, f);
                    frames[w][a][UP][f] = draw(w, attack, UP, f);
                    BufferedImage right = draw(w, attack, RIGHT, f);
                    frames[w][a][RIGHT][f] = right;
                    frames[w][a][LEFT][f] = mirror(right);
                }
            }
        }
        // head-and-shoulders crop of the front-facing hero for the HUD
        BufferedImage front = frames[0][0][DOWN][0];
        portrait = front.getSubimage(8, 1, 16, 16);
    }

    public BufferedImage get(int weapon, boolean attacking, int dir, int frame) {
        return frames[weapon][attacking ? 1 : 0][dir][frame];
    }

    public BufferedImage portrait() { return portrait; }

    // ------------------------------------------------------------------ drawing

    private BufferedImage draw(int weapon, boolean attack, int dir, int frame) {
        BufferedImage img = new BufferedImage(S, S, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        boolean behind = dir == UP;       // weapon is hidden behind the body when walking away
        int hx = dir == RIGHT ? 19 : 22;  // hand position
        int hy = dir == RIGHT ? 20 : 21;

        if (behind) drawWeapon(g, weapon, attack, dir, hx, hy);
        if (dir == DOWN) drawFront(g, frame);
        else if (dir == UP) drawBack(g, frame);
        else drawSide(g, frame);
        if (!behind) drawWeapon(g, weapon, attack, dir, hx, hy);

        g.dispose();
        return outline(img);
    }

    private void drawFront(Graphics2D g, int f) {
        legsFront(g, f);
        int swL = f == 1 ? -1 : f == 3 ? 1 : 0;
        int swR = -swL;

        // arms
        r(g, ARMOR, 9, 15 + swL, 2, 6);
        r(g, SKIN, 9, 21 + swL, 2, 2);
        r(g, ARMOR, 21, 15 + swR, 2, 6);
        r(g, SKIN, 21, 21 + swR, 2, 2);

        // torso
        r(g, ARMOR, 11, 12, 10, 9);
        r(g, ARMOR_LIGHT, 13, 13, 6, 5);
        r(g, ARMOR_DARK, 15, 13, 2, 5);
        r(g, GOLD, 15, 14, 2, 2);
        r(g, GOLD, 11, 19, 10, 2);
        r(g, ARMOR_DARK, 12, 21, 8, 1);

        // pauldrons
        r(g, ARMOR_LIGHT, 8, 12, 4, 4);
        r(g, ARMOR_LIGHT, 20, 12, 4, 4);
        r(g, GOLD, 8, 15, 4, 1);
        r(g, GOLD, 20, 15, 4, 1);

        // head
        r(g, SKIN, 13, 7, 6, 5);
        r(g, SKIN_SHADE, 13, 11, 6, 1);
        r(g, HAIR, 11, 3, 10, 4);
        r(g, HAIR_LIGHT, 13, 4, 5, 1);
        r(g, HAIR, 11, 7, 2, 5);
        r(g, HAIR, 19, 7, 2, 5);
        r(g, HAIR_SHADE, 11, 10, 2, 2);
        r(g, HAIR_SHADE, 19, 10, 2, 2);
        r(g, HAIR, 13, 6, 6, 1);
        r(g, HAIR, 13, 7, 1, 1);
        r(g, HAIR, 16, 7, 2, 1);
        r(g, EYE, 14, 9, 1, 2);
        r(g, EYE, 17, 9, 1, 2);
    }

    private void drawBack(Graphics2D g, int f) {
        legsFront(g, f);
        int swL = f == 1 ? -1 : f == 3 ? 1 : 0;
        int swR = -swL;

        r(g, ARMOR, 9, 15 + swL, 2, 6);
        r(g, SKIN, 9, 21 + swL, 2, 2);
        r(g, ARMOR, 21, 15 + swR, 2, 6);
        r(g, SKIN, 21, 21 + swR, 2, 2);

        // cape
        r(g, CAPE, 10, 12, 12, 13);
        r(g, CAPE_DARK, 10, 22, 12, 3);
        r(g, CAPE_DARK, 15, 13, 2, 10);
        r(g, GOLD, 10, 12, 12, 1);

        r(g, ARMOR_LIGHT, 8, 12, 4, 4);
        r(g, ARMOR_LIGHT, 20, 12, 4, 4);

        // head (all hair)
        r(g, HAIR, 11, 3, 10, 9);
        r(g, HAIR_LIGHT, 13, 4, 5, 1);
        r(g, HAIR_SHADE, 12, 9, 8, 3);
        r(g, HAIR_SHADE, 15, 6, 2, 3);
    }

    private void drawSide(Graphics2D g, int f) {
        // cape trailing behind (facing right, so behind = left)
        r(g, CAPE, 9, 13, 4, 11);
        r(g, CAPE_DARK, 9, 21, 4, 3);

        // far leg then near leg
        int fwd = f == 1 ? 2 : f == 3 ? -2 : 0;
        r(g, ARMOR_DARK, 14 - fwd, 21, 3, 5);
        r(g, BOOT, 14 - fwd, 26, 4, 3);
        r(g, ARMOR, 14 + fwd, 21, 3, 5);
        r(g, BOOT, 14 + fwd, 26, 5, 3);

        // torso
        r(g, ARMOR, 12, 12, 8, 9);
        r(g, ARMOR_LIGHT, 15, 13, 4, 5);
        r(g, GOLD, 12, 19, 8, 2);

        // near arm reaching forward to the hand
        r(g, ARMOR, 14, 14, 3, 4);
        r(g, ARMOR, 16, 17, 3, 3);
        r(g, SKIN, 18, 19, 2, 2);
        r(g, ARMOR_LIGHT, 13, 12, 4, 4);
        r(g, GOLD, 13, 15, 4, 1);

        // head
        r(g, HAIR, 11, 3, 9, 4);
        r(g, HAIR_LIGHT, 13, 4, 5, 1);
        r(g, HAIR, 11, 6, 4, 6);
        r(g, HAIR_SHADE, 11, 10, 4, 2);
        r(g, SKIN, 15, 7, 5, 5);
        r(g, SKIN, 20, 9, 1, 1);
        r(g, SKIN_SHADE, 15, 11, 5, 1);
        r(g, HAIR, 15, 6, 5, 1);
        r(g, HAIR, 18, 7, 1, 1);
        r(g, EYE, 18, 9, 1, 2);
    }

    private void legsFront(Graphics2D g, int f) {
        for (int side = 0; side < 2; side++) {
            int sx = side == 0 ? 12 : 17;
            boolean lifted = (f == 1 && side == 0) || (f == 3 && side == 1);
            int lift = lifted ? 2 : 0;
            r(g, ARMOR, sx, 21, 4, 5 - lift);
            r(g, ARMOR_DARK, sx, 21, 1, 5 - lift);
            r(g, BOOT, sx, 26 - lift, 4, 3);
        }
    }

    // ------------------------------------------------------------------ weapons

    private void drawWeapon(Graphics2D g, int weapon, boolean attack, int dir, int hx, int hy) {
        int fx = dir == RIGHT ? 1 : 0;                 // facing vector
        int fy = dir == DOWN ? 1 : dir == UP ? -1 : 0;

        switch (weapon) {
            case 0: sword(g, attack, fx, fy, hx, hy); break;
            case 1: gun(g, attack, fx, fy, hx, hy); break;
            default: staff(g, attack, fx, fy, hx, hy); break;
        }
    }

    private void sword(Graphics2D g, boolean attack, int fx, int fy, int hx, int hy) {
        int tx, ty;
        if (attack) {
            tx = hx + fx * 11;
            ty = hy + fy * 10 - (fx != 0 ? 3 : 0);
            if (fy != 0) tx = hx + 1;
        } else {
            tx = hx + 1;
            ty = hy + 7;
        }
        line(g, STEEL_SHADE, hx + 1, hy, tx + 1, ty);
        line(g, STEEL, hx, hy, tx, ty);

        // grip + crossguard
        double len = Math.max(1, Math.hypot(tx - hx, ty - hy));
        double ux = (tx - hx) / len, uy = (ty - hy) / len;
        int gx = (int) Math.round(hx + ux * 2), gy = (int) Math.round(hy + uy * 2);
        int px = (int) Math.round(-uy * 2), py = (int) Math.round(ux * 2);
        line(g, GOLD, gx - px, gy - py, gx + px, gy + py);
        line(g, BOOT, hx, hy, (int) Math.round(hx - ux * 2), (int) Math.round(hy - uy * 2));
    }

    private void gun(Graphics2D g, boolean attack, int fx, int fy, int hx, int hy) {
        int tx, ty;
        if (attack) {
            tx = hx + fx * 7;
            ty = hy + fy * 7 - (fx != 0 ? 1 : 0);
        } else {
            tx = hx;
            ty = hy + 5;
        }
        // grip
        r(g, WOOD, hx - 1, hy - 1, 2, 3);
        line(g, GUN_DARK, hx + (fy != 0 ? 1 : 0), hy + (fy == 0 ? 1 : 0), tx + (fy != 0 ? 1 : 0), ty + (fy == 0 ? 1 : 0));
        line(g, GUN, hx, hy, tx, ty);
        if (attack) {
            r(g, new Color(255, 226, 120), tx + fx * 1 - (fx == 0 ? 1 : 0), ty + fy * 1 - (fy == 0 ? 1 : 0), 2, 2);
        }
    }

    private void staff(Graphics2D g, boolean attack, int fx, int fy, int hx, int hy) {
        int bx, by, tx, ty;
        if (attack) {
            bx = hx - fx * 3;
            by = hy - fy * 3 + (fx != 0 ? 4 : 0);
            tx = hx + fx * 10;
            ty = hy + fy * 8 - (fx != 0 ? 5 : 0);
            if (fy != 0) { bx = hx; tx = hx + 1; }
        } else {
            bx = hx;
            by = hy + 7;
            tx = hx + 3;
            ty = hy - 13;
        }
        line(g, WOOD, bx, by, tx, ty);
        line(g, new Color(86, 56, 30), bx + 1, by, tx + 1, ty);

        int o = attack ? 2 : 1;
        r(g, ORB, tx - o, ty - o, o * 2 + 1, o * 2 + 1);
        r(g, ORB_LIGHT, tx - o + 1, ty - o + 1, 2, 2);
    }

    // ------------------------------------------------------------------ helpers

    private static void r(Graphics2D g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        g.fillRect(x, y, w, h);
    }

    /** 1px Bresenham line. */
    private static void line(Graphics2D g, Color c, int x0, int y0, int x1, int y1) {
        g.setColor(c);
        int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;
        while (true) {
            g.fillRect(x0, y0, 1, 1);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) { err += dy; x0 += sx; }
            if (e2 <= dx) { err += dx; y0 += sy; }
        }
    }

    private static BufferedImage outline(BufferedImage src) {
        BufferedImage out = new BufferedImage(S, S, BufferedImage.TYPE_INT_ARGB);
        int[] d = {1, 0, -1, 0, 0, 1, 0, -1};
        for (int y = 0; y < S; y++) {
            for (int x = 0; x < S; x++) {
                int argb = src.getRGB(x, y);
                if ((argb >>> 24) != 0) {
                    out.setRGB(x, y, argb);
                    continue;
                }
                for (int i = 0; i < 8; i += 2) {
                    int nx = x + d[i], ny = y + d[i + 1];
                    if (nx >= 0 && ny >= 0 && nx < S && ny < S && (src.getRGB(nx, ny) >>> 24) != 0) {
                        out.setRGB(x, y, OUTLINE.getRGB());
                        break;
                    }
                }
            }
        }
        return out;
    }

    private static BufferedImage mirror(BufferedImage src) {
        BufferedImage out = new BufferedImage(S, S, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < S; y++)
            for (int x = 0; x < S; x++)
                out.setRGB(S - 1 - x, y, src.getRGB(x, y));
        return out;
    }
}
