package Modules;

import java.util.List;

/**
 * Created by ZIPPER on 4/11/2017.
 */


public interface SnapToRoadListener {
    void onSnapToRoadStart();
    void onSnapToRoadSuccess(List<Location> snap);
}

