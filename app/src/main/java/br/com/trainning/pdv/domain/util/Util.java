package br.com.trainning.pdv.domain.util;

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
}
