import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GamePanel extends JPanel implements ActionListener {

    Image backgroundImg, flappyBirdImg, topPipeImg, bottomPipeImg;

    Bird bird;

    Timer gameLoop;

    GamePanel() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));

        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();
        flappyBirdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();

        bird = new Bird(flappyBirdImg, GameConstants.BOARD_WIDTH/8, GameConstants.BOARD_HEIGHT/2);

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        System.out.println("test");
        g.drawImage(backgroundImg, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
