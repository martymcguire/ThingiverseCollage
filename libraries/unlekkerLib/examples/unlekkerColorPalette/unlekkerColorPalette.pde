// unlekkerLib - Marius Watz, workshop.evolutionzone.com 
//
// Example showing how to use unlekker.util.ColorPalette to
// generate dynamic color palettes for your sketches. Left-click
// to regenerate random composition, right-click to only change
// colors.

import unlekker.util.*;

ColorPalette pal;
int num;
float x[],y[],rad[];
color col[];

void setup() {
  size(400,400);
  pal=new ColorPalette();
  
  num=150;
  x=new float[num];
  y=new float[num];
  rad=new float[num];
  col=new color[num];

  initComposition(); 
}

void draw() {
  background(200);
  smooth();
  
  for(int i=0; i<num; i++) {
    fill(col[i]);
    ellipse(x[i],y[i], rad[i],rad[i]);  
  }
}

void initColors() {
  // clear any existing colors
  pal.empty();
  
  // choose one of 3 palettes and initialize it
  int choice=(int)random(3);

  if(choice==0) {
    pal.addRange((int)random(8, 25), "F8F8F8", "D9D3CE"); 
    pal.addRange((int)random(8, 16), "FF0000", "FF8800"); 
    pal.addRange((int)random(3, 6), "FF0000", "990000"); 
    pal.addRange((int)random(3, 8), "FF8800", "FFCC00"); 
    pal.addRange((int)random(3, 12), "000000", "454545");   
  }
  else if(choice==1) {
    // FFFFFF E3E3E3 FFFF00 FF6800 FF5000 FF0000 008B00 61CD00
    pal.addRange((int)random(10, 17), "F8F8F8", "E3E3E3"); // 0
    pal.addRange((int)random(6, 10), "FFFF00", "FF6800"); // 1
    pal.addRange((int)random(6, 10), "FF5000", "FF0000"); // 2
    pal.addRange((int)random(6, 10), "008B00", "61CD00"); // 3
  }
  else if(choice==2) {
    pal.addRange((int)random(10, 17), "F8F8F8", "E7E7E7");
    pal.addRange((int)random(5, 12), "FF0061", "FF80E1"); 
    if (random(100)>40) pal.addRange((int)random(5, 12), "FFFF00", "FF6800");
    if (random(100)>40) pal.addRange((int)random(5, 12), "FF0031", "DA0028");
    if (random(100)>40) pal.addRange((int)random(5, 12), "FF4100", "FF8800");
  } 
  
  // set random colors for the objects
  for(int i=0; i<num; i++) 
    col[i]=pal.getRandomCol();
}

void initComposition() {
  for(int i=0; i<num; i++) {
    x[i]=random(width);
    y[i]=random(height);
    rad[i]=random(20,50);
    if(random(100)>90) rad[i]=rad[i]*4;
  }
  
  initColors();
}

void mousePressed() {
  if(mouseButton==RIGHT) initColors();
  else initComposition();  
}

