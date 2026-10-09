package gamemenudraft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Receives UI events and forwards them to the Controller ("manages inputs"). */
public class EventHandling implements ActionListener {
    private final Controller controller;
    private String lastCommand;

    public EventHandling(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        lastCommand = e.getActionCommand();
        handleInput();
    }

    public void handleInput() {
        if (lastCommand == null) return;
        switch (lastCommand) {
            case "PLAY"   -> controller.selectLevel(1);
            case "LEVELS" -> controller.handleLevelSelection();
            case "QUIT"   -> System.exit(0);
            case "BACK"   -> controller.handleMainMenu();
            default -> {
                if (lastCommand.startsWith("LEVEL_")) {
                    controller.selectLevel(Integer.parseInt(lastCommand.substring(6)));
                }
            }
        }
    }
}
