package uilibrary.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(ImageLoader.class.getResource(path)); //If it doesnt work, might need to build the project
			//return ImageIO.read(getClass().getResource(path)); //works if non-static
			//return ImageIO.read(new File(path)); //dont work (has to be static)
		} catch (IOException e) {
			System.out.println("IOException " + e + ", " + path);
		} catch (NullPointerException e) {
			System.out.println("Couldn't get resource. Path was null " + e + ", " + path);
		} catch (IllegalArgumentException e) {
			System.out.println("Couldn't find the resource (resource was null) " + e + ", " + path);
		}

		return null;
	}
	
	public static BufferedImage loadImage(String path, boolean absolutePath) {
		try {
			if (absolutePath) {
				return ImageIO.read(new File(path));
			} else {
				return loadImage(path);
			}
		} catch (IOException e) {
			System.out.println("IOException " + e + ", " + path);
		}
		
		return null;
	}
	
	public static void saveImage(BufferedImage img, String path, String ext) {
		try {
			File outputfile = new File(path);
			ImageIO.write(img, ext, outputfile);
		} catch (IOException e) {
			System.out.println("IOException " + e + ", " + path);
		}
	}
	
	/**
	 * Gets a subimage of full sprite sheet image.
	 * @param path Path of full image
	 * @param xGrid n'th subimage in x direction
	 * @param yGrid n'th subimage in y direction
	 * @param xSize tile size x direction
	 * @param ySize tile size y direction
	 * @return 
	 */
	public static BufferedImage getSprite(String path, int xGrid, int yGrid, int xSize, int ySize) {
		BufferedImage image = loadImage(path);

		return getSprite(image, xGrid, yGrid, xSize, ySize);
	}
	
	/**
	 * Gets a subimage of full sprite sheet image.
	 * @param image Image
	 * @param xGrid n'th subimage in x direction
	 * @param yGrid n'th subimage in y direction
	 * @param xSize tile size x direction
	 * @param ySize tile size y direction
	 * @return 
	 */
	public static BufferedImage getSprite(BufferedImage image, int xGrid, int yGrid, int xSize, int ySize) {
		return image.getSubimage(xGrid * xSize, yGrid * ySize, xSize, ySize);
	}
	
	/**
	 * Gets list of frames from bigger image.
	 * @param path Path to image
	 * @param xGridSize Frame width
	 * @param yGridSize Frame height
	 * @param xAmount Number of frames to include horizontally
	 * @param yAmount Number of frames to include vertically
	 * @return list of frames
	 */
	public static ArrayList<BufferedImage> getListOfFrames(String path, int xGridSize, int yGridSize, int xAmount, int yAmount) {
		return getListOfFramesFrom(path, xGridSize, yGridSize, 0, 0, xAmount, yAmount);
	}
	
	/**
	 * Gets list of frames from bigger image.
	 * @param image Image
	 * @param xGridSize Frame width
	 * @param yGridSize Frame height
	 * @param xAmount Number of frames to include horizontally
	 * @param yAmount Number of frames to include vertically
	 * @return list of frames
	 */
	public static ArrayList<BufferedImage> getListOfFrames(BufferedImage image, int xGridSize, int yGridSize, int xAmount, int yAmount) {
		return getListOfFramesFrom(image, xGridSize, yGridSize, 0, 0, xAmount, yAmount);
	}
	
	/**
	 * Gets list of all the frames in an image.
	 * Doesn't require width and height, but will split the whole image in parts.
	 * @param path Path to image
	 * @param xAmount How many frames does the whole image have horizontally
	 * @param yAmount How many frames does the whole image have vertically 
	 * @return 
	 */
	public static ArrayList<BufferedImage> getListOfAllFrames(String path, int xAmount, int yAmount) {
		BufferedImage image = loadImage(path);
		return getListOfAllFrames(image, xAmount, yAmount);
	}
	
	/**
	 * Gets list of all the frames in an image.
	 * Doesn't require width and height, but will split the whole image in parts.
	 * @param image Image
	 * @param xAmount How many frames does the whole image have horizontally
	 * @param yAmount How many frames does the whole image have vertically 
	 * @return 
	 */
	public static ArrayList<BufferedImage> getListOfAllFrames(BufferedImage image, int xAmount, int yAmount) {
		return getListOfFramesFrom(image, image.getWidth() / xAmount, image.getHeight() / yAmount, 0, 0, xAmount, yAmount);
	}
	
	/**
	 * Gets list of frames from bigger image.
	 * Order of the images will be from top left to right, and then one down and right etc.
	 * @param path Path to image
	 * @param xGridSize Frame width
	 * @param yGridSize Frame height
	 * @param xGridSkip How many grid widths to skip
	 * @param yGridSkip How many grid heights to skip
	 * @param xAmount Number of frames to include horizontally
	 * @param yAmount Number of frames to include vertically
	 * @return list of frames
	 */
	public static ArrayList<BufferedImage> getListOfFramesFrom(String path, int xGridSize, int yGridSize, int xGridSkip, int yGridSkip, int xAmount, int yAmount) {
		BufferedImage image = loadImage(path);
		return getListOfFramesFrom(image, xGridSize, yGridSize, xGridSkip, yGridSkip, xAmount, yAmount);
	}
	
	/**
	 * Gets list of frames from bigger image.
	 * Order of the images will be from top left to right, and then one down and right etc.
	 * @param image Image
	 * @param xGridSize Frame width
	 * @param yGridSize Frame height
	 * @param xGridSkip How many grid widths to skip
	 * @param yGridSkip How many grid heights to skip
	 * @param xAmount Number of frames to include horizontally
	 * @param yAmount Number of frames to include vertically
	 * @return list of frames
	 */
	public static ArrayList<BufferedImage> getListOfFramesFrom(BufferedImage image, int xGridSize, int yGridSize, int xGridSkip, int yGridSkip, int xAmount, int yAmount) {
		ArrayList<BufferedImage> frames = new ArrayList<>();
		for (int y = yGridSkip; y * yGridSize < (yAmount + yGridSkip) * yGridSize; y++) {
			for (int x = xGridSkip; x * xGridSize < (xAmount + xGridSkip) * xGridSize; x++) {
				frames.add(getSprite(image, x, y, xGridSize, yGridSize));
			}
		}
		return frames;
	}
}
