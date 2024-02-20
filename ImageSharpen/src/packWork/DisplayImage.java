package packWork;

// importarea bibliotecilor necesare
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public abstract class DisplayImage implements TimeInterface {

    // functie care afiseaza imaginea
    public static void displayImage(BufferedImage image) {
        System.out.println("Displaying image...");
        JFrame frame = new JFrame(); // initializarea frame-ului
        JLabel label = new JLabel(); // initializarea label-ului
        label.setIcon(new ImageIcon(image)); // adaugarea imaginii in label
        frame.getContentPane().add(label, BorderLayout.CENTER); // adaugarea label-ului in frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // inchiderea frame-ului la apasarea butonului de inchidere
        frame.pack(); // setarea dimensiunilor frame-ului
        frame.setVisible(true); // setarea vizibilitatii frame-ului
        frame.setTitle("Original Image"); // setarea titlului frame-ului
    }

    @Override
    public abstract long readTime();

    @Override
    public abstract long sharpenTime();

    @Override
    public abstract long writeTime();

}
