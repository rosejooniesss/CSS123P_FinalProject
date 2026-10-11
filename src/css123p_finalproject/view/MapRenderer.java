package css123p_finalproject.view;

import css123p_finalproject.model.MapLayout;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws the castle-hall map (stone cobble floor, red carpets, centre slab,
 * pillars with gold crosses, banners, balustrade) entirely with Java2D so
 * no tileset files are needed.
 *
 * The static scenery is rendered once per floor theme and cached. Torch glow
 * and the vignette are drawn on top every frame so the flames can flicker.
 *
 *   drawBackground(...)  ->  before entities
 *   drawLighting(...)    ->  after entities, before the HUD
 */
public class MapRenderer {

    private static final int W = MapLayout.WIDTH;
    private static final int H = MapLayout.HEIGHT;
    private static final int WALL_BOTTOM = MapLayout.WALL_BOTTOM;
    private static final int FLOOR_BOTTOM = MapLayout.FLOOR_BOTTOM;
    private static final int SIDE = MapLayout.SIDE;

    private static final int[] PILLARS = {128, 288, 512, 672};
    private static final Rectangle LEFT_CARPET = new Rectangle(64, 192, 288, 192);
    private static final Rectangle RIGHT_CARPET = new Rectangle(448, 192, 288, 192);
    private static final Rectangle SLAB = new Rectangle(352, 192, 96, 192);

    /** One look per floor. */
    public enum Theme {
        CASTLE_HALL("Floor 1 - Goblin Hall",
                new Color(116, 116, 124), new Color(64, 64, 72),
                new Color(178, 28, 40), new Color(226, 128, 24), new Color(255, 214, 92),
                new Color(150, 150, 192), new Color(255, 170, 60), new Color(176, 30, 42), 150),
        CRYPT("Floor 2 - Skeleton Crypt",
                new Color(86, 104, 118), new Color(46, 58, 70),
                new Color(26, 84, 116), new Color(120, 176, 200), new Color(220, 240, 250),
                new Color(122, 152, 176), new Color(110, 255, 190), new Color(24, 92, 104), 175),
        THRONE("Floor 3 - Boss Sanctum",
                new Color(66, 58, 76), new Color(30, 26, 38),
                new Color(96, 14, 46), new Color(130, 60, 190), new Color(214, 160, 255),
                new Color(58, 48, 84), new Color(206, 110, 255), new Color(78, 12, 64), 200);

        final String title;
        final Color stone, mortar, carpet, trim, trimLight, slab, flame, banner;
        final int darkness;

        Theme(String title, Color stone, Color mortar, Color carpet, Color trim, Color trimLight,
              Color slab, Color flame, Color banner, int darkness) {
            this.title = title;
            this.stone = stone;
            this.mortar = mortar;
            this.carpet = carpet;
            this.trim = trim;
            this.trimLight = trimLight;
            this.slab = slab;
            this.flame = flame;
            this.banner = banner;
            this.darkness = darkness;
        }

        static Theme forFloor(int floor) {
            if (floor == 2) return CRYPT;
            if (floor >= 3) return THRONE;
            return CASTLE_HALL;
        }
    }

    private final BufferedImage[] backgrounds = new BufferedImage[Theme.values().length];
    private final BufferedImage[] vignettes = new BufferedImage[Theme.values().length];
    private final List<Point> flamePoints = buildFlamePoints();

    // ------------------------------------------------------------------ public API

    public String getFloorTitle(int floor) {
        return Theme.forFloor(floor).title;
    }

    public void drawBackground(Graphics2D g, int floor) {
        g.drawImage(background(Theme.forFloor(floor)), 0, 0, null);
    }

    /** Vignette + flickering torch glow. Call after drawing entities. */
    public void drawLighting(Graphics2D g, int floor, long timeMs) {
        Theme t = Theme.forFloor(floor);
        Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(vignette(t), 0, 0, null);

        double sec = timeMs / 1000.0;
        for (int i = 0; i < flamePoints.size(); i++) {
            Point p = flamePoints.get(i);
            double flicker = 0.85 + 0.10 * Math.sin(sec * 9 + i * 1.7) + 0.05 * Math.sin(sec * 23 + i * 3.1);

            float r = (float) (70 * flicker);
            g.setPaint(new RadialGradientPaint(p.x, p.y, r,
                    new float[]{0f, 1f},
                    new Color[]{alpha(t.flame, 78), alpha(t.flame, 0)}));
            g.fill(new Ellipse2D.Float(p.x - r, p.y - r, r * 2, r * 2));

            // the flame itself
            float fh = (float) (9 * flicker);
            g.setColor(alpha(t.flame, 235));
            g.fill(new Ellipse2D.Float(p.x - 2.5f, p.y - fh, 5, fh + 1));
            g.setColor(new Color(255, 250, 200, 230));
            g.fill(new Ellipse2D.Float(p.x - 1.2f, p.y - fh * 0.55f, 2.4f, fh * 0.55f + 1));
        }

        if (oldAA != null) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }

