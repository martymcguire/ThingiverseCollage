package unlekker.util;

import java.awt.Color;

public class ColorUtil {
  public static float hsb[];

  public ColorUtil() {
  }

  public static final int colorBlended(float fract,
                       float r, float g, float b,
                       float r2, float g2, float b2) {

    r2 = (r2 - r);
    g2 = (g2 - g);
    b2 = (b2 - b);
    return color(r + r2 * fract, g + g2 * fract, b + b2 * fract);
  }

  public static final int colorAdjust(int c,float bright,float saturation) {
    if(hsb==null) hsb=new float[3];
    Color.RGBtoHSB((c >> 16)&0xff, (c >> 8)&0xff, (c &0xff), hsb);

    hsb[1]=saturation;
    hsb[2]=bright;
    c=Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    return c;
  }

  public static final int colorMult(int c,float m) {
    int cr,cg,cb;
    cr=(int)((float)((c >> 16)&0xff)*m);
    cg=(int)((float)((c >> 8)&0xff)*m);
    cb=(int)((float)((c&0xff))*m);
    if(cr>255) cr=255;
    if(cg>255) cg=255;
    if(cb>255) cb=255;
    return color(cr,cg,cb);
  }

  public static final int colorBlended(float fract,
                       float r, float g, float b, float a,
                       float r2, float g2, float b2, float a2) {


    r2 = (r2 - r);
    g2 = (g2 - g);
    b2 = (b2 - b);
    a2 = a2 - a;
    return color(r + r2 * fract, g + g2 * fract, b + b2 * fract, a + a2 * fract);
  }

  public static final int colorBlended(float fract, String c1,String c2) {
    int col1=color(c1);
    int col2=color(c2);
    return colorBlended(fract, (col1  >> 16)&0xff,(col1  >> 8)&0xff,(col1)&0xff,
                        (col2  >> 16)&0xff,(col2  >> 8)&0xff,(col2)&0xff);
  }

  public static final int color(String hex) {
   return 0xff000000 | Integer.parseInt(hex, 16);
  }

  public static final String colorToString(int c) {
    return("rgba=("+((c >> 16)&0xff)+","+((c >> 8)&0xff)+","+(c&0xff)+","+((c >> 24)&0xff)+")");
  }

  public static String colorToHex(int col) {
    String s=Integer.toHexString((col >> 16)&0xff)+
        Integer.toHexString((col >> 8)&0xff)+
        Integer.toHexString((col)&0xff);
    s=s.toUpperCase();
    return s;
  }

  public static String colorToHex(int r,int g,int b) {
    return colorToHex(color(r,g,b));
  }

  public static final int color(int x, int y, int z, int a) {
    return ( (a & 0xff) << 24) |
        ( (x & 0xff) << 16) | ( (y & 0xff) << 8) | (z & 0xff);
  }

  public static final int color(float x, float y, float z, float a) {
    return ( ( (int) a & 0xff) << 24) |
        ( ( (int) x & 0xff) << 16) | ( ( (int) y & 0xff) << 8) |
        ( (int) z & 0xff);
  }

  public static final int color(int x, int y, int z) {
    return 0xff000000 |
        ( (x & 0xff) << 16) | ( (y & 0xff) << 8) | (z & 0xff);
  }

  public static final int color(float x, float y, float z) {
    return 0xff000000 |
        ( ( (int) x & 0xff) << 16) | ( ( (int) y & 0xff) << 8) |
        ( (int) z & 0xff);
  }

  public static final int getAlpha(int c) {
    return (c >> 24)&0xff;
  }

  public static final int setAlpha(int c, float alpha) {
    return ( ( (int) alpha << 24) | (0x00ffffff & c));
  }

  public static final float getBrightness(int c) {
    if(hsb==null) hsb=new float[3];
    Color.RGBtoHSB((c >> 16)&0xff, (c >> 8)&0xff, (c &0xff), hsb);

    return hsb[2];
  }

  public static final float getSaturation(int c) {
    if(hsb==null) hsb=new float[3];
    Color.RGBtoHSB((c >> 16)&0xff, (c >> 8)&0xff, (c &0xff), hsb);

    return hsb[1];
  }


  public boolean isWhite(int c) {
    return ((c & 0xffffff)==0xffffff);
  }
}
