package Display.GamePanel.Modes;

import Display.GamePanel.GamePanel;

public interface GamePanelMode {
    GamePanelMode NONE = new None();
    //GamePanelMode SELECTED = new Selected();


    GamePanelMode handleClick();
    void repaint(GamePanel gamePanel);
}
