package unlekker.util;

public class ColorSet extends ColorUtil {
  public int num;
  public int c[];
  public int bg;
  public String name;
  int alloc;
  public static float hsb[];
  Rnd rnd;

  public ColorSet() {
    num = 0;
    alloc = 100;
    c = new int[alloc*5];
  }

  public void add(int n,int col1,int col2) {
    add(n,n,col1,col2,100);
  }

  public void add(int min,int max,int col1,int col2) {
    add(min,max,col1,col2,100);
  }

  public void add(int min,int max,int col1,int col2, int chance) {
    int pos;

    if(num==alloc) {
      int [] tmp=new int[alloc*2*5];
      System.arraycopy(c,0, tmp,0, num*5);
      c=tmp;
    }

    pos=num*5;
    c[pos++]=chance;
    c[pos++]=min;
    c[pos++]=max;
    c[pos++]=col1;
    c[pos++]=col2;

    num++;
  }

  
  public int[] getPalette(ColorPalette pal) {
    int pos;

    if(pal==null) pal=new ColorPalette();
    pal.empty();

    pos=0;
    for(int i=0; i<num; i++)
      if((c[pos]<100 && rnd.randProb(c[pos])) || c[pos++]>99)
        pal.addRange(rnd.randInt(c[pos++],c[pos++]),c[pos++],c[pos++]);

    return pal.c;
  }

  public String toString() {
    StringBuffer buf;
    int pos;

    buf=new StringBuffer();
    buf.append("\n// Set \n");
    if(name!=null) buf.append("// Set: "+name+"\n");

    pos=0;
    for(int i=0; i<num; i++) {
      for(int j=0; j<3; j++) buf.append(c[pos++]+" ");
      for(int j=0; j<3; j++) buf.append(colorToHex(c[pos++])+" ");
      buf.append('\n');
    }
    return buf.toString();
  }
}
