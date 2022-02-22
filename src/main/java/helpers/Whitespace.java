package helpers;
/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 * 
 *          Class Whitespace
 * 
 */
public class Whitespace {
    public static String generate(int length) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            str.append(" ");
        }

        return str.toString();
    }
}
