package raf.bullets.storage.dto;

public class FileEntity {

    private String name;

    private String path;

    private FileType fileType;

    public FileEntity() {

    }

    public FileEntity(String path, String name, FileType fileType) {
        this.fileType = fileType;
        this.name = name;
        this.path = path;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
