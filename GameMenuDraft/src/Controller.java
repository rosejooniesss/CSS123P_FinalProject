package gamemenudraft;

public class Controller {
    private GameEnvironment gameEnv;
    private View view;
    private EventHandling eventHandling;

    public Controller() {
        gameEnv = new GameEnvironment();
        view = new View();
        eventHandling = new EventHandling(this);
    }

    public void start() {
        view.setEventHandling(eventHandling);
        view.setVisible(true);
        handleMainMenu();
    }

    public void handleMainMenu()       { view.showMainMenu(); }
    public void handleLevelSelection() { view.showLevelSelection(); }
    public void handleGameplay()       { view.showLevel(gameEnv); }

    public void selectLevel(int level) {
        gameEnv.getMapData().level = level;
        handleGameplay();
    }
}
