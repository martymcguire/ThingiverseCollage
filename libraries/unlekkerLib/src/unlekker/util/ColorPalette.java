package unlekker.util;

import java.awt.Color;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Utility to generate color palettes.
 * 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 *
 */
public class ColorPalette extends ColorUtil {
  public int num, alloc, numSet;
  ColorSet [] set;
  public int c[];
  public static float hsb[];
  Rnd rnd;

  public ColorPalette() {
    num = 0;
    alloc = 100;
    c = new int[alloc];
    set=new ColorSet[20];
  }

  /**
   * Remove any color definitions.
   *
   */
  public void empty() {
    num=0;
  }

  /**
   * Adds ColorSet to the palette. NOT YET FUNCTIONAL
   * 
   * @param _set
   */
  public void addSet(ColorSet _set) {
    if(numSet==set.length) {
      ColorSet [] tmp=new ColorSet[numSet*2];
      System.arraycopy(set,0, tmp,0, numSet);
      set=tmp;
    }
    set[numSet++]=_set;
  }

  /**
   * Initializes palette using a randomly selected ColorSet. NOT YET FUNCTIONAL
   * 
   */
  public void getPalette() {
    num=0;
    int choice=rnd.randInt(numSet);
    set[choice].getPalette(this);
  }

  public void savePalette(String filename) {
    PrintWriter out;
    try{
//      out=new PrintWriter(filename);
      out=new PrintWriter(new FileWriter(filename));
      out.println("// "+filename+" - "+DateTool.calToString(java.util.Calendar.getInstance(),true));
      for(int i=0; i<numSet; i++) out.println(set[i].toString());
      out.flush();
      out.close();
    } catch(Exception ex){
      System.err.println("Failure creating '"+filename+"': "+ex.toString());
      return;
    }
  }

  /**
   * Get random color from the current palette.
   */
  public int getRandomCol() {
    if(rnd==null) return c[(int)Math.floor(Math.random()*num)];
    else return rnd.randInt(num);
  }

  /**
   * Scramble current palette so that colors are in random order.
   */
  public void scramblePalette() {
    int a,b,tmp;


    if(num>2) for(int i=0; i<num*3; i++) {
      a=(int)Math.floor(Math.random()*num);
      do {b=(int)Math.floor(Math.random()*num);} while(a==b);
      tmp=c[a];
      c[a]=c[b];
      c[b]=tmp;
    }
  }

   /**
   *  Add a single color entry
   */
  public void add(int col) {

    // grow array size if necessary
    if (num == alloc) {
      alloc *= 2;
      int tmp[] = new int[alloc];
      for (int i = 0; i < num; i++) tmp[i] = c[i];
      c = tmp;
    }

    c[num] = col;
    num++;
  }

  /**
   *  Add a single color multiple times
   * 
   * @param col Color to add
   * @param numcol Number of copies to add
   */
  public void add(int col, int numcol) {
    for (int i = 0; i < numcol; i++) add(col);
  }

  /**
   *  Add a single color entry
   */
  public void add(float r, float g, float b) {
    add(color(r, g, b));
  }

  /**
   *  Add a single color entry
   */
  public void add(float r, float g, float b, float a) {
    add(color(r, g, b, a));
  }

  /**
   *  Add a single color multiple times from a hexadecimal color string.
   */
  public void add(String hex,int num) {
      for(int i=0; i<num; i++) add(color(hex));
    }

  /**
   *  Add a single color entry from a hexadecimal color string.
   */
  public void add(String hex) {
      add(color(hex));
    }

  /**
   *   Adds an interpolated range of colors.
   * @param numrange Number of interpolated colors to add
   * @param r
   * @param g
   * @param b
   * @param r2
   * @param g2
   * @param b2
   */
  public void addRange(float numrange,
                       float r, float g, float b, float r2, float g2, float b2) {

    float fract;

    r2 = (r2 - r);
    g2 = (g2 - g);
    b2 = (b2 - b);
    for (float i = 0; i < numrange; i++) {
      fract = i / (numrange - 1);
      add(color(r + r2 * fract, g + g2 * fract, b + b2 * fract));
    }
  }

  /**
   *   Adds an interpolated range of colors with alpha.
   * @param numrange Number of interpolated colors to add
   * @param r
   * @param g
   * @param b
   * @param r2
   * @param g2
   * @param b2
   */
  public void addRange(float numrange,
                       float r, float g, float b, float a,
                       float r2, float g2, float b2, float a2) {

    float fract;

    r2 = (r2 - r);
    g2 = (g2 - g);
    b2 = (b2 - b);
    a2 = a2 - a;
    for (float i = 0; i < numrange; i++) {
      fract = i / (numrange - 1);
      add(color(r + r2 * fract, g + g2 * fract, b + b2 * fract, a + a2 * fract));
    }
  }

  /**
   * Adds an interpolated range of colors.

   * @param numrange Number of interpolated colors to add
   * @param c1
   * @param c2
   */
  public void addRange(float numrange, int c1,int c2) {
    addRange(numrange, (c1 >> 16)&0xff,(c1 >> 8)&0xff,(c1&0xff),
             (c2 >> 16)&0xff,(c2 >> 8)&0xff,(c2&0xff));
  }

  /**
   * Adds an interpolated range of colors.
   * 
   * @param numrange Number of interpolated colors to add
   * @param hex1 Color 1 in hexadecimal format
   * @param hex2 Color 2 in hexadecimal format
   */
  public void addRange(float numrange, String hex1,String hex2) {
    int col1,col2;
    col1=color(hex1);
    col2=color(hex2);
//    System.out.println("col1 "+colorToString(col1)+" col 2 "+colorToString(col2));
    addRange(numrange, col1,col2);
  }

}
