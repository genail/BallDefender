package pl.graniec.balldefender.physics;

import java.util.LinkedList;
import java.util.List;

public class World {

	final List<Resistor> resistors = new LinkedList<Resistor>();
	
	final List<Ball> balls = new LinkedList<Ball>();
	
	public void update(int elapsedTime) {
		for (Ball b : balls) {
			b.update(elapsedTime);
		}
	}
	
}
