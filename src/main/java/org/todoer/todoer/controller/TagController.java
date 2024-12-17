package org.todoer.todoer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todoer.todoer.dto.request.TagCreateRequest;
import org.todoer.todoer.dto.request.TagUpdateRequest;
import org.todoer.todoer.dto.response.TagResponse;
import org.todoer.todoer.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse createTag(
            @PathVariable Long projectId,
            @Valid @RequestBody TagCreateRequest request) {
        request.setProjectId(projectId);
        return tagService.createTag(request);
    }

    @GetMapping
    public List<TagResponse> getProjectTags(@PathVariable Long projectId) {
        return tagService.getProjectTags(projectId);
    }

    @PutMapping("/{tagId}")
    public TagResponse updateTag(
            @PathVariable Long tagId,
            @Valid @RequestBody TagUpdateRequest request) {
        return tagService.updateTag(tagId, request);
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
    }
}
