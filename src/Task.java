import java.time.LocalDateTime;

public class Task {

    public enum Status { TODO, IN_PROGRESS, DONE }

    private int id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

    public Task(int id, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = Status.TODO;
    }

    // Getters and setters
    public Status getStatus() { return status; }
    public int getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Helper method to mark task as done
    public void markAsDone() { this.status = Status.DONE; }
    public void markAsInProgress() { this.status = Status.IN_PROGRESS; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s %s", id, description, createdAt, updatedAt);
    }
}
