package electronicspos.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtil {

    /**
     * Loads an image icon from a file path or classpath.
     * @param path The path to the image (relative to resources or absolute)
     * @return ImageIcon if found, or a default empty icon
     */
    public static ImageIcon load(String path) {
        if (path == null || path.isEmpty()) {
            return new ImageIcon(); // empty icon
        }

        // Try loading from classpath
        URL resource = IconUtil.class.getResource(path);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        // Fallback: try loading from file system
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() <= 0) { // failed to load
            System.err.println("Icon not found: " + path);
            return new ImageIcon();
        }
        return icon;
    }

    /**
     * Loads an icon and scales it to given width/height
     */
    public static ImageIcon load(String path, int width, int height) {
        ImageIcon icon = load(path);
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        return icon;
    }

    // Example usage
    public static void main(String[] args) {
        ImageIcon icon = IconUtil.load("/images/logo.png", 100, 100);
        JOptionPane.showMessageDialog(null, "Icon loaded", "Test", JOptionPane.INFORMATION_MESSAGE, icon);
    }
}