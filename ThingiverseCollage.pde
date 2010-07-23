import unlekker.data.*;
import unlekker.geom.*;
import processing.pdf.*;
import processing.dxf.*;

boolean save_to_file = false;
String out_filename = "M.pdf";
String out_type = PDF;

color bg = color(0xBD,0x11,0x00,0xff);

/* "Plain SVG", single path, no grouped objects. */
String svg_file = "M.svg"; color fg = color(0xFF,0xFF,0xFF);

int max_size = 80;
int min_size = 20;
int num_steps = 4;

float max_rotation = PI/8.0;

String[] all_stls = {
                 "basic_foozman.stl", // thing:1090, CC-BY
                 "BotOrnamentLarge.stl", // thing:1371, public domain
                 "beco_block-ball-and-socket-v2.stl", // thing:1983, GPL-CC
                 "beco_block-mhead.stl", // thing:1983, GPL-CC
                 "beco_block-mfmf.stl", // thing:1983, GPL-CC
                 "buckle_male_rev5.stl", // thing:1416, CC-BY-SA
                 "Coat-hook.stl", // thing:49, GPL-CC
                 "CurtainHook.stl", // mine
                 "failwhale.stl", // thing:3487, CC-BY-SA
                 "floss_bow_v1.stl", // mine
                 "funnel.stl", // thing:1234, GPL-CC
                 "gekko.stl", // thing:3378, CC-BY-SA
                 "heart_lid.stl", // thing:61, CC-BY
                 "m5_hex_nut.stl", // mine
                 "maker_fork_v2.stl", // thing:1111, CC-BY-SA
                 "penny_opener_cut.stl", // thing:1842, GPL-CC
                 "puzzle_piece-3.stl", // thing:1136, CC-BY-SA
                 "rice_spoon.stl", // thing:1109, CC-BY-SA
                 "rocket_solid.stl", // thing:3327, CC-BY
                 "smt-tweezers.stl", // thing:741, GPL-CC
                 "spiderspecs_onepiece.stl", // thing:3339, CC-BY-SA
                 "spur_circles.stl", // thing:1336, CC-BY-SA
                 "teapot.stl", // thing:821, public domain
                 "Tetris4.stl", // thing:3440, all rights reserved?!
                 "Tetris5.stl", // thing:3440, all rights reserved?!
                 "towel_hook.stl", // thing:1545, CC-BY-SA
                 "wineglass2.stl", // thing:1224, GPL-CC
                 "whistle_v2.stl", // thing:1046, CC-BY-NC
                };
                
String[] stls = all_stls;

/***||||| Nothing else to configure down here (in theory) |||||***/

PShape svg_shape;
ArrayList<FaceList> polys = new ArrayList<FaceList>();
int off_x;
int off_y;  
int curr_size = max_size;
int count = 0;
PGraphics g_rb;

void setup() {
  svg_shape = loadShape(svg_file);
  println("SVG Size: " + svg_shape.width + ", " + svg_shape.height);
  off_x = round(svg_shape.width * 0.05);
  off_y = round(svg_shape.height * 0.05);
  size(round(svg_shape.width * 1.1), round(svg_shape.height * 1.1), P3D);
  smooth();
  
  // Draw the image onto an offscreen graphic
  g_rb = createGraphics(width, height, JAVA2D);
  g_rb.beginDraw();
  g_rb.background(bg);
  g_rb.shape(svg_shape, off_x, off_y);
  g_rb.endDraw();
  svg_shape = null;
  
  // Load STLs
  for(String stl_file : stls){
    STL stl = new STL(this, "data/" + stl_file);
    FaceList poly = stl.getPolyData();
    poly.normalize(max_size);
    poly.center();
    polys.add(poly);
  }

  if(save_to_file){
    println("Recording to " + out_type + " file '" + out_filename + "'...");
    beginRaw(out_type, out_filename); 
  }
  
  background(bg);
  fill(fg);
  noStroke();
}

void draw() {
  
  if((curr_size >= min_size) && count < 10000){
    FaceList model = polys.get(int(random(polys.size())));
    float r_x = random(max_rotation);
    float r_y = random(max_rotation);
    float r_z = random(TWO_PI);
    PGraphics g = createGraphics(curr_size, curr_size, P3D);
    g.beginDraw();
    g.background(bg);
    g.translate(curr_size/2,curr_size/2);
    g.rotateX(r_x); g.rotateY(r_y); g.rotateZ(r_z);
    g.fill(fg);
    g.noStroke();
    model.normalize(curr_size * 0.5);
    model.draw(g);
    g.endDraw();
    
    boolean penalty = true;
    for(int tries = 0; tries<100 && penalty; tries++){
      boolean fits = true;

      int x = (int)random(width-curr_size);
      int y = (int)random(height-curr_size);
      for (int dx = 0; dx<curr_size && fits; dx++) {
        for (int dy = 0; dy<curr_size && fits; dy++) {
          if (g.get(dx,dy) != bg) {
            if (g_rb.get(x+dx, y+dy) == bg) {
              fits = false; // penalty = false;
              //println("Outside path @ " + dx + ", " + dy);
            } else {
              if(get(x+dx, y+dy) != bg) {
                fits = false;
                //println("Collision @ " + (x+dx) + ", " + y+dy);
              }
            }
          }
        }
      }
    
      if(fits) {
        pushMatrix();
        translate(x + curr_size/2,y+curr_size/2);
        rotateX(r_x); rotateY(r_y); rotateZ(r_z);
        model.draw(this);

//        image(g, 0, 0);
        popMatrix();
        //println("Drawing!");
        count++;
        penalty = false;
      }
    }
    
    if(penalty) {
      curr_size = curr_size - ((max_size - min_size) / num_steps);
      if(curr_size >= min_size){
        //println("Shrinking!");
      } else {
        if(save_to_file){ endRaw(); }
        println("Done!");
      }
    }
  }
  
}
