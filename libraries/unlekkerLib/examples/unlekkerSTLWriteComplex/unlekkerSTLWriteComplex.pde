// unlekkerLib - Marius Watz, workshop.evolutionzone.com 
//
// Example showing how to write geometry to a STL file. 

import unlekker.data.*;
import unlekker.geom.*;

STL stl;
FaceList poly;
boolean doSave=false;

int num=60;
float pt[];

void setup() {
  size(400,400, P3D);

  pt=new float[num*3];
  initShape();
}

void draw() {
  background(100);
  
  if(doSave) {
    // Initialize STL output
    stl=(STL)beginRaw("unlekker.data.STL","Complex.stl");
  }  

  translate(width/2,height/2,-width/2);
  lights();
  
  // rotate using mouse position
  rotateX(2*PI*((float)mouseX/(float)width));
  rotateY(2*PI*((float)mouseY/(float)height));

  // draw triangles using the pt array
  fill(255,100,0);
  
  int id=0;
  beginShape(TRIANGLE_STRIP);
  for(int i=0; i<num; i++) 
    vertex(pt[id++],pt[id++],pt[id++]);
  endShape();  

  if(doSave) {
     // End STL output
    endRaw();    
    doSave=false;
  }
 
}

void initShape() {
  for(int i=0; i<num; i++) {
    pt[i*3]=random(-width/2,width/2);
    pt[i*3+1]=random(-height/2,height/2);
    pt[i*3+2]=random(-width/2,width/2);
  }
}  

void keyPressed() {
  if(key==' ') initShape();
  else if(key=='s') doSave=true;
}
