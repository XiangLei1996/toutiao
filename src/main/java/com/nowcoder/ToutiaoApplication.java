package com.nowcoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot的特点，@SpringBootApplication来注解启动类
 * 在里面提供一个主方法直接运行项目(SpringBoot内置了tomcat)
 */
@SpringBootApplication
public class ToutiaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToutiaoApplication.class, args);
	}
}
