package br.com.zup.orangetalentschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrangeTalentsChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrangeTalentsChallengeApplication.class, args);
	}

}
