package packWork;

// Interfata care contine metodele pentru aflarea timpului de executie
// a citirii, a ascutirii si a scrierii imaginii
public interface TimeInterface {
    public long readTime();

    public long sharpenTime();

    public long writeTime();
}
