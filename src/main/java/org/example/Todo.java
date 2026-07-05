package org.example;

import java.time.LocalDateTime;

public class Todo {

    public int getTitle;
    private int id;
    private String title;
    LocalDateTime start;
    LocalDateTime end;
    Integer priority;
    String memo;
    String icon;
    boolean achieve;

    public Todo(int id, String title, LocalDateTime start, LocalDateTime end, Integer priority, String memo, String icon) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.priority = priority;
        this.memo = memo;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getMemo() {
        return memo;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return title;
    }
}