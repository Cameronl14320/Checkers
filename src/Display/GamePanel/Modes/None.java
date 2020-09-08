package Display.GamePanel.Modes;

import Display.GamePanel.GamePanel;

public class None implements GamePanelMode {
    @Override
    public GamePanelMode handleClick() {
        return null;
    }

    @Override
    public void repaint(GamePanel gamePanel) {

    }
}
