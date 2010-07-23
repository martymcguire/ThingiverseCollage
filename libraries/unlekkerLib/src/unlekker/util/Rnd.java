package unlekker.util;

import java.lang.*;
import java.lang.reflect.*;
import java.io.Serializable;
import ec.util.*;

/**
 * <p><code>unlekker.util.Rnd</code> provides an alternative to java.util.rand by wrapping ec.util.MersenneTwisterFast in a Processing-friendly convenience class. </p>
 * <p><A HREF="http://www.math.keio.ac.jp/matumoto/ehtm">Mersenne Twister</A> is an advanced pseudo-random number generator with a period of 2^19937-1. The code 
 * used here is from Sean Luke, and is part of his <a href="http://cs.gmu.edu/~eclab/projects/ecj/">ECJ Evolutionary Computation Research System.</a></p>
 *
 * @usage Web & Application
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 */

public class Rnd extends MersenneTwisterFast implements Serializable {
  private static long seedCnt;
  private long seed;

    /**
      * Initialize rand number generator.
      * When called the first time the seed 0 will be used. An internal static variable is then incremented, so that the next instance will have seed 1 etc.
  */

    public Rnd() {
    	super();
    	setSeed(seedCnt);
      seedCnt++;
    }

    public Rnd(long seed) {
    	super(seed);
    }
    
/*    public static void replaceRNG(Object p) {
    	try {
				Class cl=p.getClass();
				Method m=cl.getDeclaredMethod("rand", new Class[] {Float.class,Float.class});
				
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }
*/
    /**
     * Randomly returns true or false.
     * @related rand ( )
     * @related randomInt ( )
     * @related randProb( )
     * @return boolean
     *
     **/

    public boolean randBool() {
    	return nextBoolean();
    }

    /**
     * Returns true if rndFloat(100) returns a result greater than the parameter "chance".
     * @param chance double
     * @related rand ( )
     * @related randomInt ( )
     * @related randBool ( )
     * @return boolean
     */

    public boolean randProb(double chance) {
      return nextBoolean(chance);
    }

    /**
     * Returns a rand value in the ranges [0..1&gt;, [0..range&gt; or [min..max&gt; depending on the version used.
     * @related randomInt ( )
     * @related randBool ( )
     * @related randProb ( )
     * @return float
     * */

    public float rand() {
      return nextFloat();
    }

    public float rand(float range) {
      return range*nextFloat();
    }

    public float rand(float min,float max) {
      return (max-min)*nextFloat()+min;
    }

    /**
     * Returns a rand integer value in the range [0..range-1&gt; or [min..max-1&gt;.
     * @param range 
     * @related rand ( )
     * @related randBool ( )
     * @related randProb ( )
     * @return int
     */

    public int randInt(int range) {
    	return nextInt(range);
    }

    public int randInt(int min,int max) {
      return nextInt(max-min)*+min;
    }

}
