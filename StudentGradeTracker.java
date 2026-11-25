import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private double score;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
}

public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("=== Student Grade Tracker ===");
        System.out.print("Enter the number of students: ");
        int count = readInt(scanner);

        for (int i = 0; i < count; i++) {
            System.out.println("\nStudent #" + (i + 1));
            System.out.print("Enter student name: ");
            String name = scanner.nextLine().trim();
            while (name.isEmpty()) {
                System.out.print("Name cannot be empty. Enter student name: ");
                name = scanner.nextLine().trim();
            }

            double score;
            while (true) {
                System.out.print("Enter score (0 - 100): ");
                score = readDouble(scanner);
                if (score >= 0 && score <= 100) {
                    break;
                }
                System.out.println("Invalid score. Please enter a value between 0 and 100.");
            }

            students.add(new Student(name, score));
        }

        displaySummaryReport(students);
        scanner.close();
    }

    private static void displaySummaryReport(ArrayList<Student> students) {
        System.out.println("\n==========================================");
        System.out.println("           STUDENT SUMMARY REPORT         ");
        System.out.println("==========================================");

        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        double sum = 0.0;
        double highest = Double.NEGATIVE_INFINITY;
        double lowest = Double.POSITIVE_INFINITY;
        String topStudent = "";
        String lowStudent = "";

        System.out.printf("%-20s %10s %8s%n", "Name", "Score", "Grade");
        System.out.println("------------------------------------------");

        for (Student s : students) {
            double score = s.getScore();
            sum += score;

            if (score > highest) {
                highest = score;
                topStudent = s.getName();
            }
            if (score < lowest) {
                lowest = score;
                lowStudent = s.getName();
            }

            System.out.printf("%-20s %10.2f %8s%n", s.getName(), score, getLetterGrade(score));
        }

        double average = sum / students.size();

        System.out.println("------------------------------------------");
        System.out.printf("Total Students : %d%n", students.size());
        System.out.printf("Average Score  : %.2f (%s)%n", average, getLetterGrade(average));
        System.out.printf("Highest Score  : %.2f (%s)%n", highest, topStudent);
        System.out.printf("Lowest Score   : %.2f (%s)%n", lowest, lowStudent);
        System.out.println("==========================================");
    }

    private static String getLetterGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        return val;
    }
}


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class NLPProcessor {
    private static final Set<String> STOP_WORDS = Set.of(
        "a", "an", "the", "is", "are", "was", "were", "to", "for", "of",
        "in", "on", "at", "with", "it", "this", "that", "i", "you", "me", "my"
    );

    public static List<String> tokenizeAndClean(String input) {
        String cleaned = input.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
        String[] rawTokens = cleaned.trim().split("\\s+");
        List<String> tokens = new ArrayList<>();

        for (String token : rawTokens) {
            if (!token.isEmpty() && !STOP_WORDS.contains(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}

class ChatBotEngine {
    private final Map<String, List<String>> faqKnowledgeBase = new LinkedHashMap<>();

    public ChatBotEngine() {
        initializeFAQs();
    }

    private void initializeFAQs() {
        faqKnowledgeBase.put("greeting", List.of(
            "hello", "hi", "hey", "greetings", "morning", "evening"
        ));
        faqKnowledgeBase.put("hours", List.of(
            "hours", "open", "close", "timing", "schedule", "support"
        ));
        faqKnowledgeBase.put("internship", List.of(
            "internship", "task", "certificate", "submission", "codealpha", "deadline"
        ));
        faqKnowledgeBase.put("java", List.of(
            "java", "arraylist", "oop", "class", "object", "compile", "jvm"
        ));
        faqKnowledgeBase.put("help", List.of(
            "help", "assist", "feature", "can", "do", "question"
        ));
        faqKnowledgeBase.put("bye", List.of(
            "bye", "goodbye", "exit", "quit", "later", "thanks", "thank"
        ));
    }

    public String generateResponse(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Please type a message so I can help you!";
        }

        List<String> tokens = NLPProcessor.tokenizeAndClean(userInput);
        String bestIntent = null;
        int bestScore = 0;

        for (Map.Entry<String, List<String>> entry : faqKnowledgeBase.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (tokens.contains(keyword) || userInput.toLowerCase().contains(keyword)) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestIntent = entry.getKey();
            }
        }

        if (bestScore == 0 || bestIntent == null) {
            return "I'm not sure I understood that. Try asking about internship tasks, certificates, support hours, Java basics, or type 'help'.";
        }

        return switch (bestIntent) {
            case "greeting" -> "Hello! I'm your Java AI Assistant. How can I help you today?";
            case "hours" -> "Our virtual assistant is available 24/7, and support responses are typically handled Monday–Friday, 9:00 AM – 6:00 PM.";
            case "internship" -> "Make sure your code is well-tested, pushed to GitHub or documented clearly, and submitted before your task deadline to qualify for your certificate.";
            case "java" -> "Java is an object-oriented programming language that runs on the JVM. Use ArrayList for dynamic lists and Scanner or Swing for input/UI.";
            case "help" -> "You can ask me about:\n  • Internship tasks & certificates\n  • Support hours\n  • Basic Java concepts\n  • General greetings";
            case "bye" -> "Goodbye! Best of luck with your Java projects and tasks.";
            default -> "How can I assist you further?";
        };
    }
}

public class AIChatBotGUI extends JFrame {
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final ChatBotEngine botEngine;

    public AIChatBotGUI() {
        this.botEngine = new ChatBotEngine();

        setTitle("Java AI Chatbot");
        setSize(520, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.append("Bot: Hello! Ask me a question or type 'help' to get started.\n\n");

        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        chatArea.append("You: " + text + "\n");
        String reply = botEngine.generateResponse(text);
        chatArea.append("Bot: " + reply + "\n\n");
        inputField.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AIChatBotGUI gui = new AIChatBotGUI();
            gui.setVisible(true);
        });
    }
}
