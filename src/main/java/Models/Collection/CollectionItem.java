package Models.Collection;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class CollectionItem {
    private long collectionItemID;
    private String url;
    private boolean isVideo;
    private long stt;

    public long getCollectionItemID() {
        return collectionItemID;
    }

    public void setCollectionItemID(long collectionItemID) {
        this.collectionItemID = collectionItemID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }


    public long getStt() {
        return stt;
    }

    public void setStt(long stt) {
        this.stt = stt;
    }

    @Override
    public String toString() {
        return "CollectionItem{" +
                "collectionItemID=" + collectionItemID +
                ", url='" + url + '\'' +
                ", isVideo=" + isVideo +
                ", stt=" + stt +
                '}';
    }
}
