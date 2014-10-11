import java.awt.image.*;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.gui.*;
import com.pearsoneduc.ip.op.OperationException;

public class QuantisationSimulator extends ImageSelector {

  public QuantisationSimulator(String imageFile)
   throws IOException, ImageDecoderException, OperationException {
    super(imageFile);
  }

  // This checks for RGB image
  public boolean imageOK() {
    return getSourceImage().getType() == BufferedImage.TYPE_BYTE_GRAY;
  }

  // Requantises the image
  public BufferedImage quantiseImage(int numBits) {
    int n = 8 - numBits;
    float scale = 255.0f / (255 >> n);
    byte[] tableData = new byte[256];
    for (int i = 0; i < 256; ++i) {
      tableData[i] = (byte) Math.round(scale*(i >> n));
      if(n == 5) {
      System.out.println(tableData[i] + "," + i + ", " + n);
      }
    }
    LookupOp lookup =
     new LookupOp(new ByteLookupTable(0, tableData), null);
    BufferedImage result = lookup.filter(getSourceImage(), null);
    return result;
  }

  // Creates versions of the image simulating quantisation with fewer bits
  public Vector generateImages() {
    Vector quantisation = new Vector();
    int width = getSourceImage().getWidth();
    int height = getSourceImage().getHeight();

    int levels = 2;
    for (int n = 1; n < 8; ++n, levels *= 2) {
      String key = Integer.toString(levels) + " levels";
      quantisation.addElement(key);
      addImage(key, new ImageIcon(quantiseImage(n)));
    }

    quantisation.addElement("256 levels");
    addImage("256 levels", new ImageIcon(getSourceImage()));

    return quantisation;
  }

  public static void main(String[] argv) {
    if (argv.length > 0) {
      try {
        JFrame frame = new QuantisationSimulator(argv[0]);
        frame.pack();
        frame.setVisible(true);
      }
      catch (Exception e) {
  System.err.println(e);
  System.exit(1);
      }
    }
    else {
      System.err.println("java QuantisationSimulator <imagefile>");
      System.exit(1);
    }
  }
}