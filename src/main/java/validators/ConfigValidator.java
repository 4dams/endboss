package validators;

import java.util.Arrays;

public class ConfigValidator {
    public static boolean isValid(String[] lines) {
        for (String line : lines) {
            if (!ConfigValidator.isValidLine(line))
                return false;
        }

        return true;
    }

    public static boolean isValidLine(String line) {
        System.out.println(String.format("DEBUG  | %s", line));

        String trimmed = line.trim();
        String[] parts = trimmed.split("; ");

        // System.out.println(String.format("DEBUG | %s", Arrays.toString(parts)));

        // Return `true` for comments
        if (trimmed.startsWith("//") || trimmed.startsWith("#"))
            return true;

        // Return `true` for empty lines
        if (trimmed.isEmpty())
            return true;

        // Split line into components
        String[] commandParts = parts[0].split(" ");
        String addOrRemove = commandParts[0];
        String type = commandParts[1];

        // System.out.println(String.format("DEBUG | addOrRemove: `%s` type: `%s`",
        // addOrRemove, type));

        // Check if the line starts with a `+` or a `-`
        if (!trimmed.startsWith("+") && !trimmed.startsWith("-")) {
            System.out.println("ERROR  | Each line must either start with a `+` or a `-`");
            return false;
        }

        if (addOrRemove.equals("+") && (!type.equals("PROJ") && !type.equals("AUF") && !type.equals("PROD"))) {
            System.out.println("ERROR  | The second parameter must be one of [\"PROJ\", \"AUF\", \"PROD\"]");
            return false;
        }

        return true;
    }
}
