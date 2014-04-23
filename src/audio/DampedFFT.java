package audio;

import control.ControlFrame;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import penner.easing.*;
import controlP5.*;
/**
 * Created by andreaskoberle on 05.04.14.
 */
public class DampedFFT {

    @ControlElement(properties = {"min=0", "max=1"}, x = 10, y = 10)
    private float damping = 2;
    @ControlElement(properties = {"min=0", "max=10"}, x = 10, y = 40)
    private int peakHoldTime = 5;
    @ControlElement(properties = {"min=0.01", "max=1"}, x = 10, y = 70)
    float smoothing = .1f;
    @ControlElement(properties = {"min=1", "max=100"}, x = 10, y = 90)
    float t = .1f;

    private static final int BUFFER_SIZE = 512;
    private static final int MIN_BANDWIDTH_PER_OCTAVE = 200;
    private static final int BANDS_PER_OCTAVE = 3;
    private final PApplet p;

    private FFT fft;
    private AudioInput in;
    private float[] peaks;
    private int[] peak_age;
    int avgSize;
    float minVal = 0.0f;
    float maxVal = 0.0f;
    final boolean useDB = false;

    boolean firstMinDone = false;


    BeatDetect beatDetect;

    public DampedFFT(final PApplet p) {
        this.p = p;
        Minim minim = new Minim(p);
        in = minim.getLineIn(Minim.STEREO, BUFFER_SIZE);

        fft = new FFT(in.bufferSize(), in.sampleRate());

        fft.logAverages(MIN_BANDWIDTH_PER_OCTAVE, BANDS_PER_OCTAVE);

        avgSize = fft.avgSize();

        peaks = new float[avgSize];
        peak_age = new int[avgSize];

        ControlFrame cf = new ControlFrame(this, 200, 0);
        beatDetect = new BeatDetect(in.bufferSize(), in.sampleRate());
    }

    public float[] getUpdatedPeaks() {

        fft.forward(in.mix);

        damp();
        update();

        return peaks;

    }


    public boolean beat() {
        beatDetect.detect(in.mix);
        return beatDetect.isKick();
    }

    private void update() {
        for (int i = 0; i < avgSize; i++) {
            // Get spectrum value (using dB conversion or not, as desired)
            float fftCurr;
            if (useDB) {
                fftCurr = dB(fft.getAvg(i));
            } else {
                fftCurr = fft.getAvg(i);
            }

            // Smooth using exponential moving average
            float val = (smoothing) * peaks[i] + ((1 - smoothing) * fftCurr);

            if (val > peaks[i]) {
                peaks[i] = val;
                peak_age[i] = 0;
            }

            // Find max and min values ever displayed across whole spectrum
            if (peaks[i] > maxVal) {
                maxVal = peaks[i];
            }
            if (!firstMinDone || (peaks[i] < minVal)) {
                minVal = peaks[i];
            }
        }
    }

    private void damp() {
        for (int i = 0; i < avgSize; ++i) {

            if (peak_age[i] < peakHoldTime) {
                ++peak_age[i];
            } else {
                peaks[i] -= damping;
                if (peaks[i] < 0) {
                    peaks[i] = 0;
                }
            }
        }
    }

    float dB(float x) {
        if (x == 0) {
            return 0;
        }
        return 50f * (float) Math.log10(t * x);
    }

    public void render() {
        float[] q = eased();
        for (int i = 0; i < q.length; i++) {
            p.stroke(0);
            p.line(i, p.height, i, p.height - q[i] * 4);
        }
    }

    public float[] eased() {

        int width = 50;
        float[] result = new float[avgSize * width];

        float[] peaks = getUpdatedPeaks();

        for (int i = 0; i < peaks.length - 1; i++) {
            float start = peaks[i];
            float end = peaks[i + 1];
//            p.println(start);
            for (int j = 0; j < width; j++) {
                result[i * width + j] = Cubic.easeInOut(j, start, end - start, width - 1);
            }
        }
        return result;
    }

    public int getWidth() {
        return avgSize * 50;
    }
}
