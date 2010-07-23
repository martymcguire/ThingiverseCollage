import processing.opengl.*;

// unlekkerLib - Marius Watz, workshop.evolutionzone.com 
//
// Example using a 2-dimensional array of 3D vectors to calculate
// a mesh.

import unlekker.util.*;
import unlekker.geom.*;
import unlekker.data.*;

// 2-dimensional array of 3D vector objects to contain our mesh
Vec3 mesh[][];

// num will decide the mesh dimensions
int num;  

// boolean flags indicating whether or not we should
// output STL or PDF
boolean doSTL=false,doPDF=false;

// use the CTRL key for interface
boolean ctrlDown=false;

// translation the Z direction
float xUnit,yUnit, zTrans;
float rotX,rotY,amplitude;


void setup() {
  size(800,800,OPENGL);

  // allocate space for the mesh and fill it with empty vectors  
  num=100;
  mesh=new Vec3[num][num];
  meshPlane();
  
  xUnit=width/(float)num;
  yUnit=height/(float)num;

  rotX=30;
  rotY=45;
  amplitude=100;
  zTrans=-width/2;

  // add a MouseWheelListener so we can use the mouse wheel
  // to zoom with
  frame.addMouseWheelListener(new MouseWheelInput());
}

void draw() {
  background(0);

  if(doSTL) 
    beginRaw("unlekker.data.STL","UnlekkerMesh"+frameCount+".stl");
  else if(doPDF) 
    beginRaw(PDF,"UnlekkerMesh"+frameCount+".pdf");

  lights();
  
  // translate to center of screen and use mouse movement
  // to rotate the scene
  translate(width/2,height/2,zTrans);
  rotateX(rotX);
  rotateY(rotY);

  
  stroke(255);
  fill(0,255,255, 200);  
  drawMesh();

  if(doSTL || doPDF) {
    endRaw();
    doSTL=false;
    doPDF=false;
  }
}

// different meshes can be triggered by the keyboard.
// STL and PDF output is also available

void keyPressed() {
  if(key=='1') meshPlane();
  else if(key=='2') meshNoise();
  else if(key=='3') meshSineWave();
  else if(key=='4') meshCosDist();

  if(key=='s') doSTL=true;
  if(key=='p') doPDF=true;
  
  // check for CTRL key
  if (key == CODED && keyCode==CONTROL) {
    ctrlDown=true;
        println("ctrlDown "+ctrlDown);
  }
}

void keyReleased() {
  // check for CTRL key to see if it has been released
  if (key == CODED && keyCode==CONTROL) {
    ctrlDown=false;
  }
}

void mouseDragged() {
  // if CTRL key is down, then adjust amplitude
  if(ctrlDown) {
    amplitude=((float)mouseY/(float)height)*height;
    println("amplitude "+amplitude);
  }
  // else rotate the viewport
  else {
    rotX=((float)mouseX/(float)width)*2*PI;
    rotY=((float)mouseY/(float)height)*2*PI;
  }
  
}

// drawMesh is a custom method that takes care of drawing the
// mesh using beginShape() / endShape()
void drawMesh() {

  for(int i=0; i<num-1; i++) {
    beginShape(QUAD_STRIP);
    for(int j=0; j<num; j++) {
      vertex(mesh[j][i].x,mesh[j][i].y * amplitude ,mesh[j][i].z);
      vertex(mesh[j][i+1].x,mesh[j][i+1].y  * amplitude, mesh[j][i+1].z);
    }
    endShape();    
  }
}

// sets the mesh to an even grid plane
void meshPlane() {
  for(int i=0; i<num; i++) 
    for(int j=0; j<num; j++) 
      mesh[i][j]=new Vec3((i-num/2)*10,0,(j-num/2)*10);
}

// sets the mesh data to a Perlin noisefield
void meshNoise() {
  float x,y,a,aD,b,bD,val;

  // initialize parameters for the noise field. 
  // "a" is our position in the X direction of the noise.
  // "b" is our position in the X direction of the noise.
  // "aD" and "bD" are used to traverse the noise field.
  a=random(1000);
  b=random(1000);
  aD=1.0/random(50,150);
  bD=1.0/random(50,150);

  // set amplitude and noiseDetail for noise field
  noiseDetail((int)random(4,8),random(0.4,0.9));

  for(int i=0; i<num; i++) 
    for(int j=0; j<num; j++) {

      // calculate height as a function of 2D noise
      val=(noise(a+aD*(float)i,b+bD*(float)j)-0.5)*2;

      x=((float)i-num/2)*xUnit;;
      y=((float)j-num/2)*yUnit;
      mesh[i][j].set(x,val,y);

    }
}

// sets the mesh data to a 3D plot of two sine waves
void meshSineWave() {
  float x,y,a,aD,b,bD;
  float val;

  // set random starting values and wavelengths
  // for our sine wave landscape
  a=radians(random(360));
  b=radians(random(360));
  aD=radians(random(0.1,12));
  bD=radians(random(0.1,12));

  for(int i=0; i<num; i++) 
    for(int j=0; j<num; j++) {
      x=((float)i-num/2)*xUnit;;
      y=((float)j-num/2)*yUnit;
      // calculate height as a function of two sine curves
      val=sin(a+aD*(float)i) * sin(b+bD*(float)j);
      mesh[i][j].set(x,val,y);

    }
}

// sets the mesh data to a 3D plot of cubic numbers
void meshCosDist() {
  float x,y,mod;
  float val;
  mod=1.0 / random(5,25);

  for(int i=0; i<num; i++) 
    for(int j=0; j<num; j++) {
      x=((float)i-num/2)*xUnit;;
      y=((float)j-num/2)*yUnit;

      // calculate height as y= cos(r), r being the distance
     // from the centre of the mesh
      val=cos(sqrt(x*x+y*y)*mod);

      mesh[i][j].set(x,val, y);
    }
}

// convenience class to listen for MouseWheelEvents and
// use it for that classic "zoom" effect
 
class MouseWheelInput implements MouseWheelListener{

  void mouseWheelMoved(MouseWheelEvent e) {
    int step=e.getWheelRotation();
    zTrans=zTrans+step*50;
  }
 
}
