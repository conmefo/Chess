package main;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.IOException;

public class ArcadeFontDemo {

    /**
     * Loads a font from the classpath resources.
     *
     * @param resourcePath The path to the font file within the resources folder
     *                     (e.g., "/fonts/MyArcadeFont.ttf"). Must start with '/'.
     * @param size         The desired font size.
     * @return The loaded Font object, or a default font if loading fails.
     */
    public static Font loadFont(String resourcePath, float size) {
        Font customFont = null;
        InputStream is = null;
        try {
            // Get the input stream for the font resource
            // Using ArcadeFontDemo.class is fine, or use getClass() if in non-static context
            is = ArcadeFontDemo.class.getResourceAsStream(resourcePath);

            if (is == null) {
                // CRITICAL: Resource not found!
                System.err.println("Error: Font resource not found at path: " + resourcePath);
                System.err.println("Checklist:");
                System.err.println("1. Is the font file really at 'src/main/resources" + resourcePath + "'?");
                System.err.println("2. Is the path string EXACTLY correct (case-sensitive, starts with '/')?");
                System.err.println("3. Did Maven build correctly (check 'target/classes" + resourcePath + "')?");
                // Return a default font to prevent NullPointerException later
                return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
            }

            // Create the font from the stream
            // Use Font.TRUETYPE_FONT for .ttf files
            // Use Font.OPENTYPE_FONT for .otf files
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);

            // Register the font with the graphics environment
            // This makes it potentially available by name, but using the Font object directly is safer
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            // IMPORTANT: Derive the font with the specified size.
            // createFont usually returns a font with size 1pt.
            return customFont.deriveFont(size);

        } catch (FontFormatException e) {
            System.err.println("Error loading font: Font format is invalid or unsupported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error loading font: Could not read font file (IO Exception).");
            e.printStackTrace();
        } finally {
            // Always close the input stream
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Log or ignore stream closing error
                    e.printStackTrace();
                }
            }
        }

        // Fallback to a default font if loading failed
        System.err.println("Failed to load custom font '" + resourcePath + "', returning default SansSerif.");
        return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
    }
}