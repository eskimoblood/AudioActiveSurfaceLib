package audio;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.video.*;


public class Texture {
    private final DampedFFT fft;
    private final PApplet p;
    private PImage tex;
    private int[] pixels;
    int height = 80;
    Capture cam;

    public Texture(PApplet p) {
        this.p = p;
        fft = new DampedFFT(p);
        int width = fft.getWidth();
        pixels = new int[width * height];
        tex = p.createImage(width, height, PConstants.ARGB);
//        startCam();
    }

    private void startCam() {
        String[] cameras = Capture.list();

        if (cameras.length == 0) {
            p.println("There are no cameras available for capture.");
//            exit();
        } else {
            p.println("Available cameras:");
            for (int i = 0; i < cameras.length; i++) {
                p.println(cameras[i]);
            }

            cam = new Capture(p, cameras[0]);
            cam.start();
        }
    }

    public PImage getUpdatedTexture() {
        float[] peaks = fft.eased();
        tex.loadPixels();
        int peakSize = peaks.length;
//        if (cam.available() == true) {
//            cam.read();
//        }
//        cam.loadPixels();
//        if (cam.pixels.length > 0) {

            for (int i = height - 1; i > 0; i--) {
                for (int j = 0; j < peakSize; j++) {
                    int alpha = pixels[(i - 1) * peakSize + j];
                    int position = i * peakSize + j;
//                    int color = cam.pixels[i * cam.height + j * cam.height / 90];
                    tex.pixels[position] = p.color(0, alpha);
                    pixels[position] = alpha;
                }
            }

            for (int i = 0; i < peakSize; i++) {
                float peak = peaks[i];
                int alpha = 255- Math.round(peak * 100);
                pixels[i] = alpha;
                tex.pixels[i] = p.color(0, alpha);
            }
//        }
        tex.updatePixels();
//        fft.render();
        return tex;
    }
}
