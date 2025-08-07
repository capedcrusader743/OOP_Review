import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    public enum Status { TODO, IN_PROGRESS, DONE }

    private String id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

    public Task(String id, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = Status.TODO;
    }

    // Getters and setters
    public Status getStatus() { return status; }
    public String getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public void setUpdatedAt(LocalDateTime now) { this.updatedAt = now; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Helper method to mark task as done
    public void markAsDone() { this.status = Status.DONE; }
    public void markAsInProgress() { this.status = Status.IN_PROGRESS; }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s (Created: %s, Updated: %s)",
                id,
                status.toString().toLowerCase(), // Shows "todo", "in_progress", "done"
                description,
                createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
