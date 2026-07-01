package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"controller", "dao", "model", "util"})
public class BibliotecaVirtualApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaVirtualApplication.class, args);
    }
}
