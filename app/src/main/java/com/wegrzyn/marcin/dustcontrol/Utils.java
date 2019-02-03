package com.wegrzyn.marcin.dustcontrol;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Marcin Węgrzyn on 29.01.2018.
 * wireamg@gmail.com
 */

public class Utils {

    static int setPm10Color(Context context, float pm ){
        if(pm < 50)return ContextCompat.getColor(context,R.color.green);
        else if (pm < 100) return ContextCompat.getColor(context,R.color.yellow);
        else if (pm < 150) return ContextCompat.getColor(context,R.color.orange);
        else return ContextCompat.getColor(context, R.color.red);

    }

    static int setPm2Color(Context context, float pm ){
        if(pm < 25)return ContextCompat.getColor(context,R.color.green);
        else if (pm < 50) return ContextCompat.getColor(context,R.color.yellow);
        else if (pm < 100) return ContextCompat.getColor(context,R.color.orange);
        else return ContextCompat.getColor(context, R.color.red);

    }

    static String numberFormat(float number) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(number);
    }

}
