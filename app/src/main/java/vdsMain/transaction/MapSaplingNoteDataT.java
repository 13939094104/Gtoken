package vdsMain.transaction;

import java.util.LinkedHashMap;
import java.util.Map;

//brr
public class MapSaplingNoteDataT extends LinkedHashMap<SaplingOutpoint, SaplingNoteData> {
    public MapSaplingNoteDataT() {
    }

    public MapSaplingNoteDataT(Map<? extends SaplingOutpoint, ? extends SaplingNoteData> map) {
        super(map);
    }
}
