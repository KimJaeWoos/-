package com.jwoos.android.sellbook.utils;

/**
 * Created by Jwoo on 2016-08-04.
 */
public class ImageRotate {

    public static int orientation(int orientation) {

        int rotate = 0;


        if (orientation == 2)
            rotate = 0;
        else if (orientation == 3)
            rotate = 180;
        else if (orientation == 4)
            rotate = 180;
        else if (orientation == 5)
            rotate = 90;
        else if (orientation == 6)
            rotate = 90;
        else if (orientation == 7)
            rotate = -90;
        else if (orientation == 8)
            rotate = -90;

        return rotate;
    }
}
