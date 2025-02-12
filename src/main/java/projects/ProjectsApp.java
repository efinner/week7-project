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
			"3) Select a Project",
			"4) Update project details" ,
			"5) Delete a project"
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
	                    
	                case 4:
	                    updateProjectDetails();  
	                    break;
	                case 5:
						deleteProject();
						break;
	                default:
	                    System.out.println("\n" + selection + " is not a valid selection. Try again.");
	                    break;
	            }
	        }
	    }

		    private void deleteProject() {
		    	listProjects();
				Integer projectId = getIntInput("Enter the id of the project you wish to delete");
				
				projectService.deleteProject(projectId);
				
				System.out.println("Project "+projectId+" was deleted succesfully.");
				
				//Check to see if curProject Id is = deleted project, if so set curProject to null
				if(Objects.nonNull(curProject)&&curProject.getProjectId().equals(projectId))
					curProject=null;
				
			}
	
			private void updateProjectDetails() {
		        if (curProject == null) {
		            System.out.println("\nPlease select a project.");
		            return;
		        }
	
		        // Get new info for project
		        String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		        BigDecimal estimatedHours = getDecimalInput("Enter the project estimated hours [" + curProject.getEstimatedHours() + "]");
		        BigDecimal actualHours = getDecimalInput("Enter the project actual hours [" + curProject.getActualHours() + "]");
		        Integer difficulty = getIntInput("Enter the project difficulty [" + curProject.getDifficulty() + "]");
		        String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
	
		        // Create a new project object
		        Project project = new Project();
		        project.setProjectId(curProject.getProjectId());
		        project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		        project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		        project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		        project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		        project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
	
		        // Call the service to update the project
		        projectService.modifyProjectDetails(project);
	
		        // Refresh the current project
		        curProject = projectService.fetchProjectById(curProject.getProjectId());
	
		        System.out.println("\nProject details updated successfully.");
		        System.out.println("Updated Project: " + curProject);
		    }

	    private void selectProject() {
	        listProjects();
	        Integer projectId = getIntInput("Enter a project ID to choose a project");

	        if (projectId == null) {
	            System.out.println("Invalid project ID. Please try again.");
	            return;
	        }

	        curProject = projectService.fetchProjectById(projectId);

	        if (curProject == null) {
	            System.out.println("Project with ID=" + projectId + " does not exist.");
	        } else {
	            System.out.println("Selected Project: " + curProject);
	        }
	    
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