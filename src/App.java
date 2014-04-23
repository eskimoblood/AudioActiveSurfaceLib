
/**
 * Created by andreaskoberle on 05.04.14.
 */

import audio.Texture;
import control.ControlFrame;
import controlP5.ControlElement;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGL;
import processing.opengl.PShader;

import peasy.*;
import shapes.SurfaceShape;

public class App extends PApplet {

    private Texture texture;
    private PShader shader;
    private SurfaceShape surface;

    @ControlElement(properties = {"min=1", "max=1000"}, x = 10, y = 50)
    private int distortion = 1000;

    public static void main(String args[]) {
        PApplet.main(new String[]{"App"});
    }

    public void setup() {
        size(1280, 720, OPENGL);
        ControlFrame cf = new ControlFrame(this, 0, 0);
        texture = new Texture(this);
        surface = new SurfaceShape(this, texture);
        shader = loadShader("audio_frag.glsl", "audio_vert.glsl");

        new PeasyCam(this, 100);

    }

    public void draw() {
        shader.set("distortion", distortion);
        PGL pgl = beginPGL();
//        pgl.enable(PGL.CULL_FACE);
        background(255);
        directionalLight(102, 102, 102, 0, 0, -1);
        lightSpecular(204, 204, 204);
        directionalLight(102, 102, 102, 0, 1, -1);
        lightSpecular(102, 102, 102);
        PImage updatedTexture = texture.getUpdatedTexture();
//        image(updatedTexture,0,0);
        shape(surface.getShape());
        shader(shader);
        endPGL();
    }
}
