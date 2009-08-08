package pl.graniec.balldefender.physics;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;
import pl.graniec.coralreef.geometry.Segment;
import pl.graniec.coralreef.geometry.Vector2;

public class BallTest {

	@Test
	public void testBounce() {
		World world = new World();
		Ball ball = new Ball(world);
	
		Point2 startPoint = new Point2(0, 0);
		final Vector2 moveVector = new Vector2(10, 10);
		
		Geometry geometry = new Geometry();
		geometry.addVerticle(new Point2(5, 0));
		geometry.addVerticle(new Point2(5, 10));
		
		Resistor resistor = new Resistor(geometry);
		
		Segment bounceSegment = ball.bounce(startPoint, moveVector, resistor);
		System.out.println(bounceSegment);
	}

}
