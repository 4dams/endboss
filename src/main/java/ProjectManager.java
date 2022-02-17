import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import validators.ConfigValidator;

/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class ProjectManager {

    private File file;

    private List<ProjectComponent> projects = new ArrayList<ProjectComponent>();

    public static void main(String[] args) {

        // Iterate through provided config files
        for (String fileName : args) {
            System.out.println(String.format("INFO   | looking up config `%s`...", fileName));

            // Create new ProjectManager instance for each file
            ProjectManager projectManager = new ProjectManager(fileName);
        }
    }

    public ProjectManager(String fileName) {
        System.out.println("INFO   | initializing new project manager");

        // Create new File object
        file = new File(fileName);
        String path = file.getAbsolutePath();

        // Check if a file with the provided fileName exists
        if (!file.exists() || !file.canRead() || !file.isFile())
            throw new IllegalArgumentException(
                    String.format("ERROR  | provided path `%s` is not readable or does not exists",
                            path));

        // Check if the file is a directory
        if (file.isDirectory())
            throw new IllegalArgumentException(
                    String.format("ERROR  | provided path `%s` is a directory", path));

        System.out.println(String.format("INFO   | reading contents of file `%s`", path));

        // Read file contents
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            // Create array with all lines of the config
            String line = null;
            List<String> lines = new ArrayList<String>();

            // Wieso funktioniert das?
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            // Check if the config is valid
            if (ConfigValidator.isValid(lines.toArray(new String[lines.size()]))) {
                System.out.println(String.format("INFO   | config `%s` is valid", path));
            } else {
                System.out.println(String.format("INFO   | config `%s` is invalid", path));

                // Throw exception
                throw new IllegalArgumentException(
                        String.format("ERROR  | config `%s` is invalid, please verify its contents", path));
            }

            this.createComponents(lines.toArray(new String[lines.size()]));
        } catch (IOException exception) {
            System.out.println("FATAL | cannot recover from `IOException`");
            System.out.print(exception.getMessage());
        }
    }

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
                    System.out.println(String.format("WARN | could not find any component containing `%s`", name));
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

        float total = this.projects.get(0).berechneKosten();
        System.out.println(String.format("INFO   | total: %s €", total));
    }

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

}
