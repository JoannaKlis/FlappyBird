import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    Image backgroundImg, flappyBirdImg, topPipeImg, bottomPipeImg;

    Bird bird;
    int velocityY = 0;
    int gravity = 1;

    Timer gameLoop;

    GamePanel() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));

        setFocusable(true);
        addKeyListener(this);

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
        g.drawImage(backgroundImg, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
