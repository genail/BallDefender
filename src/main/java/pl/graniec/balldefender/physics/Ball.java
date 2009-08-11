package pl.graniec.balldefender.physics;

import java.util.LinkedList;
import java.util.List;

import pl.graniec.coralreef.geometry.Angle;
import pl.graniec.coralreef.geometry.Point2;
import pl.graniec.coralreef.geometry.Segment;
import pl.graniec.coralreef.geometry.Vector2;

public class Ball {
	
	class BounceResult {
		float vx, vy;
		float x, y;
		
		Point2 bouncePoint;
		Vector2 momentVector;
	}
	
	private final World world;
	
	float radius;
	
	float x, y;
	
	float vx, vy;
	
	Point2 lastIPoint = null;
	
	
	public Ball(World world) {
		super();
		this.world = world;
	}


	public void update(int timeElapsed) {
		final float timeChange = timeElapsed / 1000f;
		
		// target position
		final float tx = x + vx * timeChange;
		final float ty = y + vy * timeChange;
		
//		final Segment moveSegment = new Segment(new Point2(x, y), new Point2(tx, ty));
		
		boolean bounced;
		boolean positionSet = false;
		
		Point2 position = new Point2(x, y);
		Vector2 movement = new Vector2(vx, vy).multiply(timeChange);
		
		do {
			bounced = false;
			
			for (Resistor r : world.resistors) {
				
				final BounceResult bounceResult = bounce(position, movement, r);
				
				if (bounceResult != null) {
					
					x = bounceResult.x;
					y = bounceResult.y;
					vx = bounceResult.vx * (1000f / timeElapsed);
					vy = bounceResult.vy * (1000f / timeElapsed);
					
					position = bounceResult.bouncePoint;
					movement = bounceResult.momentVector;
					
					bounced = true;
					positionSet = true;
					
					break;
				}
			}
		} while (bounced);
		
		if (!positionSet) {
			x = tx;
			y = ty;
		}
	}


	/**
	 * Calculates the angle difference from <code>moveVector</code> to <code>moveSegment</code>
	 * from -180 to 180 in degrees.
	 * 
	 * @param moveVector
	 * @param resistorVector
	 * 
	 * @return Vectors angle difference.
	 */
	final float getAngleDiff(Vector2 moveVector, Vector2 resistorVector) {
		return Angle.fromDegrees(moveVector.angle()).degreeDifference(Angle.fromDegrees(resistorVector.angle()));
	}
	
	BounceResult bounce(Point2 startPoint, Vector2 moveVector, Resistor r) {
		
		// convert points to segments
		final List<Segment> segments = new LinkedList<Segment>();
		
		Point2 prev = null, first = null;
		
		for (Point2 p : r.geometry.getVerticles()) {
			if (first == null) {
				first = p;
			} else {
				segments.add(new Segment(prev, p));
			}
			
			prev = p;
		}
		
		if (prev != null && prev != first) {
			segments.add(new Segment(prev, first));
		}
		
		// check intersections
		// find the closest one
		Point2 closestPoint = null;
		float closestDistance = 0;
		Vector2 segmentVector = null;
		
		final Segment moveSegment = new Segment(
				startPoint,
				new Point2(startPoint.x + moveVector.x, startPoint.y + moveVector.y)
		); 
		
		for (Segment s : segments) {
			
			final Point2 iPoint = moveSegment.intersectionPoint(s);
			
			if (iPoint != null) {
				
				if (lastIPoint != null && lastIPoint.distanceTo(iPoint) < 0.1) {
					continue;
				}
				
				final float iDistance = startPoint.distanceTo(iPoint);
				if (closestPoint == null || closestDistance > iDistance) {
					closestPoint = iPoint;
					closestDistance = iDistance;
					segmentVector = new Vector2(s.x2 - s.x1, s.y2 - s.y1);
				}
			}
		}
		
		// if there is closest bounce point then make the bounce
		if (closestPoint != null) {
			
			lastIPoint = closestPoint;
			
			final float angleDiff = getAngleDiff(moveVector, segmentVector);
			
			// calculate new angle
			final float newAngle = segmentVector.angle() + angleDiff;
			
			// build moment vector (that will point to next position)
			// and ordinary move vector
			final Vector2 momentVector = new Vector2(newAngle);
			momentVector.multiply(moveSegment.length() - closestDistance);
			
			final Vector2 ordinaryVector = new Vector2(newAngle);
			ordinaryVector.multiply(moveVector.length());
			
			final BounceResult result = new BounceResult();
			
			result.x = closestPoint.x + momentVector.x;
			result.y = closestPoint.y + momentVector.y;
			result.vx = ordinaryVector.x;
			result.vy = ordinaryVector.y;
			
			result.bouncePoint = closestPoint;
			result.momentVector = momentVector;
			
			return result;
		}
		
		return null;
		
	}
	
}
