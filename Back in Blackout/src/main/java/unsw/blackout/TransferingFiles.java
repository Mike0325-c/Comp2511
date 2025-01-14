package unsw.blackout;

public class TransferingFiles {
    private String filename;
    private String content;
    private int filesize;
    private String fromId;
    private String toId;

    public TransferingFiles(String filename, String content, int filesize, String fromId, String toId) {
        this.filename = filename;
        this.content = content;
        this.filesize = filesize;
        this.fromId = fromId;
        this.toId = toId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
