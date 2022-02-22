import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helpers.Logger;
import helpers.Whitespace;
import validators.ConfigValidator;

/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class ProjectManager {

    private File configFile;
    private File logFile;

    private Writer logFileWriter;

    private List<ProjectComponent> projects = new ArrayList<ProjectComponent>();

    /**
     * Main Method
     * 
     * @param args provided config.txt and log.txt
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            Logger.error("no config file provided");
            System.exit(1);
        }

        Logger.debug("provided config file: " + args[0]);

        if (args.length == 2) {
            Logger.debug("provided log file: " + args[1]);
        }

        ProjectManager projectManager = new ProjectManager(args[0], args[1]);
    }

    /**
     * Constructor ProjectManager
     * 
     * @param configFilePath Path for config file
     * @param logFilePath    Path for log file
     */
    public ProjectManager(String configFilePath, String logFilePath) {
        Logger.info("creating new project manager");

        // Create new config file object
        configFile = new File(configFilePath);
        String path = configFile.getAbsolutePath();

        // Create new log file writer
        try {
            logFile = new File(logFilePath);
            logFileWriter = new BufferedWriter(new FileWriter(logFile, true));

        } catch (IOException e) {
            Logger.error("error creating log file");
        }

        // Check if a file with the provided fileName exists
        if (!configFile.exists() || !configFile.canRead() || !configFile.isFile())
            throw new IllegalArgumentException(
                    String.format("ERROR  | provided path `%s` is not readable or does not exists",
                            path));

        // Check if the file is a directory
        if (configFile.isDirectory())
            throw new IllegalArgumentException(
                    String.format("ERROR  | provided path `%s` is a directory", path));

        Logger.info(String.format("reading contents of file `%s`", path));

        // Read file contents
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))) {
            // Create array with all lines of the config
            String line = null;
            List<String> lines = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            // Check if the config is valid
            if (ConfigValidator.isValid(lines.toArray(new String[lines.size()]))) {
                Logger.success("provided config is valid");
            } else {
                Logger.error("prodivded config is invalid");

                // Throw exception
                throw new IllegalArgumentException(
                        String.format("ERROR  | config `%s` is invalid, please verify its contents", path));
            }

            this.createComponents(lines.toArray(new String[lines.size()]));

            this.printProjects();

            if (this.logFileWriter != null) {
                this.logFileWriter.close();
            }
        } catch (IOException exception) {
            Logger.error("error reading from config file");
            Logger.error(exception.getMessage());
        }

    }

    /**
     * Method createComponents()
     * Creates a Componenet in an Array from config file
     * 
     * @param lines Array with data from config file
     */
    public void createComponents(String[] lines) {
        // Create all required components
        ProjectComponent[] tempComponents = {};

        for (String config : lines) {
            String trimmed = config.trim();
            String[] parts = trimmed.split("; ");
            String type = parts[0].replace("+ ", "");

            // Handle additions
            if (trimmed.startsWith("+")) {
                // Create a new component
                ProjectComponent component = null;

                // Create different types of components
                switch (type) {
                    case "PROJ":
                        component = new Project(parts[1], parts[2],
                                Float.parseFloat(parts[3].replace(";", "")));
                        break;

                    case "AUF":
                        component = new Task(parts[1], parts[2],
                                Integer.parseInt(parts[3].replace(";", "")));
                        break;

                    case "PROD":
                        component = new Product(parts[1], parts[2],
                                Float.parseFloat(parts[3].replace(";", "")));
                        break;
                }

                // Add the component to the temporary components list
                tempComponents = Arrays.copyOf(tempComponents, tempComponents.length + 1);
                tempComponents[tempComponents.length - 1] = (ProjectComponent) component;
            }
        }

        // Link components
        for (String config : lines) {
            // Split the command into parts
            String trimmed = config.trim();
            String[] parts = trimmed.split("; ");

            if (trimmed.startsWith("+")) {
                ProjectComponent target = this.findComponent(tempComponents, parts[1]);

                try {
                    // Find the parent component the target shall be linked to
                    String parentName = parts[4];
                    ProjectComponent parent = this
                            .findComponent(tempComponents, parentName);

                    // Link the target to the parent component
                    parent.components = Arrays.copyOf(parent.components, parent.components.length + 1);
                    parent.components[parent.components.length - 1] = target;

                } catch (ArrayIndexOutOfBoundsException exception) {
                    this.projects.add(target);
                }
            }
        }

        // Delete items
        for (String config : lines) {
            String trimmed = config.trim();
            String[] parts = trimmed.split("; ");
            String name = parts[0].replace("- ", "");

            if (trimmed.startsWith("-")) {
                ProjectComponent parent = this
                        .findComponentParent(this.projects.toArray(new ProjectComponent[this.projects.size()]), name);

                // Abort if no parent was found
                if (parent == null) {
                    Logger.error(String.format("could not find component containing %s", name));
                    return;
                }

                // Find the index of the item to remove
                int index = 0;
                for (int i = index; i < parent.components.length - 1; i++) {
                    if (parent.components[i] != null && parent.components[i].getName().equals(name)) {
                        index = i;
                        break;
                    }
                }

                // Remove the component at the previously identified index
                parent.components[index] = null;

            }
        }
    };

    /**
     * Method findComponent()
     * Searchs for component in Component Array
     * 
     * @param source Source Array
     * @param name   Searched Element
     * @return If Component in Array: Returns Element
     *         If Component not in Array: Returns null
     */
    public ProjectComponent findComponent(ProjectComponent[] source, String name) {
        ProjectComponent target = null;

        for (ProjectComponent component : source) {
            if (target != null)
                break;

            if (component.getName().equals(name)) {
                target = component;
                return target;

            }

            target = this.findComponent(component.components, name);
        }

        return target;
    }

    /**
     * Method findComponentParent
     * Searchs for the Parent of a Component
     * 
     * @param source Source Array
     * @param name   Searched Element
     * @return If Component has Child: Returns Child Component
     *         If Component has no Child: Returns null
     */
    public ProjectComponent findComponentParent(ProjectComponent[] source, String name) {
        ProjectComponent target = null;

        for (ProjectComponent component : source) {
            if (target != null)
                break;

            if (component != null) {
                // Check if target component is a child of the current component
                for (ProjectComponent child : component.components) {
                    if (child != null && child.getName().equals(name)) {
                        target = component;
                        return target;
                    }
                }

                target = this.findComponentParent(component.components, name);
            }

        }

        return target;
    }

    /**
     * Method printRecursively()
     * Prints the ProjectTree in Consol and writes it in log file
     * 
     * @param source Source Array
     */
    public void printRecursively(ProjectComponent[] source, int iteration) {
        try {
            // Iterate through project tree
            for (ProjectComponent component : source) {
                if (component == null)
                    return;

                // Print tree to console
                Logger.info(String.format("%s└ %s", Whitespace.generate(iteration * 4), component.toString()));

                // Write tree to file
                if (this.logFileWriter != null) {
                    this.logFileWriter
                            .append(String.format("%s└ %s\n", Whitespace.generate(iteration * 4),
                                    component.toString()));

                }

                if (component.hasChildren()) {
                    this.printRecursively(component.components, iteration + 1);
                }
            }

        } catch (IOException e) {
            Logger.error(String.format("error writing to log file at `%s`", this.logFile.getAbsolutePath()));
            e.printStackTrace();
        }

    }

    /**
     * Recursively print all components and their children
     */
    public void printProjects() {
        this.printRecursively(this.projects.toArray(new ProjectComponent[this.projects.size()]), 0);
    }
}
