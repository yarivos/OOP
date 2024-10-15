package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * The Shell class provides a command-line interface for generating ASCII art from images.
 * It allows users to interactively modify the settings and produce ASCII representations of images.
 */
public class Shell {
    private static final String DEFAULT_IMAGE_PATH_DOESNT_EXIST = "Default Image Path Doesn't Exist!";
    // Charset for ASCII art generation
    private static final char[] charset = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // Default resolution for ASCII art
    private static final int DEFAULT_RESOLUTION = 128;
    // Default output method
    private static final String DEFAULT_OUTPUT_STREAM = "console";
    // Default image path
    private static final String DEFAULT_IMG_PATH = "cat.jpeg";
    // Default font name for HTML output
    private static final String FONT_NAME = "Courier New";
    // ASCII value for space character
    private static final int SPACE_ASCII_VALUE = 32;
    // Output HTML file name
    private static final String OUT_HTML = "out.html";
    private static final String BOUNDARIES_EXCEED_ERROR_MSG = "Did not change resolution" +
            " due to exceeding boundaries.";
    private static final String WRONG_RES_FORMAT_MSG = "Did not change resolution due to incorrect format.";
    private static final String PROBLEM_WITH_IMAGE_FILE = "Did not execute due to problem with image file.";
    private static final String OUTPUT_FORMAT_ERR_MSG = "Did not change output" +
            " method due to incorrect format.";
    private static final String CHARSET_IS_EMPTY = "Did not execute. Charset is empty.";
    private static final String INCORRECT_COMMAND_ERR = "Did not execute due to incorrect command.";
    private static final String REMOVE_FORMAT_ERR = "Did not remove due to incorrect format.";
    private static final String ADD_FORMAT_ERR = "Did not add due to incorrect format.";

    // Instance variables
    private final SubImgCharMatcher imgCharMatcher;
    private Image image;
    private String outPutStream;
    private int resolution;
    private boolean didPhotoChange;
    private char[][] asciiArtOutput;
    private String imgPath;

    /**
     * Constructs a Shell instance with default settings.
     */
    Shell() {
        // Set default values
        resolution = DEFAULT_RESOLUTION;
        outPutStream = DEFAULT_OUTPUT_STREAM;
        // Initialize SubImgCharMatcher
        imgCharMatcher = new SubImgCharMatcher(charset);
        try {
            // Load default image
            image = new Image(DEFAULT_IMG_PATH);
            imgPath = DEFAULT_IMG_PATH;
        } catch (IOException e) {
            // Print error message if default image path doesn't exist
            System.out.println(DEFAULT_IMAGE_PATH_DOESNT_EXIST);
        }
        // Set flag to indicate photo change
        didPhotoChange = true;
    }

    /**
     * Handles user commands for manipulating ASCII art.
     * Supports operations such as adding/removing characters, changing resolution,
     * changing the input image, changing the output format, and displaying ASCII art.
     * Provides an interactive shell for user interaction.
     *
     * @param input An array containing the user command and its parameters.
     * @throws WrongFormatException      If the command format is incorrect.
     * @throws IOException               If there is an I/O error while executing the command.
     * @throws ResolutionExceedsBoundary If the resolution exceeds predefined limits.
     * @throws EmptyCharset              If the character set for ASCII art generation is empty.
     */
    private void chooseCommand(String[] input) throws WrongFormatException, IOException,
            ResolutionExceedsBoundary, EmptyCharset {
        switch (input[0]) {
        case "chars":
            // Prints the current character set used for ASCII art.
            System.out.println(imgCharMatcher.getCharsetAsString() + " ");
            break;
        case "add":
            // Adds a character to the ASCII character set.
            addChar(input);
            didPhotoChange = true;
            break;
        case "remove":
            // Removes a character from the ASCII character set.
            removeChar(input);
            didPhotoChange = true;
            break;
        case "res":
            // Changes the resolution of the ASCII art.
            changeRes(input);
            didPhotoChange = true;
            break;
        case "image":
            // Changes the input image for ASCII art generation.
            changeImageAscii(input);
            break;
        case "output":
            // Changes the output format of ASCII art.
            changeOutput(input);
            break;
        case "asciiArt":
            // Generates and displays ASCII art.
            asciiArt();
            break;
        default:
            // Throws an exception for incorrect command format.
            throw new WrongFormatException(INCORRECT_COMMAND_ERR);
        }
    }

    /**
     * Changes the input image for generating ASCII art.
     *
     * @param s An array containing the command and the new image path.
     * @throws IOException          If there is a problem with the image file.
     * @throws WrongFormatException If the command format is incorrect.
     */
    private void changeImageAscii(String[] s) throws IOException, WrongFormatException {
        if (s.length == 1) {
            throw new WrongFormatException(PROBLEM_WITH_IMAGE_FILE);
        }
        image = new Image(s[1]);
        imgPath = s[1];
        didPhotoChange = true;
    }

    /**
     * Generates and displays ASCII art based on the current settings.
     * If there has been a change in photo or settings, generates new ASCII art.
     * Otherwise, displays the previously generated ASCII art.
     *
     * @throws EmptyCharset If the character set for ASCII art generation is empty.
     */
    private void asciiArt() throws EmptyCharset {
        if (didPhotoChange) {
            // Generates new ASCII art based on the current settings.
            asciiArtOutput = new AsciiArtAlgorithm(image, resolution, imgCharMatcher, imgPath).run();
            createOutput();
            // Reset photo change flag
            didPhotoChange = false;
        } else {
            // Displays previously generated ASCII art.
            createOutput();
        }
    }

