package ascii_art;

import image.Image;
import image.ImageRenderer;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;

/**
 * An algorithm to convert an image into ASCII art.
 */
public class AsciiArtAlgorithm {
    private final Image image;
    private final SubImgCharMatcher imgCharMatcher;
    private static String imgPath = null;
    private static int resolution = 0;
    private static double[] imageBrightness;
    private boolean didPhotoOrResChange;


    /**
     * Constructs an AsciiArtAlgorithm object.
     *
     * @param image          The input image.
     * @param resolution     The desired resolution of the ASCII art.
     * @param imgCharMatcher The character matcher for mapping image brightness to characters.
     */
    AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher imgCharMatcher, String imgPath) {
        didPhotoOrResChange = false;
        this.image = image;
        this.imgCharMatcher = imgCharMatcher;
        if (resolution != AsciiArtAlgorithm.resolution || !AsciiArtAlgorithm.imgPath.equals(imgPath)) {
            AsciiArtAlgorithm.resolution = resolution;
            AsciiArtAlgorithm.imgPath = imgPath;
            didPhotoOrResChange = true;
        }
    }

    /**
     * Runs the ASCII art algorithm.
     *
     * @return A 2D array representing the ASCII art.
     * @throws EmptyCharset if the character set used for matching is empty.
     */
    public char[][] run() throws EmptyCharset {
        if (imgCharMatcher.getCharsetSize() == 0) {
            throw new EmptyCharset();
        }
        if (didPhotoOrResChange) {
            ImageRenderer imageRenderer = new ImageRenderer(image);
            imageRenderer.resizeImage();
            Color[][][] smallImages = imageRenderer.splitToSmallImages(resolution);
            imageBrightness = imageRenderer.calculateImageBrightness(smallImages);
        }
        char[][] asciiImage = new char[resolution][resolution];
        for (int i = 0; i < resolution * resolution; i++) {
            asciiImage[i / resolution][i % resolution] =
                    imgCharMatcher.getCharByImageBrightness(imageBrightness[i]);
        }
        return asciiImage;
    }
}
