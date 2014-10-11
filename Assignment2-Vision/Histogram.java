/*
Quoc Lien, 816097211
CS559 - Computer Vision
Histogram.java
*/

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;

public class Histogram {
	private BufferedImage img;
	private int[] h, eqh;

	public Histogram(String path) {
		h = new int[256];
		eqh = new int[256];
		initArray(h);
		initArray(eqh);

		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
	    	System.exit(1);	
		}
		h = getHistogramData(img);
		drawHistorgram(img);
		drawEQHisto(img, h);
		eqh = equalization(img, h);
		try {
		    ImageIO.write(equalizeImage(img, eqh), "jpg", new File("equalized_" + path));
		} catch (IOException e) {
			e.printStackTrace();
	    	System.exit(2);	
		}
	}

	public void initArray(int[] histo) {
		for(int i = 0; i < 256; i++) {
			histo[i] = 0;
		}
	}

	public int[] getHistogramData(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		int value = 0; 
		int[] temp = new int[256];
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				value = raster.getSample(x,y,0);
				temp[value]++;
			}
		}
		return temp;
	}

	public void drawHistorgram(BufferedImage img) {
		int x = 256 * 4;
		int y = img.getHeight()*3;
		BufferedImage histImg = new BufferedImage(x,y, BufferedImage.TYPE_BYTE_GRAY);

		for(int j = 0; j < y; j++) {
			for(int i = 0; i < x; i++) {
				histImg.setRGB(i,j, new Color(255,255,255).getRGB());
			}
		}

		int index = 0;
		int[] temp = getHistogramData(img); 
		for(int j = y-1; j >= 0; j--) {
			for(int i = 0; i < x; i+=4) {

				if(index < 255) {
					index++;
				}
				else {
					index = 0;
				}

				if(temp[index] > 0) {
					histImg.setRGB(i,j,new Color(0,0,0).getRGB());
					histImg.setRGB(i+1,j,new Color(0,0,0).getRGB());
					temp[index]--;
				}
			}
		}

		try {
			ImageIO.write(histImg, "jpg", new File("HistImg.jpg"));
		}
		catch(IOException e) {
			System.out.println("Cannot Output Historgram Image.");
		}
	}

	public int[] equalization(BufferedImage image, int[] histogram) {
		int[] eqHistogram = new int[256];
		float scale = 255.0f / (image.getWidth() * image.getHeight());
		eqHistogram[0] = (int) scale * histogram[0];
		for(int i = 1; i < 256; ++i) {
			eqHistogram[i] = (int) (eqHistogram[i-1] + Math.round(scale*histogram[i]));
		}
		return eqHistogram;
	}

	public void drawEQHisto(BufferedImage img, int[] histo) {
		int x = 256 * 4;
		int y = img.getHeight()*3;
		BufferedImage histImg = new BufferedImage(x,y, BufferedImage.TYPE_BYTE_GRAY);

		for(int j = 0; j < y; j++) {
			for(int i = 0; i < x; i++) {
				histImg.setRGB(i,j, new Color(255,255,255).getRGB());
			}
		}

		int index = 0;
		int[] temp = equalization(img, histo); 
		for(int j = y-1; j >= 0; j--) {
			for(int i = 0; i < x; i+=4) {

				if(index < 255) {
					index++;
				}
				else {
					index = 0;
				}

				if(temp[index] > 0) {
					histImg.setRGB(i,j,new Color(0,0,0).getRGB());
					histImg.setRGB(i+1,j,new Color(0,0,0).getRGB());
					temp[index]--;
				}
			}
		}

		try {
			ImageIO.write(histImg, "jpg", new File("EqualizedImg.jpg"));
		}
		catch(IOException e) {
			System.out.println("Cannot Output Historgram Image.");
		}
	}

	public void printHistogram(int[] histogram) {
		for(int i = 0; i < 256; i++) {
			System.out.println(histogram[i] + "");
		}
	}

	public BufferedImage equalizeImage(BufferedImage image, int[] eqh) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = newImage.getRaster();
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				raster.setSample(x,y,0,eqh[image.getRaster().getSample(x,y,0)]);
			}
		}
		return newImage;
	}

	public static void main(String[] argv) {
    if (argv.length < 1) {
    	System.err.println("Error: java Histogram <Image_Path>");
    	System.exit(1); 
    }

    Histogram h = new Histogram(argv[0]);
  }
}