import javax.swing.*;
import java.awt.*;

public class FlappyBird extends JPanel {
    int boardWidth = 360;
    int boardHeight = 640;
    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLUE);
    }
}
