package helpers;

public class Whitespace {
    public static String generate(int length) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            str.append(" ");
        }

        return str.toString();
    }
}
