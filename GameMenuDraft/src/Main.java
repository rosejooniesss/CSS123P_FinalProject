package gamemenudraft;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::initializeApplicationFrame);
    }

    private static void initializeApplicationFrame() {
        Controller controller = new Controller();
        controller.start();
    }
}
