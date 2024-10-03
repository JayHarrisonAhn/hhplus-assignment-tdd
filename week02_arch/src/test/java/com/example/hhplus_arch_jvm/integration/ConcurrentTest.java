package com.example.hhplus_arch_jvm.integration;

import com.example.hhplus_arch_jvm.application.command.RegisterCourseCommand;
import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.facade.CourseFacade;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationCountJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationJPARepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConcurrentTest {

    @Autowired private CourseFacade courseFacade;
    @Autowired private CourseRegistrationJPARepository courseRegistrationJPARepository;
    @Autowired private CourseRegistrationCountJPARepository courseRegistrationCountJPARepository;

    @Autowired private TransactionTemplate template;

    @Test
    void test40StudentsRegisterToOneCourse() {
        // Given
        CourseInfo courseInfo = courseFacade.createCourse(
                CourseInfo.builder()
                        .date(LocalDate.of(2025, 1, 1))
                        .name("하반기 취업 특강")
                        .coachDescription("타일러 코치")
                        .build()
        );

        // When
        ExecutorService executor = Executors.newFixedThreadPool(40);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicInteger registeredCount = new AtomicInteger(0);
        for (long i = 1; i <= 40; i++) {
            long taskId = i; // Assign a unique task number
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(taskId + "th student started to register on thread " + Thread.currentThread().getName());
                try {
                    courseFacade.registerCourse(new RegisterCourseCommand(
                            courseInfo.getId(), taskId
                    ));
                    registeredCount.incrementAndGet();
                    System.out.println(taskId);
                } catch (IllegalStateException ignored) {
                }
                System.out.println(taskId + "th student finished to register");
            }, executor);
            futures.add(future);
        }
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        assertDoesNotThrow( () -> allOfFutures.get());
        executor.shutdown();
        System.out.println("All tasks completed.");
        System.out.println(courseRegistrationCountJPARepository.findAll().get(0).getCount());

        // Then
        assertEquals(30, registeredCount.get());
        template.execute( status -> {
            assertEquals(30, courseRegistrationCountJPARepository.findByCourseIdIs(courseInfo.getId()).getCount());
            assertEquals(30, courseRegistrationJPARepository.countByCourseIdEquals(courseInfo.getId()));
            return null;
        });
    }
}
