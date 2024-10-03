package com.example.hhplus_arch_jvm.integration;

import com.example.hhplus_arch_jvm.application.command.RegisterCourseCommand;
import com.example.hhplus_arch_jvm.application.command.ViewRegisteredCourseCommand;
import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.facade.CourseFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepeatTest {

    @Autowired private CourseFacade courseFacade;

    @Autowired private TransactionTemplate template;

    @Test
    void testRegister5TimesSimultaneously() {
        // Given
        Long studentId = 100L;
        CourseInfo courseInfo = courseFacade.createCourse(
                CourseInfo.builder()
                        .date(LocalDate.of(2025, 1, 1))
                        .name("하반기 취업 특강")
                        .coachDescription("타일러 코치")
                        .build()
        );

        // When
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicInteger registeredCount = new AtomicInteger(0);
        AtomicReference<LocalDateTime> registeredTime = new AtomicReference<>();
        for (long i = 1; i <= 5; i++) {
            long taskId = i; // Assign a unique task number
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(taskId + "th student started to register on thread " + Thread.currentThread().getName());
                try {
                    CourseRegistration registration = courseFacade.registerCourse(new RegisterCourseCommand(
                            courseInfo.getId(), studentId
                    ));
                    registeredCount.incrementAndGet();
                    registeredTime.set(registration.getCreatedAt());
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

        // Then
        assertEquals(1, registeredCount.get());
        assertEquals(registeredTime.get(), courseFacade
                .viewRegisteredCourses(new ViewRegisteredCourseCommand(studentId))
                .get(0)
                .getRegisteredAt());
    }
}
