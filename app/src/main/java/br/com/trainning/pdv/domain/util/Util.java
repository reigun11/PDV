package br.com.trainning.pdv.domain.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by android on 12/03/2016.
 */
public class Util {

    public static String getCurrencyValue(double valor){
        DecimalFormat ptBR = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        ptBR.setDecimalFormatSymbols(symbols);
        if(valor>0.0d) {
            return " R$ " + ptBR.format(valor);
        }else{
            return "";
        }
    }

    public static String getFormatedCurrency(String value){
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        if(value==null){
            value="0.00";
        }
        return currency.format(Double.parseDouble(value));
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int)  (px /((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
