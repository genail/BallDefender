package pl.graniec.balldefender.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.graniec.coralreef.geometry.Angle;
import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;
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
		
		final Ball.BounceResult result = ball.bounce(startPoint, moveVector, resistor);
		
		assertEquals(0, result.x, 0.01);
		assertEquals(10, result.y, 0.01);
		assertEquals(-10, result.vx, 0.01);
		assertEquals(10, result.vy, 0.01);
	}
	
	@Test
	public void testGetAngleDiff1() {
		final Vector2 r = new Vector2(0, 1);
		final Vector2 m = new Vector2(1, 1);
		
		final float diff = Angle.fromDegrees(m.angle()).degreeDifference(Angle.fromDegrees(r.angle()));
		
		assertEquals(45, diff, 0.01);
	}
	
	@Test
	public void testGetAngleDiff2() {
		final Vector2 r = new Vector2(0, 1);
		final Vector2 m = new Vector2(1, -1);
		
		final float diff = Angle.fromDegrees(m.angle()).degreeDifference(Angle.fromDegrees(r.angle()));
		
		assertEquals(135, diff, 0.01);
	}
	
	@Test
	public void testGetAngleDiff3() {
		final Vector2 r = new Vector2(0, 1);
		final Vector2 m = new Vector2(-1, 1);
		
		final float diff = Angle.fromDegrees(m.angle()).degreeDifference(Angle.fromDegrees(r.angle()));
		
		assertEquals(-45, diff, 0.01);
	}

}
