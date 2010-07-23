package unlekker.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import processing.core.*;
import unlekker.geom.Face;
import unlekker.geom.FaceList;

/**
 * <p>Class to export POV-Ray files from Processing. Only triangles are supported at the moment.
 * See the <a href="http://http://povray.org/documentation/">POV-Ray documentation</a> for more info about the format.</p> 
 * <p>Mechanisms are provided to use the #declare imperative (beginObject()) and textures (beginTexture()).</p> 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 */

public class POVRay extends PGraphics3D {
	PApplet p;
	
  File file;
  PrintWriter out;
  String filename;
  FaceList poly;

  boolean ignoreShape=false,isDisposed=false;  

	byte [] header,byte4;
	ByteBuffer buf;


  /////////////////////////////////////////////
  // CONSTRUCTORS FOR USE WITH BEGINRAW

	/**
	 * Constructor for use with beginRaw().
	 * 
	 */

  public POVRay() {
  }

  public void setParent(PApplet _p) {
    p=_p;
  }
  
  public void setPath(String path) {
    this.filename = path;
    if (filename != null) {
      file = new File(filename);
      if (!file.isAbsolute()) file = null;
    }
    if (file == null) {
      throw new RuntimeException("An absolute path is required " +
                                 "for the location of the output file.");
    }
  }

  public void setSize(int w, int h) {
    initRaw(filename);  	
  }

    
  /////////////////////////////////////////////
  // FUNCTIONS FOR POVRAY OUTPUT
  
  private void initRaw(String path) {
    if (path != null) {
      filename=path;
      file = new File(path);
      if (!file.isAbsolute()) file = null;
    }
    if (file == null) {
      throw new RuntimeException("POVRay requires an absolute path " +
                                 "for the location of the output file.");
    }
    else {
      try {
        out = new PrintWriter(new FileWriter(file));
        out.println("// '"+filename+"' / Written by unlekker.data.POVRay");
      } catch (IOException e) {
        throw new RuntimeException(e);  // java 1.4+
      }
    }

  	poly=new FaceList();
  }

  /**
   * Outputs a #declare statement declaring a union with name "objname".
   * @param objname
   */
  
  public void beginObject(String objname) {
  	out.println("#declare "+objname+"= union { ");
  }

  /**
   * Ends the object declaration. It will also output a texture with a RGB pigment given by the current fill color. 
   */
  public void endObject() {
    if(fill) {
    	p.println(fillR+" "+fillG+" "+fillB+" "+fillA);
    	beginTexture();
    	texture("pigment {color rgb <"+
    			nf(fillR)+","+
    			nf(fillG)+","+
    			nf(fillB)+">}");
/*    			nf(fillR/255)+","+
    			nf(fillG/255)+","+
    			nf(fillB/255)+">}");*/
    	endTexture();
    }

  	out.println("}");
  }

  /**
   * Declare a named texture. 
   */
  public void beginTexture(String texname) {
  	out.println("#declare "+texname+"= texture { ");  
  }
  
  /**
   * Starts a texture declaration. 
   */
  public void beginTexture() {
  	out.println("  texture {");
  }

  /**
   * Ends the texture declaration. 
   */
  public void endTexture() {
  	out.println("  }");
  }

  /**
   * Write triangle from STLFace object. 
   */
  public void writeTri(Face f) {
  	out.print("triangle {");
  	for(int i=0; i<3; i++) {
  		out.print("<"+nf(f.v[i*3+3])+","+
  				nf(f.v[i*3+4])+","+
  				nf(f.v[i*3+5])+">");
  	}
  	out.println("}");
  }

  /**
   * Ends the object declaration. It will also output a texture with a RGB pigment given by the current fill color. 
   */
  public void texture(String tex) {
  	out.println("  "+tex);
  }

  /////////////////////////////////////////////
  // PGraphics3D methods

  public void dispose() {
  	if(isDisposed) return;
  	
    try {
    	p.println(poly.bb.min.x+" "+poly.bb.max.x+" "+poly.bb.min.y+" "+poly.bb.max.y+" "+poly.bb.min.z+" "+poly.bb.max.z);
    	out.println("#declare CenterX = "+nf(poly.bb.center.x)+";");
    	out.println("#declare CenterY = "+nf(poly.bb.center.y)+";");
    	out.println("#declare CenterZ = "+nf(poly.bb.center.z)+";");

    	
    	float max=poly.bb.getDim();
    	
    	float tx=1/max;
    	float ty=1/max;
    	float tz=1/max;
    	out.println("#declare NormX = "+tx+";");
    	out.println("#declare NormY = "+ty+";");
    	out.println("#declare NormZ = "+tz+";");
    	
    	out.flush();
    	out.close();
    	out=null;
    	isDisposed=true;
    	
	    System.out.println("Closing '"+filename+"'.\n"+poly.num+" polys written.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          

  }
  
  public void beginDraw() {
  	
  }

  public void endDraw() {
  	
  }

  protected void render_lines() {
  }  
  
  public void beginShape(int kind) {
    shape = kind;
    if ((shape != TRIANGLES) &&
        (shape != POLYGON) && (shape!=QUADS)) {
      ignoreShape=true;
    }
    else super.beginShape(kind);

    vertexCount = 0;
  }

  public void endShape(int mode) {
    if(!ignoreShape) {
    	//super.endShape(mode);
    	for(int i=0; i<poly.num; i++) {
    		writeTri(poly.f[i]);
    	}
    }
    ignoreShape=false;
    vertexCount=0;
  }

  public void vertex(float x, float y) {
    if(!ignoreShape) vertex(x, y, 0);
  }

  public void vertex(float x, float y, float z) {
    if(ignoreShape) return;

//    if (vertexCount == vertices.length) {
//      float temp[][] = new float[vertexCount << 1][VERTEX_FIELD_COUNT];
//      System.arraycopy(vertices, 0, temp, 0, vertexCount);
//      vertices = temp;
//      int temp2[] = new int[vertexCount << 1];
//      System.arraycopy(vertex_order, 0, temp2, 0, vertexCount);
//      vertex_order = temp2;
//    }
    float vertex[] = vertices[vertexCount];

    vertex[X] = x;  // note: not mx, my, mz like PGraphics3
    vertex[Y] = y;
    vertex[Z] = z;

    vertexCount++;

    if ((shape == TRIANGLES) && (vertexCount == 3)) {
    	Face face=new Face();
    	face.vertex(vertices[2][X],vertices[2][Y],vertices[2][Z]);
    	face.vertex(vertices[1][X],vertices[1][Y],vertices[1][Z]);
    	face.vertex(vertices[0][X],vertices[0][Y],vertices[0][Z]);
    	
    	if(Float.isNaN(face.v[0])) {
//    		p.println("Invalid triangle.");
    	}
    	else poly.addFace(face);

      vertexCount = 0;
    }
  }
  
  public String nf(float val) {
  	val=(float)((int)(val*100))/100f;
  	return ""+val;
  }
}
