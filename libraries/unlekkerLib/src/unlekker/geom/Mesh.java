 package unlekker.geom;

import java.io.*;
import java.nio.ByteBuffer;

/** 
 * Class for representing a polygon mesh. Takes care of unifying vertices and calculating normals.  
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
*/

public class Mesh {
  /** Current number of polygons */
  public int num;
  /** Array of STLFace objects comprising the current geometry. */
  public Face f[];
  public Vec3 v[],n[];
  

  /** 
   * Bounding box
   */
  public BBox bb; 
  
  public Mesh() {
  	f=new Face[50];
  }
  
  public Mesh(int _numinit) {
  	f=new Face[_numinit];
  }
  
  
  public void addFace(Face face)  {
  	if(num==f.length) {
  		Face [] tmp=new Face[num*2];
  		System.arraycopy(f, 0, tmp, 0, num);
  		f=tmp;
  	}

  	int id=0;
  	float x,y,z;
  	for(int i=0; i<face.edgeCnt; i++) {
  		x=face.v[id++];
  		y=face.v[id++];
  		z=face.v[id++];

  		bb.addPoint(x, y, z);
  	}

  	
  	f[num++]=face;
  }	
  /////////////////////////////////////////////
  // FUNCTIONS FOR DISPLAY AND TRANSFORM
  
  /**
   * Draws the object.
	 */

  public void draw(processing.core.PApplet p) {
  	p.beginShape(p.TRIANGLES);
  	for(int i=0; i<num; i++) {
  		p.vertex(f[i].v[3], f[i].v[4], f[i].v[5]);
  		p.vertex(f[i].v[6], f[i].v[7], f[i].v[8]);
  		p.vertex(f[i].v[9], f[i].v[10], f[i].v[11]);
  	}
  	p.endShape();
  }

  /**
	 * Calculates the bounding box of the object. 
	 */

  public void calcBounds() {
  	bb.reset();
  	for(int i=0; i<num; i++) {
  		bb.addPoint(f[i].v[3], f[i].v[4],f[i].v[5]);
  		bb.addPoint(f[i].v[6], f[i].v[7],f[i].v[8]);
  		bb.addPoint(f[i].v[9], f[i].v[10],f[i].v[11]);
  	}
  }
  
  /**
	 * Centers the object around the world origin. 
	 */

  public void center() {
  	float tx=(bb.min.x+bb.max.x)/2;
  	float ty=(bb.min.y+bb.max.y)/2;
  	float tz=(bb.min.z+bb.max.z)/2;
  	for(int i=0; i<num; i++) f[i].translate(-tx,-ty,-tz);
  	bb.translate(-tx, -ty, -tz);
  }

  /**
	 * Normalizes the object to an absolute radius. 
	 */

  public void normalize(float m) {
  	float max=bb.max.x-bb.min.x;
  	if(bb.max.y-bb.min.y>max) max=bb.max.y-bb.min.y;
  	if(bb.max.z-bb.min.z>max) max=bb.max.z-bb.min.z;
  	
  	float tx=m/max;
  	float ty=m/max;
  	float tz=m/max;
  	for(int i=0; i<num; i++) f[i].scale(tx,ty,tz);
  	bb.scale(tx, ty, tz);
  }
 
}
