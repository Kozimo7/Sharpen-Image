package packWork;

// importarea bibliotecilor necesare
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// thread
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

// Clasa care contine metodele pentru citirea, afisarea si aflarea timpului de executie
// Implementeaza interfata TimeInterface
public class ReadImage extends DisplayImage {

    static long time;

    //! thread
    private final Queue<byte[]> segmentsQueue = new LinkedList<>();
    private final Semaphore producerSemaphore = new Semaphore(1);
    private final Semaphore consumerSemaphore = new Semaphore(0);
    private final Pipe pipe = new Pipe();
    private final Object lock = new Object();
    private BufferedImage image = null;
    //! thread


    // bloc static de initializare
    static {
        time = 0;
    }

    // constructor implicit pentru clasa ReadImage
    public ReadImage() {
    }

    // metoda care citeste imaginea
    public static BufferedImage readImage(String args) {
        time = System.currentTimeMillis(); // timpul de inceput al citirii imaginii
        args = "src/Images/" + args; // adaugarea path-ului imaginii, pentru a usura citirea
        File file = new File(args);
        BufferedImage image = null; // initializarea imaginii
        try {
            image = ImageIO.read(file); // citirea imaginii
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        time = System.currentTimeMillis() - time; // timpul final al citirii imaginii
        return image;
    }

    //! thread
    private void produce (File file) throws InterruptedException, IOException{
        while (true) {
            producerSemaphore.acquire();
            byte[] bytes = ImageIO.read(file).toString().getBytes();
            synchronized (lock) {
                segmentsQueue.add(bytes);
                consumerSemaphore.release();

                lock.wait();
            }
            System.out.println("Producer finished");
            Thread.sleep(1000);
        }
    }

    private BufferedImage consumer () throws InterruptedException, IOException{
        BufferedImage image = null;
        while (true) {
            consumerSemaphore.acquire();
            byte[] bytes = null;
            synchronized (lock) {
                bytes = segmentsQueue.poll();
                if (bytes == null) {
                    break;
                }
                lock.notify();
            }
            producerSemaphore.release();
            System.out.println("Consumer finished");
            Thread.sleep(1000);


            image = ReadImage.readImage("src/Images/PozaNr1.bmp");

            System.out.println("Image read" + image);
            SharpenImage.sharpenImage(0.2, image);
            Thread writerThread = getWriterThread(bytes);

            ByteArrayOutputStream writerResult = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;

            while ((read = pipe.in.read(buffer)) > 0) {
                writerResult.write(buffer, 0, read);
            }
            pipe.in.close();
            writerThread.join();
        }
        return image;
    }

    private Thread getWriterThread (byte[] bytes) {
        Thread writerThread = new Thread(() -> {
            int  segmentPartSize = bytes.length/4;
            for (int i = 0; i < 4; i++) {
                int start = i * segmentPartSize;
                int end = (i + 1) * segmentPartSize;

                try {
                    System.out.println("Writing segment " + i);
                    pipe.out.write(bytes, start, end);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                pipe.out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writerThread.start();
        return writerThread;
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setImage (BufferedImage image) {
        this.image = image;
    }

    public void run (String input, String output) throws InterruptedException, IOException {
        File file = new File("src/Images/" + input);
        

        Thread producerThread = new Thread(() -> {
            try {
                produce(file);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        Thread consumerThread = new Thread(() -> {
            try {
                setImage(consumer());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Main thread was interrupted");
        }
        System.out.println("Main thread finished");

        WriteImage.writeImage(image, output);
        
    }
    //! thread

    // metoda care este suprascrisa, afiseaza timpul de executie al citirii imaginii
    @Override
    public long readTime() {
        System.out.println("Reading image took " + time + " ms");
        return time;
    }

    // metodele abstracte care sunt suprascrise, afiseaza 0, deoarece nu se aplica
    // pentru citirea imaginii
    @Override
    public long sharpenTime() {
        return 0;
    }

    @Override
    public long writeTime() {
        return 0;
    }

}