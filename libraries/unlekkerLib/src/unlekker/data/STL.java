package unlekker.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import processing.core.*;
import unlekker.geom.*;

/**
 * Class to export and import binary STL (stereolithography) files from Processing. 
 * STL files are typically used for rapid prototyping, and consist of simple triangle data with a face normal. 
 * See <a href="http://en.wikipedia.org/wiki/STL_(file_format)">Wikipedia: STL</a> for more info about the format. 
 * <p>Using Processing's <code>beginRaw()</code> mechanism geometry can be output through this class.  
 * STL files can also be loaded into memory.</p>
 * <p>I recommend the excellent Open Source object viewer 
 * <a href="http://meshlab.sourceforge.net/">MeshLab</a> for checking STL models. 
 * It will show inverted normals, invalid polygons and so on. It will even attempt
 * some limited repairs of damaged files.</p> 
 */

public class STL extends PGraphics3D {
	PApplet p;
	
  File file;
  String filename;

  boolean ignoreShape=false,isDisposed=false;  

  float mult=1;
  
	byte [] header,byte4;
	ByteBuffer buf;
	
	FaceList poly;


  /////////////////////////////////////////////
  // CONSTRUCTORS FOR USE WITH BEGINRAW

	/**
	 * Constructor for use with beginRaw().
	 * 
	 */

  public STL() {
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
  // CONSTRUCTOR FOR READING STL DATA FROM DISK

  /**
	 * Constructor for loading STL files. Once loaded, draw() can be used to 
	 * draw the geometry on the canvas.
	 * 
	 * @param filename Name of file to open. 
	 */

  
  public STL(PApplet applet, String path) {
  	super();
  	p=applet;
  	readSTL(path);
  }
  
  public FaceList getPolyData() {
  	return poly;
  }

 

  /////////////////////////////////////////////
  // FUNCTIONS FOR STL INPUT
  
  private void readSTL(String path) {
		header=new byte[80];
		byte4=new byte[4];

    try {
			if (path != null) {
			  filename=path;
			  file = new File(path);
			  if (!file.isAbsolute()) file=new File(p.savePath(path));
			  if (!file.isAbsolute()) 
			    throw new RuntimeException("RawSTLBinary requires an absolute path " +
			    "for the location of the input file.");
			}
			
			FileInputStream in=new FileInputStream(file);
    	System.out.println("\n\nReading "+file.getName());
			
			in.read(header);
			in.read(byte4);
  		buf = ByteBuffer.wrap(byte4);
  		buf.order(ByteOrder.nativeOrder());
  		int num=buf.getInt();
			
			System.out.println("Polygons to read: "+num);

    	header=new byte[50];    	
    	
    	poly=new FaceList(num);

			for(int i=0; i<num; i++) {
				in.read(header);
				buf = ByteBuffer.wrap(header);
	  		buf.order(ByteOrder.nativeOrder());
	  		buf.rewind();
	  		
				poly.addFace(Face.parseFace(buf));
				if(i%1000==0) System.out.println(i+" triangles read.");//f[i]);
			}
			System.out.println("Facets: "+num);

    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  }
    
  
  /////////////////////////////////////////////
  // FUNCTIONS FOR RAW STL OUTPUT
  
  private void initRaw(String path) {
    if (path != null) {
      filename=path;
      file = new File(path);
      if (!file.isAbsolute()) file = null;
    }
    if (file == null) {
      throw new RuntimeException("STL requires an absolute path " +
                                 "for the location of the output file.");
    }

  	poly=new FaceList();
  }

  public void dispose() {
  	if(isDisposed) return;
  	
    try {
    	
    	FileOutputStream out = new FileOutputStream(file);

  		header=new byte[80];

  		buf = ByteBuffer.allocate(200);

  		header=new byte[80];
  		buf.get(header,0,80);
    	out.write(header);
  		buf.rewind();

  		buf.order(ByteOrder.LITTLE_ENDIAN);
  		buf.putInt(poly.num);
  		buf.rewind();
  		buf.get(header,0,4);
    	out.write(header,0,4);
  		buf.rewind();
  		
    	System.out.println("\n\nWriting "+file.getName());

    	buf.clear();
    	header=new byte[50];
    	
			for(int i=0; i<poly.num; i++) {
//	  		System.out.println(i+": "+f[i]);
				buf.rewind();
				poly.f[i].write(buf);
				buf.rewind();
				buf.get(header);
				out.write(header);
			}

			out.flush();
			out.close();
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
    if(!ignoreShape) super.endShape(mode);
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
    	face.vertex(vertices[2][X]*mult,vertices[2][Y]*mult,vertices[2][Z]*mult);
    	face.vertex(vertices[1][X]*mult,vertices[1][Y]*mult,vertices[1][Z]*mult);
    	face.vertex(vertices[0][X]*mult,vertices[0][Y]*mult,vertices[0][Z]*mult);
    	
    	if(Float.isNaN(face.v[0])) {
    		//p.println("Invalid triangle.");
    	}
    	else poly.addFace(face);

      vertexCount = 0;
    }
  }
}
