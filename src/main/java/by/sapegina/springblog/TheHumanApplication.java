package by.sapegina.springblog;

//import by.sapegina.springblog.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@Import(SwaggerConfig.class)
public class TheHumanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheHumanApplication.class, args);
	}

}
