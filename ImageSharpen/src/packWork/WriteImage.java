package packWork;

// importarea bibliotecilor necesare
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// Clasa care contine metodele pentru scrierea imaginii
// Mosteneste clasa SharpenImage
public class WriteImage extends SharpenImage {

    static long timeWrite;
    // bloc static de initializare
    static {
        timeWrite = 0;
    }

    // constructor implicit pentru clasa WriteImage
    public WriteImage() {
    }

    // metoda care scrie imaginea, primeste ca parametri imaginea si numele imaginii finale
    public static void writeImage(BufferedImage image, String args) {
        try {
            timeWrite = System.currentTimeMillis(); // timpul de inceput al scrierii imaginii
            args += ".bmp"; // adaugarea extensiei imaginii
            ImageIO.write(image, "bmp", new File(args));    // scrierea imaginii
            timeWrite = System.currentTimeMillis() - timeWrite; // timpul final al scrierii imaginii
            return;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return;
        }
    }

    // metoda care este suprascrisa, afiseaza timpul de executie al scrierii imaginii
    @Override
    public long writeTime() {
        System.out.println("Writing image took " + timeWrite + " ms");
        return timeWrite;
    }
}
