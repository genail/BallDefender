package pl.graniec.balldefender.physics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;

public class WorldMain extends JFrame {

	private static final long serialVersionUID = 3034825644611979021L;
	
	private static final int HEIGHT = 480;
	private static final int WIDTH = 640;
	
	private static final int POINT_SIZE = 10;
	
	private class MyPanel extends JPanel {

		public MyPanel() {
			super();
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setSize(new Dimension(WIDTH, HEIGHT));
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			g.setColor(Color.gray);
			
			for (Geometry geom : geometries) {
				
				Point2 first = null, prev = null;
				
				for (Point2 p : geom.getVerticles()) {
					if (first == null) {
						first = p;
					} else {
						g.drawLine((int) prev.x, (int) prev.y, (int) p.x, (int) p.y);
					}
					
					prev = p;
				}
				
				g.drawLine((int) prev.x, (int) prev.y, (int) first.x, (int) first.y);
			}
			
			g.setColor(Color.red);
			
			final int halfSize = POINT_SIZE / 2;
			
			g.drawLine((int) ball.x - halfSize, (int) ball.y, (int) ball.x + halfSize, (int) ball.y);
			g.drawLine((int) ball.x, (int) ball.y - halfSize, (int) ball.x, (int) ball.y + halfSize);
		}
		
		@Override
		public void paintAll(Graphics g) {
			paint(g);
		}
		
		@Override
		public void update(Graphics g) {
			paint(g);
		}
		
	}
	
	private final World world = new World();
	
	private final Ball ball = new Ball(world);
	
	private final List<Geometry> geometries = new LinkedList<Geometry>();
	
	public WorldMain() {
		
		add(new MyPanel());
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		Geometry border = new Geometry();
		border.addVerticle(new Point2(0, 0));
		border.addVerticle(new Point2(WIDTH, 0));
		border.addVerticle(new Point2(WIDTH, HEIGHT));
		border.addVerticle(new Point2(0, HEIGHT));
		
		geometries.add(border);
		
		world.resistors.add(new Resistor(border));
		
		Geometry g1 = new Geometry();
		g1.addVerticles(new Point2[] {
				new Point2(10, 10),
				new Point2(100, 20),
				new Point2(200, 40),
				new Point2(170, 140),
				new Point2(130, 160),
				new Point2(10, 10),
		});
		
		geometries.add(g1);
		
		world.resistors.add(new Resistor(g1));
		
		ball.x = WIDTH / 2;
		ball.y = HEIGHT / 2;
		
		ball.vx = 500;
		ball.vy = 500;
		
		world.balls.add(ball);
	}
	
	public void update(int elapsedTime) {
		world.update(elapsedTime);
		repaint();
	}
	
	private static long last = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final WorldMain window = new WorldMain();
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				
				long now = System.currentTimeMillis();
				long delta = (last == 0) ? 0 : now - last;
				
				window.update((int) delta);
				
				last = now;
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 1000/60, 1000/60);
		
		window.setVisible(true);
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

}
