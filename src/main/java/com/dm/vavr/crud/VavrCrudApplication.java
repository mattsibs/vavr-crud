package com.dm.vavr.crud;

import com.dm.vavr.crud.config.CRUDConfiguration;
import com.dm.vavr.crud.controller.CRUDController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = CRUDConfiguration.class)
public class VavrCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(VavrCrudApplication.class, args);
	}
}
