 package unlekker.geom;

import java.io.*;
import java.nio.ByteBuffer;

/** 
 * A single polygon face. 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
*/

public class Face {
	/** Array containing the face normal and the three vertices making up this face.
	 * v[0..2] is the normal, v[3..5] is the first vertex etc. */
	public float v[];
	public int edgeCnt=0;

	public Face() {
		v=new float[12];
	}

  /////////////////////////////////////////////
  // BUILD FACET VERTEX BY VERTEX

	public void vertex(float _x,float _y,float _z) {
		v[edgeCnt*3+3]=_x;
		v[edgeCnt*3+4]=_y;
		v[edgeCnt*3+5]=_z;

		edgeCnt++;
		
		if(edgeCnt==3) { 
			// calculate normal vector
	  	float x,y,z,vx1,vy1,vz1,vx2,vy2,vz2;
	    vx1=v[6]-v[3];
	    vy1=v[7]-v[4];
	    vz1=v[8]-v[5];
	    vx2=v[9]-v[3];
	    vy2=v[10]-v[4];
	    vz2=v[11]-v[5];

	    x=vy1*vz2-vy2*vz1;
	    y=vz1*vx2-vz2*vx1;
	    z=vx1*vy2-vx2*vy1;

	    float l=(float)Math.sqrt(x*x+y*y+z*z);
	    x/=l;
	    y/=l;
	    z/=l;
	    v[0]=x;
	    v[1]=y;
	    v[2]=z;
		}
	}

  /////////////////////////////////////////////
  // READ FROM FILE

	public static Face parseFace(ByteBuffer buf) throws IOException {
		Face f;

		f=new Face();
		for(int i=0; i<12; i++) f.v[i]=buf.getFloat();

		return f;
	}

  /////////////////////////////////////////////
  // TRANSFORMS

	public void translate(float tx,float ty,float tz) {
		for(int i=0; i<3; i++) {
			v[3+i*3]+=tx;
			v[3+i*3+1]+=ty;
			v[3+i*3+2]+=tz;
		}
	}

	public void scale(float tx,float ty,float tz) {
		for(int i=0; i<3; i++) {
			v[3+i*3]*=tx;
			v[3+i*3+1]*=ty;
			v[3+i*3+2]*=tz;
		}
	}
	
  /////////////////////////////////////////////
  // UTILITY FUNCTIONS

	public void write(ByteBuffer buf) throws IOException {
		for(int i=0; i<12; i++) buf.putFloat(v[i]);
	}

	public String toString() {
		String s="n  ["+v[0]+","+v[1]+","+v[2]+"]";
		s+="\nv1 ["+v[3]+","+v[4]+","+v[4]+"]";
		s+="\nv2 ["+v[6]+","+v[7]+","+v[8]+"]";
		s+="\nv3 ["+v[9]+","+v[10]+","+v[11]+"]";
		return s;
	}
}
