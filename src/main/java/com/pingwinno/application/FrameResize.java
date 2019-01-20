package com.pingwinno.application;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FrameResize {
    public static BufferedImage resize(BufferedImage frame, int newWidth, int newHeight) {
        int oldWidth = frame.getWidth();
        int oldHeight = frame.getHeight();
        BufferedImage resizedFrame = new BufferedImage(newWidth, newWidth, frame.getType());
        Graphics2D drawler = resizedFrame.createGraphics();
        drawler.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        drawler.drawImage(frame, 0, 0, newWidth, newHeight, 0, 0, oldWidth, oldHeight, null);
        drawler.dispose();
        return resizedFrame;
    }
}
