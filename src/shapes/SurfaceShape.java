package shapes;

import audio.Texture;
import control.ControlFrame;
import controlP5.ControlElement;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import processing.core.PApplet;
import processing.core.PShape;
import surface.*;
import colorlib.*;

import java.util.Random;

/**
 * Created by andreaskoberle on 10.04.14.
 */

public class SurfaceShape {


    @ControlElement(properties = {"min=1", "max=3000"}, x = 10, y = 10)
    private int scale = 100;
    @ControlElement(properties = {"min=0", "max=100"}, x = 10, y = 30)
    private int steps = 0;
    @ControlElement(properties = {"min=0", "max=5"}, x = 10, y = 50)
    float n1_1 = .5f;
    @ControlElement(properties = {"min=0", "max=5"}, x = 10, y = 70)
    float n1_2 = 1;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 90)
    float n1_3 = 2.5f;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 110)
    int m1_1 = 0;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 130)
    float n2_1 = 3;
    @ControlElement(properties = {"min=0", "max=5"}, x = 10, y = 150)
    float n2_2 = .2f;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 170)
    float n2_3 = 1;
    @ControlElement(properties = {"min=0", "max=100"}, x = 10, y = 190)
    int m2_1 = 0;
    @ControlElement(properties = {"min=0", "max=24"}, x = 10, y = 210)
    float c1 = 4;
    @ControlElement(properties = {"min=-20", "max=20"}, x = 10, y = 240)
    int t2 = 0;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 270)
    float d1 = 0;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 300)
    float d2 = 0;
    @ControlElement(properties = {"min=0", "max=255"}, x = 10, y = 330)
    int hue = 0;

    Surface surface;


    int[] colors;

    private boolean update;
    private PApplet p;
    private Texture texture;
    private PShape shape;
    private Triad tetrad;

    public SurfaceShape(PApplet p, Texture texture) {
        this.p = p;
        this.texture = texture;
        tetrad = new Triad(p);
        surfaceToShape();
        ControlFrame cf = new ControlFrame(this, 400, 0);
        cf.addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent controlEvent) {
                update = true;
            }
        });
    }

    public PShape getShape() {
        if (update) {
            update = false;
            surfaceToShape();
        }
        return shape;
    }

    int[] getColors() {
        p.colorMode(p.HSB);
        Random rand = new Random();
        int color = rand.nextInt(255);
        p.println(color);
        tetrad.setColor(p.color(hue, 255, 255));
        p.colorMode(p.RGB);
        int colorCount = 10;
        Gradient g = new Gradient(this.p);
        int[] tColors = new int[3];
        for (int i = 0; i < 3; i++) {
            tColors[i] = tetrad.getColor(i);
        }
        g.setColors(tColors);
        g.setSteps(colorCount);
        int[] colors = new int[colorCount];
        for (int i = 0; i < colorCount; i++) {
            if (Math.random() > .7) {
                colors[i] = p.color(255);
            } else if (Math.random() > .6) {
                colors[i] = p.color(0);
            } else {
                colors[i] = g.getColor(Math.round(p.random(colorCount - 1)));
            }
        }

        g.setColors(colors);

        return colors;
    }

    void surfaceToShape() {
        colors = getColors();
        int width = 250;
        surface = new SuperShell(p, width, width, n1_1, n1_2, n1_3, m1_1, n2_1, n2_2, n2_3, m2_1, c1, t2, d1, d2);
//        surface.initColors(colors, colors);
        surface.setTexture(texture.getUpdatedTexture(), "WHOLE");

        surface.useVertexNormals(true);
        surface.setScale(scale);
        shape = p.createShape(p.GROUP);
        boolean render = true;
        for (int i = 2; i < width-2; i++) {
            if (steps != 0 && i % steps == 0) {
                render = !render;
            }
            if (render) {
                p.fill(colors[Math.round(p.random(colors.length-1))]);
                shape.addChild(surface.getHorizontalStrip(i));
//                shape.addChild(surface.getVerticalStrip(i));
            }
        }
    }
}
