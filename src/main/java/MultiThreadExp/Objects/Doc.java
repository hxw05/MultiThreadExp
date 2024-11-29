package MultiThreadExp.Objects;

import MultiThreadExp.Utils;

import java.io.File;
import java.sql.Timestamp;

public class Doc {
    private String ID;
    private String creator;
    private Timestamp timestamp;
    private String description;
    private String filename;
    private String filepath;

    public Doc(String ID, String creator, Timestamp timestamp, String description, String filepath) {
        this.ID = ID;
        this.creator = creator;
        this.timestamp = timestamp;
        this.description = description;
        this.filepath = filepath;
        this.filename = new File(filepath).getName();
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String[] toDataRow() {
        return new String[]{this.filename, this.filepath, this.description, this.creator, Utils.formatTimestamp(this.timestamp)};
    }
}