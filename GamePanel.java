import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GamePanel extends JPanel {

    Image backgroundImg, flappyBirdImg, topPipeImg, bottomPipeImg;

    Bird bird;

    GamePanel() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));

        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();
        flappyBirdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();

        bird = new Bird(flappyBirdImg, GameConstants.BOARD_WIDTH/8, GameConstants.BOARD_HEIGHT/2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    }
}
