package com.pingwinno.application;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class FrameGrabber {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(FrameGrabber.class.getName());

    public static BufferedImage getFrame(String path, int width, int height) throws org.bytedeco.javacv.FrameGrabber.Exception {
        log.trace("Grabbing frame from {}", path);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(path);
        grabber.start();
        log.trace("started");
        Frame rawFrame = grabber.grab();
        BufferedImage frame = new Java2DFrameConverter().getBufferedImage(rawFrame);
        log.trace("grabbed {}", frame.toString());

        grabber.stop();


        return frame;
    }

}

