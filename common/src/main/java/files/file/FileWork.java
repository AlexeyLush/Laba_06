package files.file;

public interface FileWork {
    String readFile();
    String readFile(String fileName);
    void writeFile(String fileName, String data);
}
