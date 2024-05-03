package user.saulo;

import java.text.DecimalFormat;

public class MathUtils {
    public static double roundDouble(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String roundedDoubleString = decimalFormat.format(d);

        return Double.valueOf(roundedDoubleString);
    }
}
