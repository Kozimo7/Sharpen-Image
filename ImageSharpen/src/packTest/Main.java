package packTest;

// importarea pachetelor necesare, pentru a putea citi, afisa, aplica filtrul de sharpness si scrie imaginea
import packWork.ReadImage;
import packWork.SharpenImage;
import packWork.WriteImage;

// importarea bibliotecilor necesare
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // initializarea variabilelor
    Scanner scanner;
    String inputName;
    String outputName;
    double sharpnessRadius;
    BufferedImage image;
    WriteImage write;

    // bloc de initializare
    {
        scanner = new Scanner(System.in);
        inputName = "";
        outputName = "";
        sharpnessRadius = 0;
        image = null;
        write = new WriteImage();
    }

    public static void main(String... args) {
        try {
            // citirea numelui imaginii de intrare, a numelui imaginii de iesire si a razei
            // de sharpness
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the input name of the image:");
            String inputName = scanner.nextLine();
            // daca numele imaginii de intrare este mai mic sau egal cu 6, se arunca o
            // exceptie, nu se poate citi imaginea
            if (inputName.length() <= 6) {
                scanner.close();
                throw new IOException("Please provide the input file name");
            }
            System.out.println("Enter the output name of the image:");
            String outputName = scanner.nextLine();
            // daca numele imaginii de iesire este egal cu 0, se arunca o exceptie, nu se
            // poate scrie imaginea
            if (outputName.length() == 0) {
                scanner.close();
                throw new IOException("Please provide the output file name");
            }
            System.out.println("Enter the sharpness radius of the image:");
            double sharpnessRadius = scanner.nextDouble();
            // daca raza de sharpness este mai mica sau egala cu 0, se arunca o exceptie, nu
            // se poate aplica filtrul
            if (sharpnessRadius <= 0) {
                scanner.close();
                throw new IOException("Please provide a positive sharpness radius");
            }
            System.out.println("Radius: " + sharpnessRadius);
            scanner.close();
            // daca numele imaginii de intrare nu se termina cu .bmp, se adauga
            // extensia .bmp, pentru a usura citirea
            if (!inputName.endsWith(".bmp")) {
                inputName += ".bmp";
            }

            // se creeaza un obiect de tip BufferedImage, care va contine imaginea citita
            BufferedImage image = null;

            // se citeste imaginea si se afiseaza imaginea originala
            image = ReadImage.readImage(inputName);
            ReadImage.displayImage(image);

            // se aplica filtrul de sharpness imaginii si se afiseaza imaginea sharpened
            image = SharpenImage.sharpenImage(sharpnessRadius, image);
            SharpenImage.displayImage(image);

            // se scrie imaginea sharpened
            WriteImage.writeImage(image, outputName);

            // se afiseaza timpul de executie al citirii, al aplicarii filtrului de
            // sharpness si al scrierii imaginii
            WriteImage write = new WriteImage();
            write.readTime();
            write.sharpenTime();
            write.writeTime();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}