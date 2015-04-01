package de.matrix44.dedolox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rollo on 01.02.15.
 */
public class ResourceManager {

  public static Bitmap getBitmap(Context context, int id) {

    if (bitmaps.containsKey(id))
      return bitmaps.get(id);

    BitmapFactory.Options opts = new BitmapFactory.Options();
    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, opts);
    //opts.inSampleSize = 4;
    bitmaps.put(id, bmp);

    return bmp;
  }

  public static Bitmap getBitmap565(Context context, int id) {

    if (bitmaps.containsKey(id))
      return bitmaps.get(id);

    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inPreferredConfig = Bitmap.Config.RGB_565;
    //opts.inSampleSize = 4;
    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, opts);
    bitmaps.put(id, bmp);

    return bmp;
  }

  private static final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
}
