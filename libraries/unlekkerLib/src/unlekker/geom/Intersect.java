package unlekker.geom;

/** Geometry utilities. Intersections, collisions etc. */

public class Intersect {
	/** The point of intersection, if any. */
	public static float intersectX, intersectY;
	
	/** Indicates the result of the last intersection check. */
	public static boolean isIntersecting=false;

	/** Calculates the intersection between two lines.    
	 * 
	 * @return Returns true if there is an intersection, in which case intersectX
	 * and intersectY will contain the actual point of intersection. Returns false 
	 * otherwise. 
	 * 
	 * This code comes from a post on Code & Form, which led to a response 
	 * with a more efficient algorithm taken from Graphics Gems. See
	 * the <a href="http://workshop.evolutionzone.com/2007/09/10/code-2d-line-intersection/">original 
	 * discussion</a>.
	 */
	
	public static boolean intersect(float x1, float y1, float x2, float y2,
			float x3, float y3, float x4, float y4) {
		float a1, a2, b1, b2, c1, c2;
		float r1, r2, r3, r4;
		float denom, offset, num;

		// Compute a1, b1, c1, where line joining points 1 and 2
		// is "a1 x + b1 y + c1 = 0".
		a1=y2-y1;
		b1=x1-x2;
		c1=(x2*y1)-(x1*y2);

		// Compute r3 and r4.
		r3=((a1*x3)+(b1*y3)+c1);
		r4=((a1*x4)+(b1*y4)+c1);

		// Check signs of r3 and r4. If both point 3 and point 4 lie on
		// same side of line 1, the line segments do not intersect.
		if ((r3!=0)&&(r4!=0)&&same_sign(r3, r4)) {
			isIntersecting=false;
			return false;
		}

		// Compute a2, b2, c2
		a2=y4-y3;
		b2=x3-x4;
		c2=(x4*y3)-(x3*y4);

		// Compute r1 and r2
		r1=(a2*x1)+(b2*y1)+c2;
		r2=(a2*x2)+(b2*y2)+c2;

		// Check signs of r1 and r2. If both point 1 and point 2 lie
		// on same side of second line segment, the line segments do
		// not intersect.
		if ((r1!=0)&&(r2!=0)&&(same_sign(r1, r2))) {
			isIntersecting=false;
			return false;
		}

		//Line segments intersect: compute intersection point.
		denom=(a1*b2)-(a2*b1);

		if (denom==0) {
			isIntersecting=false;
			return false;
		}

		if (denom<0) {
			offset=-denom/2;
		} else {
			offset=denom/2;
		}

		// The denom/2 is to get rounding instead of truncating. It
		// is added or subtracted to the numerator, depending upon the
		// sign of the numerator.

		num=(b1*c2)-(b2*c1);
		if (num<0) {
			intersectX=(num-offset)/denom;
		} else {
			intersectX=(num+offset)/denom;
		}

		num=(a2*c1)-(a1*c2);
		if (num<0) {
			intersectY=(num-offset)/denom;
		} else {
			intersectY=(num+offset)/denom;
		}

		// lines_intersect
		isIntersecting=true;
		return true;
	}

	private static boolean same_sign(float a, float b) {
		return ((a*b)>=0);
	}


	/** Calculates whether a point is inside a 2D polygon.
	 * 
	 * @param x,y The point to check.
	 * @param poly Array containing the pairs of X,Y coordinates that define the polygon. Must be in the correct order. 
	 * @return Returns true if the point is inside the polygon. Returns false 
	 * otherwise. 
	 * 
	 * Code is borrowed from this <a href="http://processing.org/discourse/yabb_beta/YaBB.cgi?board=Programs;action=display;num=1189178826">Processing.org 
	 * thread</a>. Thanks to <a href="http://www.robotacid.com/">st33d</a> for the code. 
	 */

	public static boolean insidePolygon(float x,float y,float poly[]) {
		int pnum=poly.length/2;
		int id1,id2;
	  int i, j, c = 0;
	  for (i = 0, j = pnum-1; i < pnum; j = i++) {
	  	id1=i*2;
	  	id2=j*2;
	    if ((((poly[id1+1] <= y) && (y < poly[id2+1])) ||
	     ((poly[id2+1] <= y) && (y < poly[id1+1]))) &&
	     (x < (poly[id2] - poly[id1]) * (y - poly[id1+1]) / (poly[id2+1] - poly[id1+1]) + poly[id1]))
	     c = (c+1)%2;
	  }
	  return c==1;
	}
	

	
	/*
  Determine whether or not the line segment p1,p2
  Intersects the 3 vertex facet bounded by pa,pb,pc
  Return true/false and the intersection point p

  The equation of the line is p = p1 + mu (p2 - p1)
  The equation of the plane is a x + b y + c z + d = 0
                               n.x x + n.y y + n.z z + d = 0
*/
	/*

// From http://local.wasp.uwa.edu.au/~pbourke/geometry/linefacet/
int LineFacet(p1,p2,pa,pb,pc,p)
XYZ p1,p2,pa,pb,pc,*p;
{
   double d;
   double a1,a2,a3;
   double total,denom,mu;
   XYZ n,pa1,pa2,pa3;

   // Calculate the parameters for the plane
   n.x = (pb.y - pa.y)*(pc.z - pa.z) - (pb.z - pa.z)*(pc.y - pa.y);
   n.y = (pb.z - pa.z)*(pc.x - pa.x) - (pb.x - pa.x)*(pc.z - pa.z);
   n.z = (pb.x - pa.x)*(pc.y - pa.y) - (pb.y - pa.y)*(pc.x - pa.x);
   Normalise(&n);
   d = - n.x * pa.x - n.y * pa.y - n.z * pa.z;

   // Calculate the position on the line that intersects the plane 
   denom = n.x * (p2.x - p1.x) + n.y * (p2.y - p1.y) + n.z * (p2.z - p1.z);
   if (ABS(denom) < EPS)         // Line and plane don't intersect 
      return(FALSE);
   mu = - (d + n.x * p1.x + n.y * p1.y + n.z * p1.z) / denom;
   p->x = p1.x + mu * (p2.x - p1.x);
   p->y = p1.y + mu * (p2.y - p1.y);
   p->z = p1.z + mu * (p2.z - p1.z);
   if (mu < 0 || mu > 1)   // Intersection not along line segment
      return(FALSE);

   // Determine whether or not the intersection point is bounded by pa,pb,pc 
   pa1.x = pa.x - p->x;
   pa1.y = pa.y - p->y;
   pa1.z = pa.z - p->z;
   Normalise(&pa1);
   pa2.x = pb.x - p->x;
   pa2.y = pb.y - p->y;
   pa2.z = pb.z - p->z;
   Normalise(&pa2);
   pa3.x = pc.x - p->x;
   pa3.y = pc.y - p->y;
   pa3.z = pc.z - p->z;
   Normalise(&pa3);
   a1 = pa1.x*pa2.x + pa1.y*pa2.y + pa1.z*pa2.z;
   a2 = pa2.x*pa3.x + pa2.y*pa3.y + pa2.z*pa3.z;
   a3 = pa3.x*pa1.x + pa3.y*pa1.y + pa3.z*pa1.z;
   total = (acos(a1) + acos(a2) + acos(a3)) * RTOD;
   if (ABS(total - 360) > EPS)
      return(FALSE);

   return(TRUE);
}*/
	
}
