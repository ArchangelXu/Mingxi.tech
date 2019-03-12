package tech.mingxi.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class ExamApplication {
	@Autowired
	NewsJob newsJob;

	public static void main(String[] args) {
		SpringApplication.run(ExamApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void getNewsDataOnApplicationStart() {
		newsJob.getNews();
	}
}
