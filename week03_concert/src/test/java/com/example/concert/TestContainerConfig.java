package com.example.concert;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfig {

    private static GenericContainer redis;
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;

    private static GenericContainer mariadb;
    private static final String MARIADB_IMAGE = "mariadb:11.5";

    private static KafkaContainer kafka;
    private static final String KAFKA_IMAGE = "apache/kafka";

    static {
        redis = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));

        mariadb = new MariaDBContainer(MARIADB_IMAGE)
                .withDatabaseName("concert")
                .withUsername("username")
                .withPassword("password");

        kafka = new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE));
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }
}
