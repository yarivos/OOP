package image;

import java.awt.*;

/**
 * The ImageRenderer class provides methods to manipulate images, such as resizing and splitting.
 */
public class ImageRenderer {
    // Constants for RGB factors and max RGB value
    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;
    private static final int MAX_RGB_VALUE = 255;

    // Instance variables
    private final Color[][] pixelArray; // Original pixel array
    private Color[][] resizedPixelArray; // Resized pixel array
    private final int width; // Original image width
    private final int height; // Original image height
    private int numberOfSmallImages; // Number of small images after splitting
    private int resizeHeight; // Resized image height
    private int resizeWidth; // Resized image width
    private int smallImgWidth; // Width of a small image
    private int smallImgHeight; // Height of a small image

    /**
     * Constructs an ImageRenderer object with the specified image.
     * @param image The image to be rendered.
     */
    public ImageRenderer(Image image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        pixelArray = new Color[height][width];
        getPixelArray(image);
    }

    /**
     * Populates the pixel array from the given image.
     * @param image The image to extract pixels from.
     */
    private void getPixelArray(Image image) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j] = image.getPixel(i, j);
            }
        }
    }

    /**
     * Resizes the image to the nearest power of two.
     */
    public void resizeImage() {
        resizeWidth = resizeExpoTwo(width);
        resizeHeight = resizeExpoTwo(height);
        if (width == resizeWidth && height == resizeHeight) {
            resizedPixelArray = pixelArray;
            return;
        }
        resizedPixelArray = new Color[resizeHeight][resizeWidth];
        int leftBoundary = (resizeWidth - width) / 2;
        int upBoundary = (resizeHeight - height) / 2;
        int rightBoundary = leftBoundary + width;
        int bottomBoundary = upBoundary + height;
        // Fill resizedPixelArray with original image pixels, padding with white if necessary
        for (int i = 0; i < resizeHeight; i++) {
            for (int j = 0; j < resizeWidth; j++) {
                if (j < leftBoundary || j >= rightBoundary || i < upBoundary || i >= bottomBoundary) {
                    resizedPixelArray[i][j] = Color.WHITE;
                } else {
                    resizedPixelArray[i][j] = pixelArray[i - upBoundary][j - leftBoundary];
                }
            }
        }
    }

    /**
     * Resizes a length to the nearest power of two.
     * @param len The length to be resized.
     * @return The resized length.
     */
    private int resizeExpoTwo(int len) {
        int resizeLength = 2;
        while (resizeLength < len) {
            resizeLength *= 2;
        }
        return resizeLength;
    }

    /**
     * Splits the resized image into small images of specified resolution.
     * @param resolution The resolution for splitting the image.
     * @return A 3D array containing the small images.
     */
    public Color[][][] splitToSmallImages(int resolution) {
        smallImgWidth = resizeWidth / resolution;
        smallImgHeight = resizeHeight / resolution;
        numberOfSmallImages = resolution * resolution;
        Color[][][] smallImages = new Color[numberOfSmallImages][smallImgHeight][smallImgWidth];
        // Split the resized image into small images
        for (int heightIndex = 0; heightIndex < resizeHeight; heightIndex++) {
            for (int widthIndex = 0; widthIndex < resizeWidth; widthIndex++) {
                smallImages[widthIndex / smallImgWidth + resolution * (heightIndex / smallImgHeight)]
                        [heightIndex % smallImgHeight]
                        [widthIndex % smallImgWidth] =
                        resizedPixelArray[heightIndex][widthIndex];
            }
        }
        return smallImages;
    }

    /**
     * Calculates the brightness for each small image.
     * @param smallImages The array of small images.
     * @return An array containing the brightness value for each small image.
     */
    public double[] calculateImageBrightness(Color[][][] smallImages) {
        double[] pixelsRGB = new double[numberOfSmallImages];
        // Calculate brightness for each small image
        for (int pixel = 0; pixel < numberOfSmallImages; pixel++) {
            pixelsRGB[pixel] = 0.0;
            for (int pixelHeight = 0; pixelHeight < smallImgHeight; pixelHeight++) {
                for (int pixelWidth = 0; pixelWidth < smallImgWidth; pixelWidth++) {
                    Color currPixel = smallImages[pixel][pixelHeight][pixelWidth];
                    pixelsRGB[pixel] += currPixel.getRed() * RED_FACTOR +
                            currPixel.getGreen() * GREEN_FACTOR +
                            currPixel.getBlue() * BLUE_FACTOR;
                }
            }
            pixelsRGB[pixel] /= (MAX_RGB_VALUE * smallImgHeight * smallImgWidth);
        }
        return pixelsRGB;
    }
}
