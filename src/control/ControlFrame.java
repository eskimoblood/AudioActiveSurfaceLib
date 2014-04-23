package control;

import controlP5.ControlListener;
import controlP5.ControlP5;
import processing.core.PApplet;

import java.awt.*;

public class ControlFrame extends PApplet {

    int w;
    int h;
    private ControlP5 cp5;
    private Object parent;
    private int x;
    private int y;

    public void setup() {
        size(200, 400);
        frameRate(25);
    }

    public ControlP5 getCp5() {
        return cp5;
    }

    public void draw() {
        background(0);
    }

    public ControlFrame(Object parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;

        String name = parent.getClass().getSimpleName();


        createFrame(name);
        createControlP5(parent, name);
    }

    public void addListener(String s, ControlListener cl) {
        cp5.getController(s, parent).addListener(cl);
    }

    public void addListener(ControlListener cl) {
        cp5.addListener(cl);
    }

    private void createFrame(String name) {
        this.init();
        Frame f = new Frame(name);
        f.add(this);
        f.setTitle(name);
        f.setSize(200, 400);
        f.setLocation(x, y);
        f.setResizable(false);
        f.setVisible(true);
    }

    private void createControlP5(Object parent, String name) {
        cp5 = new ControlP5(this);
        cp5.addControllersFor(name, parent);
    }
}
