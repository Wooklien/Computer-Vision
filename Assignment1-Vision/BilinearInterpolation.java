/*
Quoc Lien, 816097211
CS559 - Computer Vision
Histogram.java
*/

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Histogram {
	private BufferedImage img;
	private int m;

	public BilinearInterpolation(String path, int m) {
		try {
		    img = ImageIO.read(new File(path));
		    this.m = m;
		    BufferedImage newImg = scaleImage(m);

		    ImageIcon imgIcon = new ImageIcon(newImg);
		    JLabel picture = new JLabel(imgIcon);
		    JOptionPane.showMessageDialog(null, picture);

		    ImageIO.write(newImg, "png", new File("copy_" + path));
		} catch (IOException e) {
			e.printStackTrace();
	    	System.exit(1);	
		}
	}

	public BufferedImage scaleImage(int m) {
		int w, h, w2, h2;
		// Surrounding pixels;
		w = img.getWidth();
		h = img.getHeight();
		w2 = m * img.getWidth(); 
		h2 = m * img.getHeight(); 
		BufferedImage outputImage = new BufferedImage (w2, h2, 
		img.getType());

		// Ratio between original picture and resized picture 
		float x_ratio = (float) (w)/(w2); 
		float y_ratio = (float) (h)/(h2);
		float dX, dY;

		for (int y = 0; y < h2; y++) {
			for (int x = 0; x < w2; x++) {
				int x2 = (int)(x_ratio * x);
				int y2 = (int)(y_ratio * y);
				dX = (x_ratio * x) - x2;
				dY = (y_ratio * y) - y2;

				Color a = new Color(img.getRGB(x2, y2));

				int x3 = x2;
				if(x2 == w - 1) {
					x3 = x2 - 1;
				}

				Color b = new Color(img.getRGB(x3 + 1, y2));

				int y3 = y2;
				if(y2 == h - 1) {
					y3 = y2 - 1;
				}

				Color c = new Color(img.getRGB(x2, y3 + 1));

				Color d = new Color(img.getRGB(x3 + 1, y3 + 1));

				int alpha = (int)
				((a.getAlpha() * (1 - dX) * (1 - dY)) + 
				(b.getAlpha() * (dX) * (1 - dY)) + 
				(c.getAlpha() * (dY) * (1 - dX)) + 
				(d.getAlpha() * dX * dY));

				int red = (int)
				((a.getRed()*(1 - dX) * (1 - dY)) + 
				(b.getRed() * (dX) * (1 - dY)) + 
				(c.getRed() * (dY) * (1 - dX)) + 
				(d.getRed() * dX * dY));

				int green = (int)
				((a.getGreen()*(1 - dX) * (1 - dY)) + 
				(b.getGreen() * (dX) * (1 - dY)) + 
				(c.getGreen() * (dY) * (1 - dX)) + 
				(d.getGreen() * dX * dY));

				int blue = (int)
				((a.getBlue()*(1 - dX) * (1 - dY)) + 
				(b.getBlue() * (dX) * (1 - dY)) + 
				(c.getBlue() * (dY) * (1 - dX)) + 
				(d.getBlue() * dX * dY));

				outputImage.setRGB(x, y, new Color(red, green, blue, alpha).getRGB());
			}
		}
        		
		return outputImage;
	}

	public static void main(String[] argv) {
    if (argv.length < 2) {
    	System.err.println("java Scaling <imagefile> <scaling factor>");
    	System.exit(1); 
    }

    BilinearInterpolation s = new BilinearInterpolation(argv[0], Integer.parseInt(argv[1]));
  }
}