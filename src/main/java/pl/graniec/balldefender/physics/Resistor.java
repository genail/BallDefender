package pl.graniec.balldefender.physics;

import java.util.LinkedList;
import java.util.List;

import pl.graniec.atlantis.Core;
import pl.graniec.atlantis.Drawable;
import pl.graniec.atlantis.Graphics;
import pl.graniec.atlantis.drawables.Line;
import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;

public class Resistor {
	
	final Geometry geometry;

	public Resistor(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public Drawable debugGetDrawable(final Core core) {
		return new Drawable() {
			
			private final List<Line> lines = new LinkedList<Line>();
			
			@Override
			public void draw(Graphics g) {
				if (lines.isEmpty()) {
					buildLines();
				}
				
				for (Line l : lines) {
					l.draw(g);
				}
			}
			
			private void buildLines() {
				
				Point2 first = null;
				Point2 prev = null;
				
				for (Point2 p : geometry.getVerticles()) {
					if (first == null) {
						first = p;
						prev = p;
						continue;
					}
					
					Line line = core.newLine();
					line.setPosition(prev.x, prev.y, p.x, p.y);
					line.setColor(0xFFFFFF00);
					lines.add(line);
					
					prev = p;
				}
				
				if (first != null && prev != first) {
					Line line = core.newLine();
					line.setPosition(prev.x, prev.y, first.x, first.y);
					line.setColor(0xFFFFFF00);
					lines.add(line);
				}
			}
		};
	}
	
}
