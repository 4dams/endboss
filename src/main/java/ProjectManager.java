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
            System.out.println(String.format("INFO | %s", fileName));

            // Create new ProjectManager instance for each file
            ProjectManager projectManager = new ProjectManager(fileName);
        }
    }

    public ProjectManager(String fileName) {
        System.out.println("INFO | Initializing new ProjectManager");
        System.out.println(String.format("INFO | Provided fileName: `%s`", fileName));

        // Create new File object
        file = new File(fileName);
        String path = file.getAbsolutePath();

        // Check if a file with the provided fileName exists
        if (!file.exists() || !file.canRead() || !file.isFile())
            throw new IllegalArgumentException(
                    String.format("ERROR | Provided path `%s` is not readable or does not exists",
                            path));

        // Check if the file is a directory
        if (file.isDirectory())
            throw new IllegalArgumentException(
                    String.format("ERROR | Provided path `%s` is a directory", path));

        System.out.println(String.format("INFO | Reading contents of file `%s`", path));

        // Read file contents
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            // Create array with all lines of the config
            String line = null;
            List<String> lines = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            // Check if the config is valid
            if (ConfigValidator.isValid(lines.toArray(new String[lines.size()]))) {
                System.out.println(String.format("INFO | Config `%s` is valid", path));
            } else {
                System.out.println(String.format("INFO | Config `%s` is invalid", path));

                // Throw exception
                throw new IllegalArgumentException(
                        String.format("ERROR | Config `%s` is invalid, please verify its contents", path));
            }

            this.createComponents(lines.toArray(new String[lines.size()]));

            // // Iterate through lines and create components
            // for (String config : lines.toArray(new String[lines.size()])) {
            // String trimmed = config.trim();
            // String[] parts = trimmed.split("; ");
            // String type = parts[0].replace("+ ", "");

            // if (trimmed.startsWith("+")) {
            // switch (type) {
            // case "PROJ":
            // System.out.println(String.format("INFO | Creating new Project `%s`",
            // parts[1]));

            // Project project = new Project(parts[1], parts[2],
            // Float.parseFloat(parts[3].replace(";", "")));

            // try {

            // String parent = parts[4];

            // // TODO: Add project to parent preject, find parent project, check if parent
            // // project exists

            // Project parentProject = this
            // .findProject(this.projects.toArray(new Project[this.projects.size()]),
            // parent);

            // System.out.println(parentProject);

            // } catch (ArrayIndexOutOfBoundsException exception) {
            // System.out.println("DEBUG | Project has no parent project");

            // this.projects.add(project);
            // }

            // break;

            // case "AUF":
            // System.out.println(String.format("INFO | Creating new Task `%s`", parts[1]));
            // break;

            // case "PROD":
            // System.out.println(String.format("INFO | Creating new Product `%s`",
            // parts[1]));
            // break;
            // }
            // } else {
            // String target = parts[0].replace("- ", "");

            // System.out.println(String.format("INFO | Deleting Project component `%s`",
            // target));
            // // TODO: find component with given name
            // }
            // }

        } catch (IOException exception) {
            System.out.println("FATAL | Cannot recover from `IOException`");
            System.out.print(exception.getMessage());
        }
    }

    public void createComponents(String[] lines) {
        // Create all required components
        Project[] rawComponents = {};

        for (String config : lines) {
            String trimmed = config.trim();
            String[] parts = trimmed.split("; ");
            String type = parts[0].replace("+ ", "");

            // TODO: Handle removals
            if (trimmed.startsWith("+")) {
                switch (type) {
                    case "PROJ":
                        Project project = new Project(parts[1], parts[2],
                                Float.parseFloat(parts[3].replace(";", "")));

                        rawComponents = Arrays.copyOf(rawComponents, rawComponents.length + 1);
                        rawComponents[rawComponents.length - 1] = project;
                }
            }
        }

        // Link components
        for (String config : lines) {
            String trimmed = config.trim();
            String[] parts = trimmed.split("; ");
            String type = parts[0].replace("+ ", "");

            if (trimmed.startsWith("+")) {
                switch (type) {
                    case "PROJ":
                        ProjectComponent target = this.findProject(rawComponents, parts[1]);

                        try {
                            String parentName = parts[4];
                            Project parent = this
                                    .findProject(rawComponents, parentName);

                            parent.components[parent.components.length] = target;

                        } catch (ArrayIndexOutOfBoundsException exception) {
                            this.projects.add(target);
                        }
                }
            }
        }

        System.out.println(this);
    }

    public Project findProject(ProjectComponent[] source, String name) {
        for (ProjectComponent project : source) {
            if (project instanceof Project) {
                // TODO: Debug me and fix this fucking shit i cba anyway
                if (project.getName().equals(name)) {
                    return (Project) project;
                } else {
                    return this.findProject(project.components, name);
                }
            }
        }

        System.out.println(String.format("ERROR | Cannot find parent project with name `%s`", name));

        return null;
    }

}
