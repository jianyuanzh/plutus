CREATE SCHEMA `plutus` ;

CREATE TABLE `plutus`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `userscol_UNIQUE` (`email` ASC));
