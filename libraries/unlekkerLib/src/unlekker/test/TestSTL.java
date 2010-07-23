package unlekker.test;

import processing.core.*;
import processing.opengl.*;
import unlekker.data.*;
import unlekker.geom.*;


public class TestSTL extends PApplet {
	
	STL stl;
	FaceList poly;

	public void setup() {
	  size(400,400, P3D);
	  frameRate(25);
	  sphereDetail(12);
	}

	public void draw() {
	  translate(width/2,height/2);

	  // First write the STL data
	  if(frameCount==10) outputSTL();
	  // Then read it back in
	  else if(frameCount==11) readSTL();

	  // If data has been read, then draw it
	  if(poly!=null) {
	    background(0);
	    noStroke();
	    lights();
	    rotateY(radians(frameCount));
	    rotateX(radians(frameCount*0.25f));
	    fill(0,200,255, 128);

	    poly.draw(this);
	  }
	}

	public void readSTL() {
	  // Read STL file
	  stl=new STL(this,"Boxes.stl");

	  // Get polygon data
	  poly=stl.getPolyData();

	  poly.normalize(400); // normalize object to 400 radius
	  println(poly.bb.toString());
	  poly.center(); // center it around world origin
	}

	public void outputSTL() {
	  float rad;

	  // Initialize STL output
	  stl=(STL)beginRaw("unlekker.data.STL","Boxes.stl");

	  // Draw random shapes
	  for(int i=0; i<200; i++) {
	    pushMatrix();
	    translate(random(-200,200),0,-random(400));
	    rotateX(((float)(int)random(6))*radians(30));
	    rotateY(((float)(int)random(6))*radians(30));

	    rad=random(5,25);
	    if(random(100)>5) box(rad,random(50,200),rad);
	    else sphere(rad);
	    popMatrix();
	  }
	  
	  // End STL output
	  endRaw();
	}	
	public static void main(String [] args) {
		PApplet.main(new String [] {"unlekker.test.TestSTL"});
	}

}
