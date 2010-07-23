 package unlekker.geom;


/** 
 * Bounding box for calculating centroids and dimensions of point data. 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
*/

public class BBox {
	public Vec3 min,max,center,dim;
	
	public BBox() {
		min=new Vec3(10000,10000,10000);
		max=new Vec3(-10000,-10000,-10000);
		center=new Vec3();		
		dim=new Vec3();
	}

	/**
	 * Reset bounding box for new calculation.
	 *
	 */
	public void reset() {
		min.set(10000,10000,10000);
		max.set(-10000,-10000,-10000);
		center.set(0,0,0);
	}
	
	/**
	 * Check new point against current bounding box.
	 *
	 */
	public void addPoint(float _x,float _y,float _z) {
		if(_x<min.x) min.x=_x;
		if(_y<min.y) min.y=_y;
		if(_z<min.z) min.z=_z;
		if(_x>max.x) max.x=_x;
		if(_y>max.y) max.y=_y;
		if(_z>max.z) max.z=_z;
	}
	
	public float getDim() {
		dim.set(max);
		dim.sub(min);
				
  	float maxdim=dim.x;
  	if(dim.y>maxdim) maxdim=dim.y;
  	if(dim.z>maxdim) maxdim=dim.z;
  	
  	return maxdim;
	}

  /////////////////////////////////////////////
  // TRANSFORMS

	public void translate(float tx,float ty,float tz) {
		min.add(tx,ty,tz);
		max.add(tx,ty,tz);
		center.add(tx,ty,tz);
	}

	public void scale(float tx,float ty,float tz) {
		min.mult(tx,ty,tz);
		max.mult(tx,ty,tz);
		center.mult(tx,ty,tz);
	}
	
	public String toString() {
		String s="BBox: "+min+" - "+max+" Center: "+center;
		return s;
	}
}
