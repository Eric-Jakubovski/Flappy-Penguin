import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyPenguin extends JPanel implements ActionListener, KeyListener {
    private int penguinY = 250;
    private int penguinVelocity = 0;
    private Timer timer;
    private ArrayList<Rectangle> pipes;
    private boolean gameOver = false;

    public FlappyPenguin() {
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        pipes = new ArrayList<>();
        addPipe(true);  // Start with a full set of pipes
        addPipe(true);
    }

    public void addPipe(boolean start) {
        int pipeHeight = (int) (Math.random() * 200 + 100);
        int spacing = 300;
        if (start) {
            pipes.add(new Rectangle(800 + pipes.size() * spacing, 0, 50, pipeHeight));
            pipes.add(new Rectangle(800 + pipes.size() * spacing, pipeHeight + 150, 50, 600 - pipeHeight - 150));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + spacing, 0, 50, pipeHeight));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + spacing, pipeHeight + 150, 50, 600 - pipeHeight - 150));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN); // Background
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.BLACK); // Penguin body
        g.fillRect(100, penguinY, 20, 30);
        g.setColor(Color.WHITE); // Penguin belly
        g.fillRect(105, penguinY + 10, 10, 10);
        g.setColor(Color.ORANGE); // Penguin feet
        g.fillRect(100, penguinY + 30, 20, 5);

        g.setColor(Color.GREEN); // Pipes
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 300, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        penguinY += penguinVelocity;
        penguinVelocity += 1; // Gravity effect
        if (penguinY > 570) penguinY = 570; // Ground collision

        ArrayList<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
            if (pipe.x + pipe.width < 0) {
                toRemove.add(pipe);
            }
        }
        pipes.removeAll(toRemove);

        if (pipes.get(pipes.size() - 1).x < 700) {
            addPipe(false);
        }

        checkCollisions();

        repaint();
    }

    private void checkCollisions() {
        Rectangle penguin = new Rectangle(100, penguinY, 20, 30);
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(penguin) || penguinY <= 0 || penguinY >= 570) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            penguinVelocity = -10; // Penguin jumps
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        FlappyPenguin game = new FlappyPenguin();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
