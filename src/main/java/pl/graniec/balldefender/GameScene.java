package pl.graniec.balldefender;

import pl.graniec.atlantis.Core;
import pl.graniec.atlantis.Scene;
import pl.graniec.atlantis.Stage;
import pl.graniec.atlantis.Window;
import pl.graniec.atlantis.drawables.ImageSprite;
import pl.graniec.atlantis.opengl.GLCore;
import pl.graniec.balldefender.physics.Resistor;
import pl.graniec.balldefender.physics.World;
import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;

public class GameScene extends Scene {

	/** Physics world */
	private final World world = new World();
	
	@Override
	public void load() {
		Core core = Core.getCurrent();
		ImageSprite bg = core.newImageSprite("classpath:/level.png");
		
		bg.width.set(Stage.getWidth());
		bg.height.set(Stage.getHeight());
		
		add(bg);
		
		ImageSprite ball = core.newImageSprite("classpath:/ball.png");
		
		ball.x.set(50);
		ball.y.set(50);
		
		ball.width.set(40);
		ball.height.set(40);
		
		add(ball);
		
//		Geometry geometry = new Geometry();
//		geometry.addVerticle(new Point2(100, 100));
//		geometry.addVerticle(new Point2(120, 100));
//		geometry.addVerticle(new Point2(120, 120));
//		geometry.addVerticle(new Point2(100, 140));
//		
//		Resistor resistor = new Resistor(geometry);
//		add(resistor.debugGetDrawable(core));
		
		buildWorld();
	}
	
	public static void main(String[] args) {
		Core core = new GLCore();
		Window window = core.newWindow();
		window.setTitle("Test");
		window.show();
		
		Scene scene = new GameScene();
		Stage.setScene(scene);
	}
	
	private void buildWorld() {
		
		final float tolerance = 0.1f;
		
		// left top
		Geometry ltGeom = arc(270 - tolerance, 360 + tolerance, 32, 62);
		Resistor ltResistor = new Resistor(ltGeom);
		
		// right top
		Geometry rtGeom = arc(180 - tolerance, 270 + tolerance, 32, 62);
		rtGeom.translate(Stage.getWidth(), 0);
		Resistor rtResistor = new Resistor(rtGeom);
		
		// right bottom
		Geometry rbGeom = arc(90 - tolerance, 180 + tolerance, 32, 62);
		rbGeom.translate(Stage.getWidth(), Stage.getHeight());
		Resistor rbResistor = new Resistor(rbGeom);
		
		// left bottom
		Geometry lbGeom = arc(0 - tolerance, 90 + tolerance, 32, 62);
		lbGeom.translate(0, Stage.getHeight());
		Resistor lbResistor = new Resistor(lbGeom);
		
		add(ltResistor.debugGetDrawable(Core.getCurrent()));
		add(rtResistor.debugGetDrawable(Core.getCurrent()));
		add(rbResistor.debugGetDrawable(Core.getCurrent()));
		add(lbResistor.debugGetDrawable(Core.getCurrent()));
	}
	
	private Geometry arc(float angleFrom, float angleTo, float resolution, float radius) {
		
		final Geometry geometry = new Geometry();
		
		geometry.addVerticle(new Point2(0, 0));
		
		for (int i = 0; i <= resolution; ++i) {
			final float degree = 360 * (i / resolution);
			
			if (degree < angleFrom || degree > angleTo) {
				continue;
			}
			
			final float sin = (float) Math.sin(Math.toRadians(degree));
			final float cos = (float) Math.cos(Math.toRadians(degree));
			
			final Point2 point = new Point2(cos * radius, -sin * radius);
			geometry.addVerticle(point);
		}
		
		return geometry;
	}

}
