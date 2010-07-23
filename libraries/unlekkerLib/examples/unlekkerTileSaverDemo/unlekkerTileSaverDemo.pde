import processing.opengl.*;

// unlekkerLib - Marius Watz, workshop.evolutionzone.com 
//
// Demonstrates the use of unlekker.util.TileSaver to
// produce (very) high-res images from realtime OpenGL
// applications through the use of viewport tiling.

import unlekker.util.TileSaver;
import unlekker.geom.Vec3;

int num;
Pt pt[];
TileSaver tiler;

void setup() {
  size(400,400, OPENGL);
  
  num=100;
  pt=new Pt[num];
  for(int i=0; i<num; i++) pt[i]=new Pt();  

  tiler=new TileSaver(this);
}
 
public void draw() {
  if(tiler==null) return; // Not initialized
    tiler.pre();

  background(50);
  lights();
  translate(width/2,height/2,0);
  
  noStroke();
  for(int i=0; i<num; i++) pt[i].draw();
  
  tiler.post();
}

  public void pre() {
    println("pre()");
  }

  public void post() {
  }


// Saves tiled imaged when 't' is pressed
public void keyPressed() {
  if(key=='t') tiler.init("Simple"+nf(frameCount,5),8);
}


class Pt {
  Vec3 v;
  float rad;
  color col;

  public Pt() {
    v=new Vec3(random(-0.5,0.5)*width,
    random(-0.5,0.5)*height,
    random(-0.15,0.15)*width);
    rad=random(20,50);
    
    if(random(100)>60) col=color(random(50,100));
    else col=color(random(160,22));
  }

  void draw() {
    pushMatrix();
    translate(v.x,v.y,v.z);
    fill(col);
    sphere(rad);
    popMatrix();    
  }
}
