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
			"1) Add a project",
			"2) List Projects",
			"3) Select a Project"
	);
	// @formatter:on
	 private ProjectService projectService = new ProjectService();
	    private Project curProject; 

	    public static void main(String[] args) {
	        new ProjectsApp().processUserSelections();
	    }

	    private void processUserSelections() {
	        boolean done = false;

	        while (!done) {
	            printOperations();  // This ensures operations are printed once per loop
	            int selection = getUserSelection();

	            switch (selection) {
	                case -1:
	                    done = exitMenu();
	                    break;
	                case 1:
	                    createProject();
	                    break;
	                case 2:
	                    listProjects();
	                    break;
	                case 3:
	                    selectProject();
	                    break;
	                default:
	                    System.out.println("\n" + selection + " is not a valid selection. Try again.");
	                    break;
	            }
	        }
	    }

	    private void selectProject() {
	        listProjects();
	        Integer projectId = getIntInput("Enter a project Id to choose a project");

	        curProject = projectService.fetchProjectById(projectId);
	        System.out.println("Selected Project: " + curProject); 
	    }
	    private void listProjects() {
	        List<Project> projects = projectService.fetchAllProjects();

	        System.out.println("\nProjects: ");

	        projects.forEach(
	                project -> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
	    }

	    private void printOperations() {
	        System.out.println("\nAvailable Operations:");
	        operations.forEach(op -> System.out.println("  " + op));

	        if (curProject == null) {
	            System.out.println("\nYou are not working with a project.");
	        } else {
	            System.out.println("\nYou are working with project: " + curProject);
	        }
	    
	    }

	    private void createProject() {
	        String projectName = getStringInput("Enter the Project Name ");
	        BigDecimal estimatedHours = getDecimalInput("Enter the estimated Hours ");
	        BigDecimal actualHours = getDecimalInput("Enter the Actual Hours ");
	        Integer difficulty = getIntInput("Enter the Project Difficulty (1-5) ");
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
	        } catch (NumberFormatException e) {
	            throw new DbException(input + " is not a valid decimal number.");
	        }
	    }

	    private boolean exitMenu() {
	        System.out.println("Exiting the Menu");
	        return true;
	    }

	    private int getUserSelection() {
	        
	        Integer input = getIntInput("Enter a menu selection");
	        return input == null ? -1 : input;
	    }

	    private Integer getIntInput(String prompt) {
	        String input = getStringInput(prompt);

	        if (Objects.isNull(input)) {
	            return null;
	        }

	        try {
	            return Integer.valueOf(input);
	        } catch (NumberFormatException e) {
	            throw new DbException(input + " is not a valid number. Try again.");
	        }
	    }

	    private String getStringInput(String prompt) {
	        System.out.print(prompt + ": ");
	        String input = scanner.nextLine().trim();

	        return input.isEmpty() ? null : input;
	    }
	}