package ru.sbsoft.common;

/**
 * @author balandin
 * @since May 31, 2013 5:10:24 PM
 */
public class Timing {

    long start = 0;
    long end = 0;

    public Timing() {
        this(true);
    }

    public Timing(boolean autoStart) {
        if (autoStart) {
            start();
        }
    }

    public final void start() {
        start = System.currentTimeMillis();
        end = 0;
    }

    public void stop() {
        if (start == 0) {
            throw new IllegalStateException();
        }
        end = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        if (end == 0) {
            stop();
        }
        final long t = end - start;
        return (t / 1000) + "." + Strings.lpad(String.valueOf(t % 1000), '0', 3) + " sec.";
    }

    public static void main(String[] args) throws InterruptedException {
        Timing t = new Timing();
        Thread.currentThread().sleep(1004);
        System.out.println(t);
    }
}
