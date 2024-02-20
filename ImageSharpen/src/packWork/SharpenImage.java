package packWork;

// importarea bibliotecilor necesare
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

// Clasa care contine metodele pentru sharpness ale imaginii
// Mosteneste clasa ReadImage
public class SharpenImage extends ReadImage {

    static long timeSharp;
    // bloc static de initializare
    static {
        timeSharp = 0;
    }

    // constructor implicit pentru clasa SharpenImage
    public SharpenImage() {
    }

    // metoda care aplica filtrul de sharpness imaginea
    public static BufferedImage sharpenImage(double radius, BufferedImage originalImage) {
        timeSharp = System.currentTimeMillis(); // timpul de inceput al aplicarii filtrului imaginii

        // initializarea imaginii sharpened, cu aceleasi dimensiuni ca si imaginea originala
        BufferedImage sharpenedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                originalImage.getType());

        // parcurgerea imaginii sharpened, pixel cu pixel
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                // initializarea sumelor pentru fiecare culoare, care vor fi folosite pentru
                // calcularea culorii sharpened
                int sumR = 0, sumG = 0, sumB = 0;
                int n = 0;
                // parcurgerea imaginii original, in functie de raza, pe axa oy
                for (double ny = Math.max(0, y - radius); ny < Math.min(originalImage.getHeight(),
                        y + 1 + radius); ny++) {
                    // parcurgerea imaginii original, in functie de raza, pe axa ox
                    for (double nx = Math.max(0, x - radius); nx < Math.min(originalImage.getWidth(),
                            x + 1 + radius); nx++) {
                        // daca pixelul nu este pixelul curent(locatia x si y difera), se adauga la suma culorilor
                        if (nx != x || ny != y) {
                            // creeam un nou obiect de tip Color, pentru a putea extrage culorile pixelului
                            Color c = new Color(originalImage.getRGB((int) nx, (int) ny));
                            sumR += c.getRed();
                            sumG += c.getGreen();
                            sumB += c.getBlue();
                            n++;
                        }
                    }
                }
                // extragem culoarea pixelului curent
                Color c = new Color(originalImage.getRGB(x, y));
                // calculam culorile sharpened, in functie de suma culorilor si de numarul de pixeli
                int red = (int) constrain(c.getRed() * (n + 1) - sumR, 0, 255);
                int green = (int) constrain(c.getGreen() * (n + 1) - sumG, 0, 255);
                int blue = (int) constrain(c.getBlue() * (n + 1) - sumB, 0, 255);
                // setam culoarea sharpened a pixelului curent
                Color newColor = new Color(red, green, blue);
                // setam culoarea sharpened a pixelului curent in imaginea sharpened
                sharpenedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        timeSharp = System.currentTimeMillis() - timeSharp; // timpul final al aplicarii filtrului imaginii

        return sharpenedImage;
    }

    // metoda care determina valoarea maxima a unei culori
    private static double constrain(double val, double min, double max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        }
        return val;
    }

    // metoda care afiseaza imaginea sharpened
    public static void displayImage(BufferedImage image) {
        System.out.println("Displaying sharpened image...");
        JFrame frame = new JFrame();    // initializarea frame-ului
        JLabel label = new JLabel();    // initializarea label-ului
        label.setIcon(new ImageIcon(image));    // adaugarea imaginii in label
        frame.getContentPane().add(label, BorderLayout.CENTER); // adaugarea label-ului in frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // inchiderea frame-ului la apasarea butonului de inchidere
        frame.pack();   // setarea dimensiunilor frame-ului
        frame.setVisible(true); // setarea vizibilitatii frame-ului
        frame.setTitle("Sharpened Image");  // setarea titlului frame-ului
    }

    // metoda care este suprascrisa, afiseaza timpul de executie al aplicarii filtrului imaginii
    @Override
    public long sharpenTime() {
        System.out.println("Sharpening image took " + timeSharp + " ms");
        return timeSharp;
    }

}