    /**
     * Runs the interactive shell for generating ASCII art.
     * Reads user input and executes corresponding commands until "exit" is entered.
     */
    public void run() {
        while (true) {
            System.out.print(">>> ");
            // Read user input
            String[] input = KeyboardInput.readLine().split(" ", 2);
            // Execute commands based on user input
            if (input[0].equals("exit")) {
                return;
            }
            try {
                chooseCommand(input);
            } catch (IOException e) {
                System.out.println(PROBLEM_WITH_IMAGE_FILE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Adds character(s) to the charset.
     *
     * @param s The string representing the character(s) to add.
     * @throws IOException If an I/O error occurs.
     */
    private void addChar(String[] s) throws WrongFormatException {
        if (s.length == 1) {
            throw new WrongFormatException(ADD_FORMAT_ERR);
        }
        if (s[1].length() == 1) {
            imgCharMatcher.addChar(s[1].charAt(0));
            return;
        }
        if (s[1].equals("all")) {
            for (int i = 32; i < 127; i++) {
                imgCharMatcher.addChar((char) i);
            }
            return;
        }
        if (s[1].equals("space")) {
            imgCharMatcher.addChar((char) SPACE_ASCII_VALUE);
            return;
        }
        if (s[1].length() == 3 && s[1].charAt(1) == '-') {
            AddFromCharTo(s[1]);
            return;
        }
        throw new WrongFormatException(ADD_FORMAT_ERR);
    }

    /**
     * Removes character(s) from the charset.
     *
     * @param s The string representing the character(s) to remove.
     * @throws IOException If an I/O error occurs.
     */
    private void removeChar(String[] s) throws WrongFormatException {
        if (s.length == 1) {
            throw new WrongFormatException(REMOVE_FORMAT_ERR);
        }
        if (s[1].length() == 1) {
            imgCharMatcher.removeChar(s[1].charAt(0));
            return;
        }
        switch (s[1]) {
        case "all":
            for (int i = 32; i < 127; i++) {
                imgCharMatcher.removeChar((char) i);
            }
            return;
        case "space":
            imgCharMatcher.removeChar((char) SPACE_ASCII_VALUE);
            return;
        }
        if (s[1].length() == 3 && s[1].charAt(1) == '-') {
            removeFromCharTo(s[1]);
            return;
        }
        throw new WrongFormatException(REMOVE_FORMAT_ERR);
    }

    /**
     * Changes the resolution of ASCII art.
     *
     * @param s The string representing the new resolution or direction ('up'/'down').
     * @throws IOException               If an I/O error occurs.
     * @throws ResolutionExceedsBoundary If the new resolution exceeds image boundary.
     */
    private void changeRes(String[] s) throws WrongFormatException, ResolutionExceedsBoundary {
        if (s.length == 1) {
            throw new WrongFormatException(WRONG_RES_FORMAT_MSG);
        }
        switch (s[1]) {
        case "up":
            if (image.getWidth() < resolution * 2) {
                throw new ResolutionExceedsBoundary();
            }
            resolution *= 2;
            System.out.printf("Resolution set to %d.\n", resolution);
            return;
        case "down":
            if (checkMinBoundaryExceeds()) {
                throw new ResolutionExceedsBoundary();
            }
            resolution /= 2;
            System.out.printf("Resolution set to %d.\n", resolution);
            return;
        }
        throw new WrongFormatException(WRONG_RES_FORMAT_MSG);
    }

    /**
     * Changes the output method for ASCII art.
     *
     * @param s The string representing the new output method ('html'/'console').
     * @throws IOException If an I/O error occurs.
     */
    private void changeOutput(String[] s) throws WrongFormatException {
        if (s.length == 1) {
            throw new WrongFormatException(OUTPUT_FORMAT_ERR_MSG);
        }
        switch (s[1]) {
        case "html":
            outPutStream = "html";
            return;
        case "console":
            outPutStream = "console";
            return;
        }
        throw new WrongFormatException(OUTPUT_FORMAT_ERR_MSG);
    }

    /**
     * Adds characters from a range to the charset.
     *
     * @param s The string representing the range of characters to add.
     */
    private void AddFromCharTo(String s) {
        char lowChar = s.charAt(0);
        char highChar = s.charAt(2);
        if (highChar < lowChar) {
            char temp = lowChar;
            lowChar = highChar;
            highChar = temp;
        }
        for (int i = lowChar; i <= highChar; i++) {
            imgCharMatcher.addChar((char) i);
        }
    }

    /**
     * Removes characters from a range from the charset.
     *
     * @param s The string representing the range of characters to remove.
     */
    private void removeFromCharTo(String s) {
        char lowChar = s.charAt(0);
        char highChar = s.charAt(2);
        if (highChar < lowChar) {
            char temp = lowChar;
            lowChar = highChar;
            highChar = temp;
        }
        for (int i = lowChar; i <= highChar; i++) {
            imgCharMatcher.removeChar((char) i);
        }
    }

    /**
     * Checks if the minimum boundary of resolution exceeds.
     *
     * @return true if minimum boundary exceeds, false otherwise.
     */
    private boolean checkMinBoundaryExceeds() {
        double minCharsInRow = Math.max(1, image.getWidth() / image.getHeight());
        return minCharsInRow > (double) resolution / 2;
    }

    /**
     * Creates output based on the output stream.
     */
    private void createOutput() {
        switch (outPutStream) {
        case "console":
            new ConsoleAsciiOutput().out(asciiArtOutput);
            break;
        case "html":
            new HtmlAsciiOutput(OUT_HTML, FONT_NAME).out(asciiArtOutput);
        }
    }

    /**
     * The main method to start the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Create instance of Shell and run it
        Shell shell = new Shell();
        shell.run();
    }
}
