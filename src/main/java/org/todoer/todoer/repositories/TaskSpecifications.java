package org.todoer.todoer.repositories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.todoer.todoer.entities.Task;
import org.todoer.todoer.entities.TaskStatus;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskSpecifications {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> dueBetween(ZonedDateTime start, ZonedDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start == null) return cb.lessThanOrEqualTo(root.get("dueDate"), end);
            if (end == null) return cb.greaterThanOrEqualTo(root.get("dueDate"), start);
            return cb.between(root.get("dueDate"), start, end);
        };
    }

    public static Specification<Task> hasAssignee(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return null;
            return cb.equal(root.get("assignedTo").get("id"), userId);
        };
    }

    public static Specification<Task> inProject(Long projectId) {
        return (root, query, cb) -> {
            if (projectId == null) return null;
            return cb.equal(root.get("project").get("id"), projectId);
        };
    }

    public static Specification<Task> hasPriority(Integer priority) {
        return (root, query, cb) -> {
            if (priority == null) return null;
            return cb.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.trim().isEmpty()) return null;
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Task> createdBy(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return null;
            return cb.equal(root.get("createdBy").get("id"), userId);
        };
    }

    public static Specification<Task> hasTag(Long tagId) {
        return (root, query, cb) -> {
            if (tagId == null) return null;
            return cb.isMember(tagId, root.get("tags"));
        };
    }
}
