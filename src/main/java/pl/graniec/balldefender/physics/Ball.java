package pl.graniec.balldefender.physics;

import java.util.LinkedList;
import java.util.List;

import pl.graniec.coralreef.geometry.Point2;
import pl.graniec.coralreef.geometry.Segment;
import pl.graniec.coralreef.geometry.Vector2;

public class Ball {
	
	private final World world;
	
	float radius;
	
	float x, y;
	
	float vx, vy;
	
	
	public Ball(World world) {
		super();
		this.world = world;
	}


	public void update(int timeElapsed) {
		final float timeChange = timeElapsed / 1000f;
		
		// target position
		final float tx = x + vx * timeChange;
		final float ty = x + vy * timeChange;
		
		final Segment moveSegment = new Segment(new Point2(x, y), new Point2(tx, ty));
		
		boolean bounced;
		
		do {
			bounced = false;
			
			for (Resistor r : world.resistors) {
				if(bounce(moveSegment, r)) {
					bounced = true;
					continue;
				}
			}
		} while (bounced);
		
		
	}


	enum BounceSide {
		LEFT,
		RIGHT
	}
	
	final BounceSide bounceSide(Point2 startPoint, Vector2 moveVector, Segment resistorSegment) {
		final float moveVectorAngle = moveVector.angle();
		final float resistorSegmentAngle = Vector2.angle(
				resistorSegment.x2 - resistorSegment.x1,
				resistorSegment.y2 - resistorSegment.y1
		);
		
		
	}
	
	Segment bounce(Point2 startPoint, Vector2 moveVector, Resistor r) {
		
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
		float angle = 0;
		
		final Segment moveSegment = new Segment(
				startPoint,
				new Point2(startPoint.x + moveVector.x, startPoint.y + moveVector.y)
		); 
		
		for (Segment s : segments) {
			final Point2 iPoint = moveSegment.intersectionPoint(s);
			
			if (iPoint != null) {
				final float iDistance = startPoint.distanceTo(iPoint);
				if (closestPoint == null || closestDistance > iDistance) {
					closestPoint = iPoint;
					closestDistance = iDistance;
					angle = Point2.angle(s.x2 - s.x1, s.y2 - s.y1);
				}
			}
		}
		
		// if there is closest bounce point then make the bounce
		if (closestPoint != null) {
			float moveAngle = Point2.angle(moveSegment.x2 - moveSegment.x1, moveSegment.y2 - moveSegment.y1);
			
			angle += 180;
			moveAngle += 180;
			
			angle %= 180;
			moveAngle %= 180;
			
			final float diff = angle - moveAngle;
			final Vector2 directionVector = new Vector2(moveAngle + diff).multiply(moveSegment.length() - closestDistance); 
			
			return new Segment(closestPoint, new Point2(directionVector.x, directionVector.y));
		}
		
		return null;
		
	}
	
}
