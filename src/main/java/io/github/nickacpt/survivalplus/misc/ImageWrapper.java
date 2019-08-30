package io.github.nickacpt.survivalplus.misc;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ImageWrapper {

    private final BufferedImage image;
    private int width;
    private int height;
    private boolean hasAlpha;

    public ImageWrapper(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        hasAlpha = image.getAlphaRaster() != null;
    }

    public void processImage(Consumer<int[]> processColor) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int[] colors = getRGB(x, y);
                processColor.accept(colors);

                setRGB(x, y, colors[0], colors[1], colors[2], colors[3]);
            }
        }
    }

    public int[] getRGB(int x, int y)
    {
        final Object dataElements = image.getRaster().getDataElements(x, y, null);

        final int red = image.getColorModel().getRed(dataElements);
        final int alpha = hasAlpha ? image.getColorModel().getAlpha(dataElements) : 255;
        final int green = image.getColorModel().getGreen(dataElements);
        final int blue = image.getColorModel().getBlue(dataElements);
        return new int[] {alpha, red, green, blue};
    }

    public void setRGB(int x, int y, int alpha, int red, int green, int blue) {
        int argb = blue; // blue

        if (hasAlpha) argb += (alpha << 24); // alpha

        argb += (green << 8); // green
        argb += (red << 16); // red
        image.setRGB(x, y, argb);
    }
}
