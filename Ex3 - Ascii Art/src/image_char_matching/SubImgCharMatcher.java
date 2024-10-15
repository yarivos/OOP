/**
 * A utility class for matching characters to image brightness levels.
 */
package image_char_matching;

import java.util.Map;
import java.util.TreeMap;
/**
 * Represents a character matcher used for matching image brightness to ASCII characters.
 * This class manages a character set and provides methods for adding/removing characters,
 * as well as retrieving characters based on image brightness.
 */
public class SubImgCharMatcher {
    // Constants
    private static final int INITIAL_MAX_BRIGHTNESS = 0;
    private static final int INITIAL_MIN_BRIGHTNESS = 1;
    private final int CHAR_PIXEL_RES = 16;

    // Fields
    private double maxBrightness;
    private double minBrightness;
    private final TreeMap<Character, Double> charBrightnessMap;
    private final TreeMap<Double, Character> brightnessCharMap;
    private final TreeMap<Double, Character> normedBrightnessCharMap;
    private boolean needToNorm;

    /**
     * Constructs a SubImgCharMatcher object with the provided charset.
     *
     * @param charset The character set used for matching.
     */
    public SubImgCharMatcher(char[] charset) {
        charBrightnessMap = new TreeMap<Character, Double>();
        brightnessCharMap = new TreeMap<Double, Character>();
        normedBrightnessCharMap = new TreeMap<Double, Character>();
        maxBrightness = INITIAL_MAX_BRIGHTNESS;
        minBrightness = INITIAL_MIN_BRIGHTNESS;
        needToNorm = true;
        for (int i = 0; i < charset.length; i++) {
            addChar(charset[i]);
        }
        normalizeBrightnessCharMap();
    }

    /**
     * Gets the character corresponding to the provided image brightness.
     *
     * @param brightness The brightness level of the image.
     * @return The character matching the brightness level.
     */
    public char getCharByImageBrightness(double brightness) {
        if (needToNorm) {
            // Normalize everything
            normalizeBrightnessCharMap();
            needToNorm = false;
        }
        Map.Entry<Double, Character> ceilingEntry = normedBrightnessCharMap.ceilingEntry(brightness);
        Map.Entry<Double, Character> floorEntry = normedBrightnessCharMap.floorEntry(brightness);
        if (ceilingEntry == null) {
            return floorEntry.getValue();
        }
        if (floorEntry == null) {
            return ceilingEntry.getValue();
        }
        if (Math.abs(floorEntry.getKey() - brightness) <= Math.abs(ceilingEntry.getKey() - brightness)) {
            return floorEntry.getValue();
        } else {
            return ceilingEntry.getValue();
        }
    }

    /**
     * Normalizes the brightness character map.
     */
    private void normalizeBrightnessCharMap() {
        minBrightness = brightnessCharMap.firstKey();
        maxBrightness = brightnessCharMap.lastKey();
        normedBrightnessCharMap.clear();
        for (Map.Entry<Double, Character> entry : brightnessCharMap.entrySet()) {
            normedBrightnessCharMap.put(normalizeBrightness(entry.getKey()), entry.getValue());
        }
    }

    /**
     * Adds a character to the character set.
     *
     * @param c The character to add.
     */
    public void addChar(char c) {
        if (charBrightnessMap.containsKey(c)) return; // If c is already in the set.
        double charBrightness = convertCharToBrightness(c);
        // If min or max brightness, normalize the brightnesses
        checkMinMaxBrightness(charBrightness);
        charBrightnessMap.put(c, charBrightness);
        // If brightness doesn't exist then put and leave
        if (!brightnessCharMap.containsKey(charBrightness)) {
            brightnessCharMap.put(charBrightness, c);
            if (!needToNorm) {
                normedBrightnessCharMap.put(normalizeBrightness(charBrightness), c);
            }
        } else { // Brightness does exist already in different char.
            char insideMapChar = brightnessCharMap.get(charBrightness);
            // If ASCII value of c is lower than the char inside the map already then replace.
            if (c < insideMapChar) {
                brightnessCharMap.replace(charBrightness, c);
                if (!needToNorm) {
                    normedBrightnessCharMap.replace(normalizeBrightness(charBrightness), c);
                }
            }
        }
    }

    /**
     * Removes a character from the character set.
     *
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        if (!charBrightnessMap.containsKey(c)) return; // If c is not in the set.
        // C is in the set
        double charBrightness = convertCharToBrightness(c);
        // Check if c changes max or min brightness.
        if (!checkBrightnessIsMinMax(charBrightness)) {
            normedBrightnessCharMap.remove(normalizeBrightness(charBrightness));
        }
        charBrightnessMap.remove(c);
        brightnessCharMap.remove(charBrightness);
        for (Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()) {
            if (entry.getValue() == charBrightness) {
                brightnessCharMap.put(charBrightness, c);
                return;
            }
        }

    }

    /**
     * Checks if the brightness is the minimum or maximum.
     *
     * @param charBrightness The brightness to check.
     * @return True if the brightness is the minimum or maximum, false otherwise.
     */
    private boolean checkBrightnessIsMinMax(double charBrightness) {
        if (charBrightness == minBrightness || charBrightness == maxBrightness) {
            needToNorm = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if the brightness exceeds the minimum or maximum.
     *
     * @param charBrightness The brightness to check.
     */
    private void checkMinMaxBrightness(double charBrightness) {
        if (charBrightness > maxBrightness || charBrightness < minBrightness) {
            needToNorm = true;
        }
    }

    /**
     * Normalizes the brightness.
     *
     * @param charBrightness The brightness to normalize.
     * @return The normalized brightness.
     */
    private double normalizeBrightness(double charBrightness) {
        return (charBrightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /**
     * Converts a character to brightness.
     *
     * @param c The character to convert.
     * @return The brightness of the character.
     */
    private double convertCharToBrightness(char c) {
        int trueCounter = getCharTrueCounter(c);
        return (double) trueCounter / (CHAR_PIXEL_RES * CHAR_PIXEL_RES);
    }

    /**
     * Gets the true counter of a character.
     *
     * @param c The character to count.
     * @return The count of 'true' values in the character's boolean array representation.
     */
    private int getCharTrueCounter(char c) {
        int trueCounter = 0;
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        for (int i = 0; i < CHAR_PIXEL_RES; i++) {
            for (int j = 0; j < CHAR_PIXEL_RES; j++) {
                if (boolArray[i][j]) trueCounter++;
            }
        }
        return trueCounter;
    }

    /**
     * Gets the character set as a string.
     *
     * @return The character set as a string.
     */
    public String getCharsetAsString() {
        String charString = charBrightnessMap.keySet().toString();
        return charString.substring(1, charString.length() - 1).replace(",", "");
    }

    /**
     * Gets the size of the character set.
     * @return The size of the character set.
     */
    public int getCharsetSize() {
        return charBrightnessMap.size();
    }
}

