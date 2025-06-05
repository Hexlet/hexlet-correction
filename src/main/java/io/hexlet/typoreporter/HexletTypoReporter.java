package io.hexlet.typoreporter;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.util.Optional.ofNullable;

@Slf4j
@SpringBootApplication
public class HexletTypoReporter {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv
            .configure()
            .ignoreIfMissing()
            .load();
        final var env = SpringApplication.run(HexletTypoReporter.class, args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        final var protocol = "http";
        final var port = ofNullable(env.getProperty("server.port")).orElse("8080");
        final var contextPath = ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(String::isBlank)
            .orElse("/");
        var hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        final var msg = """

            ----------------------------------------------------------
                Application '{}' is running! Access URLs:
                Local:       {}://localhost:{}{}
                External:    {}://{}:{}{}
                Profile(s):  {}
            ----------------------------------------------------------
            """;
        final var appName = env.getProperty("spring.application.name");
        log.info(msg, appName, protocol, port, contextPath,
            protocol, hostAddress, port, contextPath, env.getActiveProfiles());
    }
}
