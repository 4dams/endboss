package validators;

import helpers.Logger;

/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class ConfigValidator {
    /**
     * Check if a given configuration file is valid by checking each line
     * 
     * @param lines Array that should be tested
     * @return
     */
    public static boolean isValid(String[] lines) {
        for (String line : lines) {
            if (!ConfigValidator.isValidLine(line))
                return false;
        }

        return true;
    }

    /**
     * Check if a given line is valid
     * 
     * @param line Line that should be tested
     * @return returns true if line valid
     */
    public static boolean isValidLine(String line) {
        Logger.debug(line);

        String trimmed = line.trim();
        String[] parts = trimmed.split("; ");

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

        // Check if the line starts with a `+` or a `-`
        if (!trimmed.startsWith("+") && !trimmed.startsWith("-")) {
            Logger.error("each line must start with either `+` or `-`");
            return false;
        }

        if (addOrRemove.equals("+") && (!type.equals("PROJ") && !type.equals("AUF") && !type.equals("PROD"))) {
            Logger.error("the second parameter must be one of [\"PROJ\", \"AUF\", \"PROD\"]");
            return false;
        }

        return true;
    }
}