    // ------------------------------------------------------------------ caching

    private BufferedImage background(Theme t) {
        if (backgrounds[t.ordinal()] == null) backgrounds[t.ordinal()] = buildBackground(t);
        return backgrounds[t.ordinal()];
    }

    private BufferedImage vignette(Theme t) {
        if (vignettes[t.ordinal()] == null) {
            BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setPaint(new RadialGradientPaint(W / 2f, H / 2f + 10, 560f,
                    new float[]{0f, 0.55f, 1f},
                    new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), new Color(0, 0, 0, t.darkness)}));
            g.fillRect(0, 0, W, H);
            g.dispose();
            vignettes[t.ordinal()] = img;
        }
        return vignettes[t.ordinal()];
    }

    private static List<Point> buildFlamePoints() {
        List<Point> pts = new ArrayList<>();
        for (int cx : PILLARS) {            // the two candles beside each pillar cross
            pts.add(new Point(cx - 18, 66));
            pts.add(new Point(cx + 18, 66));
        }
        for (int cx : new int[]{64, 736}) { // the two candelabras
            pts.add(new Point(cx - 12, 92));
            pts.add(new Point(cx, 85));
            pts.add(new Point(cx + 12, 92));
        }
        return pts;
    }

    // ------------------------------------------------------------------ scene building

    private BufferedImage buildBackground(Theme t) {
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Random rnd = new Random(7L + t.ordinal());

        paintFloor(g, t, rnd);
        paintCarpet(g, t, LEFT_CARPET, rnd);
        paintCarpet(g, t, RIGHT_CARPET, rnd);
        paintSlab(g, t, rnd);
        paintBackWall(g, t, rnd);

        for (int bx : new int[]{208, 340, 460, 592}) paintBanner(g, t, bx);
        if (t == Theme.THRONE) {
            paintThrone(g, t);
        } else {
            paintPortrait(g, t, 382, 0);
            paintPortrait(g, t, 418, 1);
        }

        for (int cx : PILLARS) paintPillar(g, t, cx, rnd);
        paintCandelabra(g, t, 64);
        paintCandelabra(g, t, 736);

        paintSideWalls(g, t, rnd);
        paintBalustrade(g, t);

        g.dispose();
        return img;
    }

    private void paintFloor(Graphics2D g, Theme t, Random rnd) {
        g.setColor(t.mortar);
        g.fillRect(0, 0, W, H);

        for (int y = WALL_BOTTOM - 16; y < FLOOR_BOTTOM; y += 16) {
            int off = ((y / 16) % 2) * 8;
            for (int x = -16 + off; x < W; x += 16) {
                int v = rnd.nextInt(29) - 14;
                g.setColor(new Color(
                        clamp(t.stone.getRed() + v),
                        clamp(t.stone.getGreen() + v),
                        clamp(t.stone.getBlue() + v + rnd.nextInt(7) - 3)));
                g.fill(new RoundRectangle2D.Float(x + 1, y + 1, 14, 14, 6, 6));
                g.setColor(new Color(255, 255, 255, 26));
                g.drawLine(x + 4, y + 2, x + 11, y + 2);
                g.setColor(new Color(0, 0, 0, 40));
                g.drawLine(x + 4, y + 14, x + 12, y + 14);
            }
        }

        // darken towards the balcony edge
        g.setPaint(new GradientPaint(0, FLOOR_BOTTOM - 24, new Color(0, 0, 0, 0),
                0, FLOOR_BOTTOM, new Color(0, 0, 0, 90)));
        g.fillRect(0, FLOOR_BOTTOM - 24, W, 24);
    }

    private void paintCarpet(Graphics2D g, Theme t, Rectangle r, Random rnd) {
        g.setColor(new Color(0, 0, 0, 70));
        g.fillRect(r.x + 2, r.y + 5, r.width, r.height);

        g.setColor(t.trim);
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(t.trimLight);
        g.fillRect(r.x + 3, r.y + 3, r.width - 6, r.height - 6);
        g.setColor(t.carpet);
        g.fillRect(r.x + 5, r.y + 5, r.width - 10, r.height - 10);

        // woven texture
        for (int x = r.x + 5; x < r.x + r.width - 5; x += 4) {
            g.setColor(new Color(0, 0, 0, 18));
            g.drawLine(x, r.y + 5, x, r.y + r.height - 6);
        }
        for (int y = r.y + 5; y < r.y + r.height - 5; y += 4) {
            g.setColor(new Color(255, 255, 255, 12));
            g.drawLine(r.x + 5, y, r.x + r.width - 6, y);
        }
        int specks = (r.width * r.height) / 40;
        for (int i = 0; i < specks; i++) {
            int px = r.x + 5 + rnd.nextInt(r.width - 10);
            int py = r.y + 5 + rnd.nextInt(r.height - 10);
            g.setColor(rnd.nextBoolean() ? new Color(0, 0, 0, 40) : new Color(255, 255, 255, 22));
            g.fillRect(px, py, 2, 2);
        }

        // inner border lines + centre medallion
        g.setColor(shade(t.carpet, 0.7));
        g.drawRect(r.x + 16, r.y + 16, r.width - 32, r.height - 32);
        g.setColor(alpha(t.trim, 170));
        g.drawRect(r.x + 19, r.y + 19, r.width - 38, r.height - 38);

        int cx = r.x + r.width / 2, cy = r.y + r.height / 2;
        Path2D.Float d = new Path2D.Float();
        d.moveTo(cx, cy - 20);
        d.lineTo(cx + 34, cy);
        d.lineTo(cx, cy + 20);
        d.lineTo(cx - 34, cy);
        d.closePath();
        g.setColor(alpha(t.trim, 190));
        g.draw(d);
        g.setColor(alpha(t.trimLight, 120));
        g.fillOval(cx - 5, cy - 5, 10, 10);
    }

    private void paintSlab(Graphics2D g, Theme t, Random rnd) {
        Rectangle s = SLAB;
        g.setColor(t.slab);
        g.fill(s);
        for (int y = s.y; y < s.y + s.height; y += 32) {
            for (int x = s.x; x < s.x + s.width; x += 32) {
                int v = rnd.nextInt(17) - 8;
                g.setColor(new Color(clamp(t.slab.getRed() + v), clamp(t.slab.getGreen() + v), clamp(t.slab.getBlue() + v)));
                g.fillRect(x + 1, y + 1, 30, 30);
                g.setColor(new Color(255, 255, 255, 34));
                g.drawLine(x + 1, y + 1, x + 30, y + 1);
                g.drawLine(x + 1, y + 1, x + 1, y + 30);
                g.setColor(new Color(0, 0, 0, 50));
                g.drawLine(x + 1, y + 30, x + 30, y + 30);
                g.drawLine(x + 30, y + 1, x + 30, y + 30);
            }
        }
        g.setColor(shade(t.slab, 0.55));
        g.drawRect(s.x, s.y, s.width - 1, s.height - 1);

        if (t == Theme.THRONE) paintRune(g, t, s.x + s.width / 2, s.y + s.height / 2);
    }

    private void paintRune(Graphics2D g, Theme t, int cx, int cy) {
        g.setStroke(new BasicStroke(2f));
        g.setColor(alpha(t.flame, 170));
        g.drawOval(cx - 38, cy - 38, 76, 76);
        g.setColor(alpha(t.flame, 110));
        g.drawOval(cx - 28, cy - 28, 56, 56);
        double[] px = new double[5], py = new double[5];
        for (int i = 0; i < 5; i++) {
            double a = -Math.PI / 2 + i * 2 * Math.PI / 5;
            px[i] = cx + Math.cos(a) * 36;
            py[i] = cy + Math.sin(a) * 36;
        }
        g.setColor(alpha(t.flame, 160));
        for (int i = 0; i < 5; i++) {
            int j = (i + 2) % 5;
            g.drawLine((int) px[i], (int) py[i], (int) px[j], (int) py[j]);
        }
        g.setStroke(new BasicStroke(1f));
    }

    private void paintBackWall(Graphics2D g, Theme t, Random rnd) {
        Color base = shade(t.stone, 0.42);
        g.setColor(shade(t.mortar, 0.7));
        g.fillRect(0, 0, W, WALL_BOTTOM);

        for (int y = 0; y < WALL_BOTTOM; y += 16) {
            int off = ((y / 16) % 2) * 16;
            for (int x = -32 + off; x < W; x += 32) {
                int v = rnd.nextInt(15) - 7;
                g.setColor(new Color(clamp(base.getRed() + v), clamp(base.getGreen() + v), clamp(base.getBlue() + v)));
                g.fillRect(x + 1, y + 1, 30, 14);
                g.setColor(new Color(255, 255, 255, 14));
                g.drawLine(x + 1, y + 1, x + 30, y + 1);
            }
        }

        // dark ceiling
        g.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 235), 0, 44, new Color(0, 0, 0, 0)));
        g.fillRect(0, 0, W, 44);

        // ledge at the foot of the wall
        g.setColor(shade(t.stone, 0.9));
        g.fillRect(0, WALL_BOTTOM - 8, W, 8);
        g.setColor(shade(t.stone, 1.25));
        g.drawLine(0, WALL_BOTTOM - 8, W, WALL_BOTTOM - 8);
        g.setColor(new Color(0, 0, 0, 90));
        g.drawLine(0, WALL_BOTTOM, W, WALL_BOTTOM);

        // wall shadow on the floor
        g.setPaint(new GradientPaint(0, WALL_BOTTOM, new Color(0, 0, 0, 120),
                0, WALL_BOTTOM + 26, new Color(0, 0, 0, 0)));
        g.fillRect(0, WALL_BOTTOM, W, 26);
    }

    private void paintBanner(Graphics2D g, Theme t, int cx) {
        int top = 30, bot = 100, hw = 14;
        Path2D.Float p = new Path2D.Float();
        p.moveTo(cx - hw, top);
        p.lineTo(cx + hw, top);
        p.lineTo(cx + hw, bot);
        p.lineTo(cx, bot - 10);
        p.lineTo(cx - hw, bot);
        p.closePath();

        g.setColor(t.banner);
        g.fill(p);
        g.setColor(shade(t.banner, 0.65));
        g.fillRect(cx + hw - 5, top, 5, bot - top - 6);
        g.setColor(t.trimLight);
        g.setStroke(new BasicStroke(1.5f));
        g.draw(p);
        g.setStroke(new BasicStroke(1f));

        g.setColor(shade(t.trim, 0.8));
        g.fillRect(cx - hw - 3, top - 4, 2 * hw + 6, 4);

        Path2D.Float d = new Path2D.Float();
        d.moveTo(cx, top + 14);
        d.lineTo(cx + 7, top + 24);
        d.lineTo(cx, top + 34);
        d.lineTo(cx - 7, top + 24);
        d.closePath();
        g.setColor(t.trimLight);
        g.fill(d);
    }

    private void paintPortrait(Graphics2D g, Theme t, int cx, int variant) {
        int w = 32, h = 44, x = cx - w / 2, y = 34;
        g.setColor(shade(t.trim, 0.9));
        g.fillRect(x - 3, y - 3, w + 6, h + 6);
        g.setColor(new Color(40, 28, 22));
        g.fillRect(x, y, w, h);

        Shape old = g.getClip();
        g.clipRect(x, y, w, h);
        g.setColor(variant == 0 ? new Color(122, 40, 40) : new Color(50, 70, 112));
        g.fillOval(x + 2, y + h - 22, w - 4, 30);
        g.setColor(new Color(214, 176, 140));
        g.fillOval(x + 10, y + 8, 12, 15);
        g.setColor(new Color(60, 40, 30));
        g.fillArc(x + 9, y + 6, 14, 12, 0, 180);
        g.setClip(old);

        g.setColor(t.trimLight);
        g.drawRect(x - 3, y - 3, w + 5, h + 5);
    }

    private void paintThrone(Graphics2D g, Theme t) {
        int cx = 400;
        Path2D.Float back = new Path2D.Float();
        back.moveTo(cx - 26, 114);
        back.lineTo(cx - 26, 52);
        back.lineTo(cx - 14, 34);
        back.lineTo(cx, 24);
        back.lineTo(cx + 14, 34);
        back.lineTo(cx + 26, 52);
        back.lineTo(cx + 26, 114);
        back.closePath();

        g.setColor(shade(t.stone, 0.5));
        g.fill(back);
        g.setColor(t.carpet);
        g.fillRect(cx - 18, 50, 36, 48);
        g.setColor(t.trimLight);
        g.setStroke(new BasicStroke(2f));
        g.draw(back);
        g.setStroke(new BasicStroke(1f));

        g.setColor(shade(t.stone, 0.6));
        g.fillRect(cx - 34, 82, 8, 34);
        g.fillRect(cx + 26, 82, 8, 34);
        g.fillRect(cx - 30, 98, 60, 16);
        g.setColor(t.carpet);
        g.fillRect(cx - 22, 92, 44, 10);
        g.setColor(t.flame);
        g.fillOval(cx - 4, 36, 8, 11);
    }

    private void paintPillar(Graphics2D g, Theme t, int cx, Random rnd) {
        int x0 = cx - 32, top = -4, bot = 148;

        // floor shadow
        g.setColor(new Color(0, 0, 0, 85));
        g.fill(new Ellipse2D.Float(cx - 30, bot - 6, 92, 22));

        // cylinder body
        g.setPaint(new LinearGradientPaint(x0, 0, x0 + 64, 0,
                new float[]{0f, 0.3f, 0.7f, 1f},
                new Color[]{shade(t.stone, 0.38), shade(t.stone, 1.05), shade(t.stone, 0.72), shade(t.stone, 0.30)}));
        g.fillRect(x0, top, 64, bot - top);
        g.setColor(shade(t.stone, 0.45));
        g.fill(new Ellipse2D.Float(x0 - 2, bot - 9, 68, 18));

        // curved block rings + staggered joints
        g.setColor(new Color(0, 0, 0, 70));
        g.setStroke(new BasicStroke(1.2f));
        int k = 0;
        for (int y = 14; y < bot - 8; y += 20, k++) {
            g.draw(new QuadCurve2D.Float(x0, y, cx, y + 7, x0 + 64, y));
            int jx = x0 + 14 + ((k * 23) % 36);
            g.drawLine(jx, y + 3, jx, y + 20);
        }
        g.setStroke(new BasicStroke(1f));

        for (int i = 0; i < 40; i++) {
            int px = x0 + 3 + rnd.nextInt(58);
            int py = rnd.nextInt(bot - 12);
            g.setColor(rnd.nextBoolean() ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 30));
            g.fillRect(px, py, 2, 2);
        }

        // gold cross
        Color gold = new Color(218, 172, 62);
        g.setColor(new Color(60, 40, 10));
        g.fillRect(cx - 4, 47, 8, 38);
        g.fillRect(cx - 11, 59, 22, 8);
        g.setColor(gold);
        g.fillRect(cx - 3, 48, 6, 36);
        g.fillRect(cx - 10, 60, 20, 6);
        g.setColor(new Color(255, 240, 170));
        g.fillRect(cx - 1, 49, 2, 34);

        // candles beside the cross
        for (int side : new int[]{-1, 1}) {
            int sx = cx + side * 18;
            g.setColor(new Color(60, 40, 10));
            g.fillRect(sx - 3, 70, 6, 10);
            g.setColor(new Color(236, 228, 200));
            g.fillRect(sx - 2, 68, 4, 8);
        }
    }

    private void paintCandelabra(Graphics2D g, Theme t, int cx) {
        Color iron = new Color(32, 30, 36);
        g.setColor(new Color(0, 0, 0, 90));
        g.fill(new Ellipse2D.Float(cx - 14, 136, 32, 8));

        g.setColor(iron);
        g.setStroke(new BasicStroke(3f));
        g.drawLine(cx, 140, cx, 104);
        g.drawLine(cx - 10, 142, cx, 134);
        g.drawLine(cx + 10, 142, cx, 134);
        g.setStroke(new BasicStroke(2f));
        g.draw(new QuadCurve2D.Float(cx - 12, 98, cx, 116, cx + 12, 98));
        g.setStroke(new BasicStroke(1f));

        g.setColor(new Color(236, 228, 200));
        g.fillRect(cx - 14, 94, 4, 8);
        g.fillRect(cx - 2, 87, 4, 8);
        g.fillRect(cx + 10, 94, 4, 8);
    }

    private void paintSideWalls(Graphics2D g, Theme t, Random rnd) {
        Color wall = shade(t.stone, 0.5);
        for (int side = 0; side < 2; side++) {
            int x0 = side == 0 ? 0 : W - SIDE;
            int top = WALL_BOTTOM - 8;

            g.setColor(wall);
            g.fillRect(x0, top, SIDE, FLOOR_BOTTOM - top);

            g.setColor(shade(t.mortar, 0.8));
            for (int y = WALL_BOTTOM; y < FLOOR_BOTTOM; y += 16) {
                g.drawLine(x0, y, x0 + SIDE, y);
                int jx = x0 + (((y / 16) % 2 == 0) ? 10 : 22);
                g.drawLine(jx, y, jx, y + 16);
            }

            // staircase leading up to the back corners
            for (int i = 0; i < 6; i++) {
                int y = WALL_BOTTOM + 8 + i * 22;
                g.setColor(shade(t.stone, 0.62 + 0.06 * i));
                g.fillRect(x0, y, SIDE, 22);
                g.setColor(shade(t.stone, 1.3));
                g.drawLine(x0, y, x0 + SIDE, y);
                g.setColor(new Color(0, 0, 0, 90));
                g.drawLine(x0, y + 21, x0 + SIDE, y + 21);
            }

            // inner edge
            int ex = side == 0 ? SIDE - 3 : W - SIDE;
            g.setColor(shade(t.stone, 1.2));
            g.fillRect(ex, top, 3, FLOOR_BOTTOM - top);
            g.setColor(new Color(0, 0, 0, 80));
            int sx = side == 0 ? SIDE : W - SIDE - 4;
            g.fillRect(sx, top, 4, FLOOR_BOTTOM - top);
        }
    }

    private void paintBalustrade(Graphics2D g, Theme t) {
        int railY = FLOOR_BOTTOM;                 // 512
        // void below
        g.setColor(new Color(4, 4, 8));
        g.fillRect(0, railY + 32, W, H - railY - 32);

        // support columns under the big posts
        int[] posts = new int[8];
        for (int k = 0; k < 8; k++) posts[k] = 400 + (int) ((k - 3.5) * 96);
        for (int px : posts) {
            g.setPaint(new LinearGradientPaint(px - 9, 0, px + 9, 0,
                    new float[]{0f, 0.4f, 1f},
                    new Color[]{shade(t.stone, 0.3), shade(t.stone, 0.7), shade(t.stone, 0.25)}));
            g.fillRect(px - 9, railY + 34, 18, H - railY - 34);
        }

        // top cap
        g.setPaint(new GradientPaint(0, railY, shade(t.stone, 1.25), 0, railY + 10, shade(t.stone, 0.8)));
        g.fillRect(0, railY, W, 10);
        g.setColor(new Color(0, 0, 0, 90));
        g.drawLine(0, railY + 10, W, railY + 10);

        // balusters
        for (int x = 4; x < W; x += 12) {
            g.setPaint(new LinearGradientPaint(x, 0, x + 5, 0,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{shade(t.stone, 0.6), shade(t.stone, 1.1), shade(t.stone, 0.5)}));
            g.fillRect(x, railY + 10, 5, 14);
        }

        // bottom beam
        g.setColor(shade(t.stone, 0.55));
        g.fillRect(0, railY + 24, W, 8);
        g.setColor(new Color(255, 255, 255, 30));
        g.drawLine(0, railY + 24, W, railY + 24);

        // big posts with orange base plates (like the reference)
        for (int px : posts) {
            g.setPaint(new LinearGradientPaint(px - 6, 0, px + 6, 0,
                    new float[]{0f, 0.4f, 1f},
                    new Color[]{shade(t.stone, 0.35), shade(t.stone, 0.85), shade(t.stone, 0.3)}));
            g.fillRect(px - 6, railY - 10, 12, 42);
            g.setColor(shade(t.stone, 1.3));
            g.fillRect(px - 8, railY - 13, 16, 4);
            g.setColor(t.trim);
            g.fillRect(px - 13, railY + 24, 26, 9);
            g.setColor(t.trimLight);
            g.drawLine(px - 13, railY + 24, px + 12, railY + 24);
            g.setColor(shade(t.trim, 0.55));
            g.drawRect(px - 13, railY + 24, 25, 8);
        }
    }

    // ------------------------------------------------------------------ helpers

    private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }

    private static Color shade(Color c, double f) {
        return new Color(clamp((int) (c.getRed() * f)), clamp((int) (c.getGreen() * f)), clamp((int) (c.getBlue() * f)));
    }

    private static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }
}
