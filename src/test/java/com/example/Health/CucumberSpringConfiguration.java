package com.example.Health;

import java.time.Duration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    static MySQLContainer<?> mysql =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("healthcheck")
                    .withUsername("test")
                    .withPassword("1234")
                    .withStartupTimeout(Duration.ofMinutes(2));

    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7"))
                    .withExposedPorts(6379)
                    .waitingFor(Wait.forListeningPort())
                    .withStartupTimeout(Duration.ofMinutes(2));

    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"))
                    .waitingFor(Wait.forListeningPort())
                    .withStartupTimeout(Duration.ofMinutes(3));

    static {
        // 변경된 부분: 컨테이너를 수동으로 먼저 시작
        mysql.start(); // 변경된 부분
        redis.start(); // 변경된 부분
        kafka.start(); // 변경된 부분

        // 변경된 부분: 시작 후 값 확인용 로그
        System.out.println("=== Testcontainers started ==="); // 변경된 부분
        System.out.println("MySQL JDBC URL = " + mysql.getJdbcUrl()); // 변경된 부분
        System.out.println("Redis Host = " + redis.getHost()); // 변경된 부분
        System.out.println("Redis Port = " + redis.getMappedPort(6379)); // 변경된 부분
        System.out.println("Kafka Bootstrap = " + kafka.getBootstrapServers()); // 변경된 부분
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        System.out.println("=== registerProperties 실행됨 ==="); // 변경된 부분

        // 변경된 부분: Spring Boot가 실제 사용할 속성을 직접 등록
        registry.add("spring.datasource.url", mysql::getJdbcUrl); // 변경된 부분
        registry.add("spring.datasource.username", mysql::getUsername); // 변경된 부분
        registry.add("spring.datasource.password", mysql::getPassword); // 변경된 부분
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver"); // 변경된 부분

        registry.add("spring.data.redis.host", redis::getHost); // 변경된 부분
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379)); // 변경된 부분

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers); // 변경된 부분
    }
}