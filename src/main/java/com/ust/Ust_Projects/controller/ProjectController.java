package com.ust.Ust_Projects.controller;

import com.ust.Ust_Projects.model.Project;
import com.ust.Ust_Projects.model.ProjectStatus;
import com.ust.Ust_Projects.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @PostMapping("/create")
    public String createProject(@RequestBody Project project, Principal principal) {
        project.setUsername(principal.getName());
        project.setProjectStatus(ProjectStatus.IN_PROGRESS);
        projectRepository.save(project);
        return "Project created successfully";
    }

    @GetMapping("/approveAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String approveAll() {
        projectRepository.findAll().stream().filter(post -> post.getProjectStatus().equals(ProjectStatus.IN_PROGRESS))
                .forEach(post -> {
                    post.setProjectStatus(ProjectStatus.COMPLETED);
                    projectRepository.save(post);
                });
        return "Approved all posts !";
    }
}
