CREATE SCHEMA `plutus` ;

CREATE TABLE `plutus`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `userscol_UNIQUE` (`email` ASC));

CREATE TABLE `plutus`.`tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `value` VARCHAR(128) NULL,
  `flowType` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

CREATE TABLE `plutus`.`buckets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `desc` VARCHAR(512) NULL,
  `balance` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

  CREATE TABLE `money_flows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flowType` int(11) DEFAULT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `tagIds` varchar(45) DEFAULT NULL,
  `fromBucketId` int(11) DEFAULT NULL,
  `toBucketId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bucketId_idx` (`fromBucketId`),
  KEY `toBucketId_idx` (`toBucketId`),
  CONSTRAINT `fromBucketId` FOREIGN KEY (`fromBucketId`) REFERENCES `buckets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `toBucketId` FOREIGN KEY (`toBucketId`) REFERENCES `buckets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

