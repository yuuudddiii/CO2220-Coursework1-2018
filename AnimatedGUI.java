import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics.*;

public class AnimatedGUI 
{
	private JFrame frame;
	private int xC; // x-coordinate of upper left corner of the square containing the circle
	private int xS; // x-coordinate of upper left corner of the square
	private int deltaXC; // for circle movement direction
	private int deltaXS; // for square movement direction
	private int diameter;
	private int width = 500;
	private int height = 500;
	private boolean animateCircle;
	private boolean animateSquare;
	
	private JButton startStopCircleButton;
	private JButton startStopSquareButton;
	private ShapesDrawPanel drawPanel;
	
	public AnimatedGUI(){
		xC = 0;
		xS = 0;
		deltaXS = 1;
		deltaXC= 1;		
		diameter=50;
		animateCircle = true;
		animateSquare = true;
		startStopCircleButton = new JButton("click me to start or stop the Circle");
		startStopSquareButton = new JButton("Click me to start or stop the Square");
		drawPanel = new ShapesDrawPanel();
	}		

	public static void main (String[] args){
		AnimatedGUI gui = new AnimatedGUI();
		gui.go();
		
	}

	public void go(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(startStopCircleButton, BorderLayout.PAGE_END);
		frame.getContentPane().add(startStopSquareButton, BorderLayout.PAGE_START);
		frame.getContentPane().add(drawPanel, BorderLayout.CENTER);
		startStopCircleButton.addActionListener(new CircleButtonListener());
		startStopSquareButton.addActionListener(new SquareButtonListener());
		frame.setSize(width,height);
		frame.setVisible(true);
	}
	
	class CircleButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			if (animateCircle) {
			animateCircle = false;
			drawPanel.repaint();
			}
			
			else {
				animateCircle = true;
				drawPanel.repaint();
			}
		}
	}
	
	class SquareButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent a) {
			if (animateSquare) {
				animateSquare = false;
				drawPanel.repaint();
			}
			
			else {
				animateSquare = true;
				drawPanel.repaint();
			}
		}
	}

	class ShapesDrawPanel extends JPanel{
		
		public void paintComponent (Graphics g){
			Graphics2D g2=(Graphics2D)g;
			g2.setColor(Color.yellow);
			g2.fillRect(0,0,this.getWidth(), this.getHeight());
			g2.setColor(Color.blue);
			g2.fillOval((this.getWidth()-(diameter+xC))/2,(this.getHeight()-(diameter+xC))/2,diameter+xC,diameter+xC);
			g2.setColor(Color.red);
			g2.fillRect(xS,0,50,50);
			if (animateCircle) animateCircle();
			if (animateSquare) animateSquare();
		}
		
		public void animateCircle(){
			try{
				Thread.sleep(3);
			}
			catch (Exception ex){}
			
			/* An expanding circle within a square fully covers the square when the
			 * diagonal of the square
			 * (the hypotenuse of the triangle with the edges of the square as its breadth and height)
			 * is equal to the diameter of the circle 
			 * , hence, when that condition is met, reverse the expansion of the circle
			 */
			if (xC >= (Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) - diameter)) deltaXC *= -1;
			else if (deltaXC < 0 && xC == 0) deltaXC *= -1; // reverse the contraction of the circle when it goes back to its original size
			
			xC += deltaXC; 
			repaint();
		}
		
		public void animateSquare(){
			try{
				Thread.sleep(3);
			}
			catch (Exception ex){}
			
			if (xS + 50 == width) deltaXS *= -1; // Reverse the movement when the square meets the right edge
			else if (deltaXS < 0 && xS == 0) deltaXS *=-1; // Reverse the movement when the square meets the left edge
			xS += deltaXS; 
			repaint();
		}
	}
}