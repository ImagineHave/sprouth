package xyz.imaginehave.sprouth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.imaginehave.sprouth.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}