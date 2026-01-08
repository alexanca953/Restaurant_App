package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Feedback implements Serializable {
    private int reviewId;
    private int clientId;
    private int score;
    private String comment;
    private String type;
    private LocalDateTime dateTime;

    public Feedback() {}

    public Feedback(int reviewId, int clientId, int score, String comment, String type, LocalDateTime dateTime) {
        this.reviewId = reviewId;
        this.clientId = clientId;
        this.score = score;
        this.comment = comment;
        this.type = type;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "reviewId=" + reviewId +
                ", clientId=" + clientId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", type='" + type + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}