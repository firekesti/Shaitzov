package net.crunkhouse.shaitzov;

import android.content.Context;

public final class DensityUtils {
    private DensityUtils() {
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     * @param px      A value in pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int pixelToDp(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     * @param context Context to get resources and device specific display metrics
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int dpToPixel(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
