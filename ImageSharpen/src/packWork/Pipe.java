package packWork;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Pipe {
    public PipedInputStream in;
    public PipedOutputStream out;

    public Pipe() {
        try {
            out = new PipedOutputStream();
            in = new PipedInputStream(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}