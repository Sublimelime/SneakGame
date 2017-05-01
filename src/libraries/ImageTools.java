package libraries;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageTools {

    public static int HORIZONTAL_FLIP = 1, VERTICAL_FLIP = 2, DOUBLE_FLIP = 3;

    /**
     * Loads an image.
     *
     * @param fileName The name of a file with an image
     * @return Returns the loaded image. null is returned if the image cannot be loaded.
     */
    public static BufferedImage load(String fileName) {
        try {
            return ImageIO.read((new File(fileName)));
        } catch (IOException e) {
            System.err.println("Unable to read file...");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Copies and returns an image.
     *
     * @param img Receives a buffered image
     * @return Returns a copy of the received image. null is returned if the received image is null.
     */
    public static BufferedImage copy(BufferedImage img) {
        if (img == null) {
            return null;
        }
        ColorModel cm = img.getColorModel();
        WritableRaster raster = img.copyData(null);
        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
    }

    /**
     * Returns a new image with transparency enabled.
     *
     * @param img Receives a buffered image
     * @return returns a copy of the received image with a color mode that allows transparency.
     * null is returned if the received image is null.
     */
    public static BufferedImage copyWithTransparency(BufferedImage img) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics tempG = temp.getGraphics();
        tempG.drawImage(img, 0, 0, null);
        return temp;
    }

    /**
     * Checks if the provided image has transparency.
     *
     * @param img Receives a buffered image
     * @return returns true if the image has a transparent mode and false otherwise.
     */
    public static boolean hasTransparency(BufferedImage img) {
        return img.getColorModel().hasAlpha();
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image and two positive double scale values (percentages)
     * @param horizontalScale Value to scale horizontal by.
     * @param verticalScale Value to scale vertical by.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive scales are provided
     */
    public static BufferedImage scale(BufferedImage img, double horizontalScale,
            double verticalScale) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = temp.createGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(horizontalScale, verticalScale);

        tempG.drawImage(img, xform, null);
        return temp;
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image
     * @param newWidth New width to scale to.
     * @param newHeight New height to scale to.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive dimensions are provided
     */
    public static BufferedImage scale(BufferedImage img, int newWidth,
            int newHeight) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = temp.createGraphics();
        AffineTransform xform = new AffineTransform();
        double currentWidth = img.getWidth(), currentHeight = img.getHeight();
        xform.setToScale(newWidth / currentWidth, newHeight / currentHeight);
        tempG.drawImage(img, xform, null);
        return temp;
    }

    /**
     * Rotates an image.
     *
     * @param img Receives a buffered image
     * @param angle The angle to rotate to.
     * @return The rotated image. null is returned if the received image is null.
     */
    public static BufferedImage rotate(BufferedImage img, double angle) {
        if (img == null) {
            return null;
        }
        angle = angle % 360;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(0, 0);
        affineTransform.rotate(Math.toRadians(angle), img.getWidth() / 2, img.getHeight() / 2);
        BufferedImage rotated = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
        Graphics2D g = (Graphics2D) (rotated.getGraphics());
        g.drawImage(img, affineTransform, null);
        return rotated;
    }

    /**
     * Flips an image.
     *
     * @param img Receives a buffered image
     * @param type Type of flip (int)
     * @return Creates and returns a flipped copy of the received image.
     * null is returned if the received image is null or if an invalid flipping value is provided
     */
    public static BufferedImage flip(BufferedImage img, int type) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics tempG = temp.getGraphics();
        if (type == HORIZONTAL_FLIP) {
            tempG.drawImage(img, 0 + img.getWidth(), 0, -img.getWidth(), img.getHeight(), null);
            return temp;
        } else if (type == VERTICAL_FLIP) {
            tempG.drawImage(img, 0, img.getHeight(), img.getWidth(), -img.getHeight(), null);
            return temp;
        } else if (type == DOUBLE_FLIP) {
            return rotate(img, 180);
        } else {
            return null;
        }
    }

    /**
     * Blurs an image.
     *
     * @param img Receives a buffered image
     * @return creates and returns a blurred copy of the received image,
     * the blurring is created by blending each cell with its 8 neighbors.
     * Null is returned if the received image is null.
     */
    public static BufferedImage blur(BufferedImage img) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        temp.getGraphics().drawImage(img, 0, 0, null);
        //go through every other pixel, get averages of all 8 surrounding
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                temp.setRGB(x, y, getAverageForLocation(img, x, y));
            }
        }

        return temp;
    }

    /**
     * Gets the average color of a pixel location.
     *
     * @param img The buffered image to operate on.
     * @param x Starting x location
     * @param y Starting y location
     * @return Average color of all 8 surrounding pixels.
     */
    private static int getAverageForLocation(BufferedImage img, int x, int y) {
        int total = 0;
        int count = 0;
        int red = 0, blue = 0, green = 0;
        Color c = new Color(0, 0, 0);

        for (int y2 = y - 1; y2 <= y + 1; y2++) {
            for (int x2 = x - 1; x2 <= x + 1; x2++) {
                if (y2 < 0 || y2 >= img.getHeight() || x2 < 0 || x2 >= img.getWidth()) {
                    //invalid loc
                } else {
                    c = new Color(img.getRGB(x2, y2), true);
                    red += c.getRed();
                    blue += c.getBlue();
                    green += c.getGreen();

                    count++;
                }
            }
        }
        Color colorToReturn = new Color(red / count, green / count, blue / count, c.getAlpha());
        return colorToReturn.getRGB();
    }

    /**
     * Inverts an image's colors.
     *
     * @param img Receives a buffered image
     * @return Image with inverted colors. null is returned if the received image is null.
     */
    public static BufferedImage invertColor(BufferedImage img) {
        if (img == null) {
            return null;
        }
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int inverted;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                inverted = (0x00FFFFFF - (rgb | 0xFF000000)) | (rgb & 0xFF000000);
                temp.setRGB(x, y, inverted);
            }
        }
        return temp;
    }

    /**
     * Removes a certain percentage of an image's pixels.
     *
     * @param img Receives a buffered image
     * @param percentToRemove Percent to remove of the image.
     * @return creates and returns a copy of the received image with the given
     * percentage in decimal form of the images remaining non-fully transparent
     * pixels changed to be completely transparent. null is returned if the
     * received image is null or if non-positive percentage is provided.
     */
    public static BufferedImage removePixels(BufferedImage img, double percentToRemove) {
        if (img == null || percentToRemove < 0) {
            return null;
        } else if (percentToRemove > 1) {
            percentToRemove = 1;
        }
        BufferedImage temp = copy(img);
        long quantityOfPixelsTotal = img.getWidth() * img.getHeight();
        int quantityOfPixelsNotInvisible = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (((img.getRGB(x, y) >> 24) & 0xFF) > 0) { //not invisible
                    quantityOfPixelsNotInvisible++;
                }
            }
        }

        int countRemoved = 0;
        int x, y, red, blue, green, alpha, rgb;
        while (countRemoved <= quantityOfPixelsNotInvisible * percentToRemove) {
            do { //find a non-transparent pixel
                x = (int) (Math.random() * img.getWidth());
                y = (int) (Math.random() * img.getHeight());
                rgb = img.getRGB(x, y);
                alpha = (rgb >> 24 & 0xFF);
            } while (alpha == 0);
            blue = rgb & 0xFF;
            green = (rgb >> 8) & 0xFF;
            red = (rgb >> 16) & 0xFF;

            Color newColor = new Color(red, green, blue, 0);
            temp.setRGB(x, y, newColor.getRGB());
            countRemoved++;
        }

        return temp;
    }

    /**
     * Removes a certain amount of pixels from an image.
     *
     * @param img Receives a buffered image
     * @param numToRemove number of pixels to remove
     * @return creates and returns a copy of the received image with the given
     * number of the images remaining pixels removed.
     * Non-fully transparent pixels are changed to be completely transparent.
     * null is returned if the received image is null or if non-positive number
     * is provided. Note: is there are not enough pixels in the image it will
     * remove as many as it can.
     */
    public static BufferedImage removePixels(BufferedImage img, int numToRemove) {
        if (img == null || numToRemove < 0) {
            return null;
        }
        //figures out what percentage of the total pixels in the image is numToRemove, calls percent removePixels
        return removePixels(img, (double) numToRemove / (img.getHeight() * img.getWidth()));
    }

    /**
     * Fades an image.
     *
     * @param img Receives a buffered image
     * @param fade Double percentage to fade
     * @return Creates and returns a copy of the received image that has been
     * faded the given percentage. Fading is done by multiply the alpha value by (1-fade)
     * Null is returned if the received image is null or if non-positive fade value is provided
     * Note: any fade greater than 1 will be reduced to 1
     */
    public static BufferedImage fade(BufferedImage img, double fade) {
        if (img == null) {
            return null;
        }
        int alpha, red, blue, green;
        BufferedImage temp = copy(img);
        //go through every pixel in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                alpha = (rgb >> 24 & 0xFF);
                blue = rgb & 0xFF;
                green = (rgb >> 8) & 0xFF;
                red = (rgb >> 16) & 0xFF;

                alpha *= (1 - fade);

                temp.setRGB(x, y, ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16)
                        | ((green & 0xFF) << 8) | (blue & 0xFF));
            }
        }

        return temp;
    }

    /**
     * Lightens an image.
     *
     * @param img Receives a buffered image
     * @param lightenFactor double percentage to lighten
     * @return creates and returns a copy of the received image that has been
     * lightened by the given percentage + 1.
     * Null is returned if the received image is null or if non-positive lighten.
     * Factor value is provided.
     * Note: any colors that end up being larger than 255 will be changed to 255.
     */
    public static BufferedImage lighten(BufferedImage img, double lightenFactor) {
        if (img == null) {
            return null;
        }
        int alpha, red, blue, green;
        BufferedImage temp = copy(img);
        //go through every pixel in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                alpha = (rgb >> 24 & 0xFF);
                blue = rgb & 0xFF;
                green = (rgb >> 8) & 0xFF;
                red = (rgb >> 16) & 0xFF;

                blue *= (1 + lightenFactor);
                red *= (1 + lightenFactor);
                green *= (1 + lightenFactor);
                if (blue > 255) {
                    blue = 255;
                }
                if (green > 255) {
                    green = 255;
                }
                if (red > 255) {
                    red = 255;
                }
                temp.setRGB(x, y, ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16)
                        | ((green & 0xFF) << 8) | (blue & 0xFF));
            }
        }

        return temp;
    }

    /**
     * Darkens an image.
     *
     * @param img Receives a buffered image
     * @param darkenFactor double percentage to darken
     * @return creates and returns a copy of the received image that has been
     * darkened by 1 minus the given percentage.
     * null is returned if the received image is null or if non-positive.
     * Note: any colors that end up being larger than 255 will be
     * changed to 255.
     */
    public static BufferedImage darken(BufferedImage img, double darkenFactor) {
        if (img == null) {
            return null;
        }
        int alpha, red, blue, green, rgb;
        BufferedImage temp = copy(img);
        //go through every pixel in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                rgb = img.getRGB(x, y);
                alpha = (rgb >> 24 & 0xFF);
                blue = rgb & 0xFF;
                green = (rgb >> 8) & 0xFF;
                red = (rgb >> 16) & 0xFF;

                blue *= (1 - darkenFactor);
                red *= (1 - darkenFactor);
                green *= (1 - darkenFactor);
                if (blue < 0) {
                    blue = 0;
                }
                if (green < 0) {
                    green = 0;
                }
                if (red < 0) {
                    red = 0;
                }
                temp.setRGB(x, y, ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16)
                        | ((green & 0xFF) << 8) | (blue & 0xFF));
            }
        }

        return temp;
    }
}
