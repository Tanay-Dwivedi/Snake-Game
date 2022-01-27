import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	static final int screenWidth = 800;
	static final int screenHeight = 800;
	static final int unitSize = 25;
	static final int gameUnits = (screenWidth * screenHeight)/unitSize;
	static final int delay = 100;
	final int x[] = new int[gameUnits];
	final int y[] = new int[gameUnits];
	int snakeBodyParts = 5;
	int foodEaten;
	int foodX;
	int foodY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(new Color(144,238,144));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newFood();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			
			// for setting the grid lines
//			for (int i=0; i<screenHeight/unitSize; i++) {
//				g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
//				g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
//			}
			g.setColor(new Color(0,100,0));
			g.fillOval(foodX, foodY, unitSize, unitSize);
			
			for(int i=0; i<snakeBodyParts; i++) {
				if(i==0) {
					g.setColor(new Color(105,105,105));
					g.fillRect(x[i], y[i], unitSize, unitSize);
				}
				else {
					g.setColor(new Color(112,128,144));
					g.fillRect(x[i], y[i], unitSize, unitSize);
				}
			}
			g.setColor(new Color(0,0,255));
			g.setFont(new Font("Bebas Neue", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+foodEaten +"   "+ "Snake size: " +(snakeBodyParts-5), (screenWidth-metrics.stringWidth("Score: "+foodEaten +"   "+ "Snake size: " +snakeBodyParts))/2, g.getFont().getSize());
			
		}
		else {
			gameOver(g);
		}
	}
	
	public void newFood() {
		foodX = random.nextInt((int)(screenWidth/unitSize)) * unitSize;
		foodY = random.nextInt((int)(screenHeight/unitSize)) * unitSize;
	}
	
	public void move() {
		for(int i=snakeBodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch (direction) {
		case 'U' :
			y[0] = y[0] - unitSize;
			break;
		case 'D' :
			y[0] = y[0] + unitSize;
			break;
		case 'L' :
			x[0] = x[0] - unitSize;
			break;
		case 'R' :
			x[0] = x[0] + unitSize;
			break;
		}
	}
	
	public void checkFood() {
		if((x[0]==foodX) && (y[0]==foodY)) {
			if (snakeBodyParts >= 10 && snakeBodyParts <20) {
				snakeBodyParts += 2;
				foodEaten += 10;
			}
			else if(snakeBodyParts >= 20 && snakeBodyParts <30) {
				snakeBodyParts += 3;
				foodEaten += 15;
			}
			else if(snakeBodyParts >= 30) {
				snakeBodyParts += 4;
				foodEaten += 20;
			}
			else {
				snakeBodyParts++;
				foodEaten+=5;
			}
			newFood();
		}
	}
	
	public void checkCollisions() {
		
		// checking if the head collids with the body
		for(int i=snakeBodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//checking if the head touches the left border
		if(x[0] < 0) {
			running = false;
		}
		
		// checking if the head touches the right border.
		if(x[0] > screenWidth) {
			running = false;
		}
		
		// checking if the head touches the top border.
		if(y[0] < 0) {
			running = false;
		}

		// checking if the head touches the bottom border.
		if(y[0] > screenHeight) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
		
	}
	
	public void gameOver(Graphics g) {
		
		//scores display
		g.setColor(new Color(0,0,255));
		g.setFont(new Font("Bebas Neue", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+foodEaten +"   "+ "Snake size: " +(snakeBodyParts-5), (screenWidth-metrics1.stringWidth("Score: "+foodEaten +"   "+ "Snake size: " +snakeBodyParts))/2, g.getFont().getSize());
		
		// setting the game over text
		g.setColor(Color.RED);
		g.setFont(new Font("Bebas Neue", Font.BOLD, 100));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (screenWidth-metrics2.stringWidth("Game Over"))/2, screenHeight/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkFood();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
		
	}

}
