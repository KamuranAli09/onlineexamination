import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}

class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

public class ExamSystem {
    private static User currentUser;
    private static List<Question> questions;
    private static List<Integer> selectedAnswers;
    private static Timer timer;
    private static int remainingTimeInSeconds = 1800;

    public static void main(String[] args) {
        initializeQuestions();
        login();
    }

    private static void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What is 5^2?", List.of("10", "7", "3", "25"), 3));
        // Add more questions as needed
    }

    private static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        currentUser = new User(username, password, "Pradhaan S");

        if (currentUser != null) {
            showMainMenu(scanner);
        } else {
            System.out.println("Login failed. Please try again.");
            login();
        }
    }

    private static void showMainMenu(Scanner scanner) {
        System.out.println("Welcome, " + currentUser.getFullName() + "!");
        System.out.println("1. Start Exam");
        System.out.println("2. Update Profile");
        System.out.println("3. Change Password");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                startExam(scanner);
                break;
            case 2:
                updateProfile(scanner);
                break;
            case 3:
                changePassword(scanner);
                break;
            case 4:
                logout(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                showMainMenu(scanner);
                break;
        }
    }

    private static void startExam(Scanner scanner) {
        selectedAnswers = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remainingTimeInSeconds--;
                if (remainingTimeInSeconds <= 0) {
                    autoSubmit();
                }
            }
        }, 1000, 1000);

        System.out.println("You have 30 minutes to complete the exam.");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.getQuestionText());
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j));
            }
            System.out.print("Select an answer (1-" + options.size() + "): ");
            int answer = scanner.nextInt();
            selectedAnswers.add(answer - 1);
        }
        autoSubmit();
    }

    private static void autoSubmit() {
        if (timer != null) {
            timer.cancel();
        }
        System.out.println("Time's up! Submitting your answers.");
        showResult();
    }

    private static void showResult() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int selectedAnswerIndex = selectedAnswers.get(i);
            if (selectedAnswerIndex == question.getCorrectOptionIndex()) {
                score++;
            }
        }

        System.out.println("You scored " + score + " out of " + questions.size() + " questions.");
        logout(new Scanner(System.in));
    }

    private static void updateProfile(Scanner scanner) {
        System.out.print("Enter your new full name: ");
        String newFullName = scanner.nextLine();
        currentUser = new User(currentUser.getUsername(), currentUser.getPassword(), newFullName);
        System.out.println("Profile updated successfully.");
        showMainMenu(scanner);
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();
        if (currentPassword.equals(currentUser.getPassword())) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            currentUser = new User(currentUser.getUsername(), newPassword, currentUser.getFullName());
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Incorrect current password. Please try again.");
        }
        showMainMenu(scanner);
    }

    private static void logout(Scanner scanner) {
        System.out.println("Logging out. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
