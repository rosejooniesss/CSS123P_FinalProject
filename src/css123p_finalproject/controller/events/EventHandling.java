package css123p_finalproject.controller.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EventHandling extends KeyAdapter {
    public boolean up, down, left, right;
    public int requestedSlot = -1;
    
    // Direction tracking for dashing
    public int lastDirX = 1; // Default facing right
    public int lastDirY = 0;
    public boolean dashRequested = false;

    private void updateDirection() {
        int dx = 0;
        int dy = 0;
        
        if (left) dx = -1;
        else if (right) dx = 1;
        
        if (up) dy = -1;
        else if (down) dy = 1;
        
        // Only update facing direction if a movement key is actively held down
        if (dx != 0 || dy != 0) {
            lastDirX = dx;
            lastDirY = dy;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = true;
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = true;

        if (key == KeyEvent.VK_1) requestedSlot = 0;
        if (key == KeyEvent.VK_2) requestedSlot = 1;
        if (key == KeyEvent.VK_3) requestedSlot = 2;

        updateDirection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = false;
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = false;

        updateDirection();
    }
}