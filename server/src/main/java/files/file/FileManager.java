package files.file;

/**
 * Абстрактный класс для менеджера файла
 */

public abstract class FileManager {

    private String fileName;

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return this.fileName;
    }
    public FileManager(String fileName){
        this.fileName = fileName;
    }

}
