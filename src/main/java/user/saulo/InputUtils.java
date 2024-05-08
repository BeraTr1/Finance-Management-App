package user.saulo;public class InputUtils {
    public static double getDoubleFromString(String s) throws Exception {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            throw new Exception("'" + s + "' is not a number!");
        }
    }
}
