package Models.Collection;

import java.util.ArrayList;

public class Collection {
    private long collectionID;
    private String nameOfCollection;
    private ArrayList<CollectionItem> listCollection;

    public long getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(long collectionID) {
        this.collectionID = collectionID;
    }

    public String getNameOfCollection() {
        return nameOfCollection;
    }

    public void setNameOfCollection(String nameOfCollection) {
        this.nameOfCollection = nameOfCollection;
    }

    public ArrayList<CollectionItem> getListCollection() {
        return listCollection;
    }

    public void setListCollection(ArrayList<CollectionItem> listCollection) {
        this.listCollection = listCollection;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "collectionID=" + collectionID +
                ", nameOfCollection='" + nameOfCollection + '\'' +
                ", listCollection=" + listCollection +
                '}';
    }
}
