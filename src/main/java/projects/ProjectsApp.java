/**
 * 
 */
package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;


/**
 * 
 */
public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	// @formatter:off
	private List<String> operations = List.of( 
			"1) Add a project"
	);
	// @formatter:on
	 private ProjectService projectService = new ProjectService();

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections(); 
	

}
	private void processUserSelections() {
		boolean done = false;
		
	    while (!done) {
            try {
                printOperations();  // Displays the menu options
                int selection = getUserSelection();  // Gets the user's selection

                switch (selection) {
                    case -1:
                        done = exitMenu();  // Handles exit condition
                        break;
                    case 1:
                        createProject();  // Method to handle project creation
                        break;
                    default:
                        System.out.println("\n" + selection + " is not a valid selection. Try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.toString());  // Handles general exceptions and prints the error message
            }
        }
    
				
		
	}
	private void printOperations() {
		System.out.println("\nAvailable Operations:");
	    operations.forEach(op -> System.out.println("  " + op));  // Print each operation with indentation
	}
	private void createProject() {
		String projectName = getStringInput("Enter the Project Name ");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated Hours ");
		BigDecimal actualHours = getDecimalInput("Enter the Actual Hours ");
		Integer difficulty = getIntInput("Enter the Project Difficulty (1-5) " );
		String notes = getStringInput("Enter the Project notes ");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch (NumberFormatException e) {
			throw new DbException(input + "is not a vaild decimal number. ");
		}
	}
	private boolean exitMenu() {
		System.out.println("Exiting the Menu");
        return true;  // Return true to indicate that the application should exit
	}
	private int getUserSelection() {
		  printOperations();  // Print the available operations
		    Integer input = getIntInput("Enter a menu selection");  // Get user input as an Integer

		    // Return -1 if input is null, otherwise return the input value
		    return input == null ? -1 : input;
		}
	private Integer getIntInput(String prompt) {
		  String input = getStringInput(prompt);  // Get user input as a String

		    if (Objects.isNull(input)) {
		        return null;  // Return null if input is null
		    }

		    try {
		        return Integer.valueOf(input);  // Convert input to Integer
		    } catch (NumberFormatException e) {
		        throw new DbException(input + " is not a valid number. Try again.");  // Throw custom exception
		    }
}
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");  // Print the prompt
	    String input = scanner.nextLine().trim();  // Get user input and trim whitespace

	    return input.isEmpty() ? null : input;  // Return null if input is blank, otherwise return the trimmed input
	    
	}
}

