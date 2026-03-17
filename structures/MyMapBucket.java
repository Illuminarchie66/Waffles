package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyKeyValueEntry;

public class MyMapBucket {
    private MyArrayList<MyKeyValueEntry> entries;

    public MyMapBucket() {
        if(entries == null) {
            entries = new MyArrayList<>();
        }
    }

    public MyArrayList<MyKeyValueEntry> getEntries() {
        return entries;
    }

    public void addEntry(MyKeyValueEntry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(MyKeyValueEntry entry) {
        this.entries.remove(entry);
    }
}