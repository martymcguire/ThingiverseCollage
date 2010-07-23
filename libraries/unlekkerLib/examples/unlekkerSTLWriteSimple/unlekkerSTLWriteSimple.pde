// unlekkerLib - Marius Watz, workshop.evolutionzone.com 
//
// Example showing how to write geometry to a STL file. 

import unlekker.data.*;

void setup() {
  size(400,400, P3D);

  noLoop();
}

void draw() {
  background(100);
  
  // Initialize STL output
  beginRaw("unlekker.data.STL","STLWrite.stl");

  translate(width/2,height/2,-width/2);
  sphere(width/2);

  // End STL output
  endRaw();    
}

