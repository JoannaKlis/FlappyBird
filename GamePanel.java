import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Images
    private Image backgroundImg;
    private Image birdImg;
    private Image topPipeImg;
    private Image bottomPipeImg;
    private Image gameOverImg; // Dodana grafika game over

    // Game constants
    private final int birdX = GameConstants.BOARD_WIDTH / 8;
    private final int birdY = GameConstants.BOARD_HEIGHT / 2;

    private Bird bird;
    private final ArrayList<Pipe> pipes = new ArrayList<>();
    private final Random random = new Random();

    private final int velocityX = -4; // Pipes move left
    private int velocityY = 0;
    private final int gravity = 1;

    private Timer gameLoop;
    private Timer placePipeTimer;
    private boolean gameOver = false;
    private double score = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        gameOverImg = new ImageIcon(getClass().getResource("./gameover.png")).getImage(); // Ładowanie grafiki game over

        bird = new Bird(birdImg, birdX, birdY);

        // Timers
        placePipeTimer = new Timer(GameConstants.PIPE_INTERVAL, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void placePipes() {
        int randomPipeY = (int) (0 - GameConstants.PIPE_HEIGHT / 4 - Math.random() * (GameConstants.PIPE_HEIGHT / 2));
        int openingSpace = GameConstants.BOARD_HEIGHT / 4;

        Pipe topPipe = new Pipe(topPipeImg, GameConstants.BOARD_WIDTH, randomPipeY);
        Pipe bottomPipe = new Pipe(bottomPipeImg, GameConstants.BOARD_WIDTH, randomPipeY + GameConstants.PIPE_HEIGHT + openingSpace);

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(String.valueOf((int) score), 10, 35);

        if (gameOver) {
            // Oblicz pozycję do wyświetlenia grafiki gameover na środku ekranu
            int x = (GameConstants.BOARD_WIDTH - gameOverImg.getWidth(null)) / 2;
            int y = (GameConstants.BOARD_HEIGHT - gameOverImg.getHeight(null)) / 2;
            g.drawImage(gameOverImg, x, y, null);
        }
    }

    private void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > GameConstants.BOARD_HEIGHT) {
            gameOver = true;
        }
    }

    private boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                bird = new Bird(birdImg, birdX, birdY);
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}