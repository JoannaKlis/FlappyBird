import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    Image backgroundImg, birdImg, topPipeImg, bottomPipeImg, gameOverImg, menuImg, restartImg;

    private final int birdX = GameConstants.BOARD_WIDTH / 8;
    private final int birdY = GameConstants.BOARD_HEIGHT / 2;

    private Bird bird;
    private final ArrayList<Pipe> pipes = new ArrayList<>();

    int velocityX;
    int velocityY = -9;
    int gravity;

    Timer gameLoop;
    Timer placePipeTimer;
    private boolean gameOver = false;
    private double score = 0;

    private final BirdType selectedType;
    private final JFrame frame;

    private Rectangle menuIconRect;
    private Rectangle restartIconRect;

    public GamePanel(BirdType selectedType, JFrame frame) {
        this.selectedType = selectedType;
        this.frame = frame;
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();
        gameOverImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./gameover.png"))).getImage();
        menuImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./menu.png"))).getImage();
        restartImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./restart.png"))).getImage();

        switch (selectedType) {
            case FAST -> {
                birdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdblue.png"))).getImage();
                gravity = 1;
                velocityX = -8;
            }
            case HEAVY -> {
                birdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdpink.png"))).getImage();
                gravity = 2;
                velocityX = -4;
            }
            default -> {
                birdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
                gravity = 1;
                velocityX = -4;
            }
        }

        bird = new Bird(birdImg, birdX, birdY, selectedType);

        placePipeTimer = new Timer(GameConstants.PIPE_INTERVAL, _ -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void placePipes() {
        int randomPipeY = (int) (0 - (double) GameConstants.PIPE_HEIGHT / 4 - Math.random() * ((double) GameConstants.PIPE_HEIGHT / 2));
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
            int gameOverX = (GameConstants.BOARD_WIDTH - gameOverImg.getWidth(null)) / 2;
            int gameOverY = (GameConstants.BOARD_HEIGHT - gameOverImg.getHeight(null)) / 2;
            g.drawImage(gameOverImg, gameOverX, gameOverY, null);

            int iconWidth = gameOverImg.getWidth(null) / 2;
            int iconHeight = gameOverImg.getHeight(null) / 2;

            int iconsY = gameOverY + gameOverImg.getHeight(null) + 20;
            int totalIconsWidth = iconWidth * 2 + 40;
            int startX = (GameConstants.BOARD_WIDTH - totalIconsWidth) / 2;

            g.drawImage(menuImg, startX, iconsY, iconWidth, iconHeight, null);
            menuIconRect = new Rectangle(startX, iconsY, iconWidth, iconHeight);

            int restartX = startX + iconWidth + 40;
            g.drawImage(restartImg, restartX, iconsY, iconWidth, iconHeight, null);
            restartIconRect = new Rectangle(restartX, iconsY, iconWidth, iconHeight);
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
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                velocityY = -9;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver) {
            Point p = e.getPoint();

            if (menuIconRect != null && menuIconRect.contains(p)) {
                Main.showMainMenu(frame);
            } else if (restartIconRect != null && restartIconRect.contains(p)) {
                bird = new Bird(birdImg, birdX, birdY, selectedType);
                velocityY = -9;
                pipes.clear();
                score = 0;
                gameOver = false;

                placePipeTimer.restart();
                gameLoop.restart();
                requestFocus();
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
