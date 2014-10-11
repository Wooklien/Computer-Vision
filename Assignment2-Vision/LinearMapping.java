/*
Quoc Lien, 816097211
CS559 - Computer Vision
LinearMapping.java
*/

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.Color;

public class LinearMapping {
	private BufferedImage img;

	public LinearMapping(String path, Data3 gain, Data3 biased) {
		try {
		    img = ImageIO.read(new File(path));
		    ImageIO.write(mapping(img, gain, biased), "jpg", new File(gain.r + "," + gain.g + "," + gain.b + "," + biased.r + "," + biased.g + "," + biased.b + "," + path));
		} catch (IOException e) {
			e.printStackTrace();
	    	System.exit(1);	
		}
	}

	public BufferedImage mapping(BufferedImage image, Data3 gain, Data3 biased) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				Color rgb = new Color(image.getRGB(x, y));
				int red = bounded(gain.r * rgb.getRed() + biased.r);
				int green = bounded(gain.g * rgb.getGreen() + biased.g);
				int blue = bounded(gain.b * rgb.getBlue() + biased.b);

				newImage.setRGB(x, y, new Color(red, green, blue).getRGB());
			}
		}
		return newImage;
	}

	public static class Data3 {
		public float r, g, b;
		public Data3(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	public int bounded(float value) {
		return Math.min(Math.max(Math.round(value), 0), 255);
	}

	public static void main(String[] argv) {
		String string, filename;
		Data3 gain, biased;
    if (argv.length < 7) {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Enter <Filename> <float> <float> <float> <float> <float> <float>: ");
    	string = scanner.nextLine();
    	scanner.close();
    	String[] tokenizer = string.split(" ");
    	filename = tokenizer[0];
    	gain = new Data3(Float.parseFloat(tokenizer[1]), Float.parseFloat(tokenizer[2]), Float.parseFloat(tokenizer[3]));
        biased = new Data3(Float.parseFloat(tokenizer[4]), Float.parseFloat(tokenizer[5]), Float.parseFloat(tokenizer[6]));
    	
    }
    else {
    	filename = argv[0];
    	gain = new Data3(Float.parseFloat(argv[1]), Float.parseFloat(argv[2]), Float.parseFloat(argv[3]));
        biased = new Data3(Float.parseFloat(argv[4]), Float.parseFloat(argv[5]), Float.parseFloat(argv[6]));
    }

    LinearMapping mapping = new LinearMapping(filename, gain, biased);
  }
}
