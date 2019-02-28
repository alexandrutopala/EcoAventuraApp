-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema EcoAventuraDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema EcoAventuraDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `EcoAventuraDB` DEFAULT CHARACTER SET utf8 ;
USE `EcoAventuraDB` ;

-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `parola` VARCHAR(45) NULL,
  `acces` INT NULL,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;

INSERT INTO `user` (`idUser`, `username`, `parola`, `acces`) VALUES (NULL, 'Topi', 'Wanted2014', '3');

-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`Serie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`Serie` (
  `idSerie` INT NOT NULL AUTO_INCREMENT,
  `numarSerie` INT NULL,
  `dataInceput` VARCHAR(45) NULL,
  PRIMARY KEY (`idSerie`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`Echipa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`Echipa` (
  `idEchipa` INT NOT NULL AUTO_INCREMENT,
  `numeEchipa` VARCHAR(45) NULL,
  `culoareEchipa` VARCHAR(45) NULL,
  `scoalaEchipa` VARCHAR(45) NULL,
  `profEchipa` VARCHAR(45) NULL,
  `Serie_idSerie` INT NOT NULL,
  PRIMARY KEY (`idEchipa`),
  INDEX `fk_Echipa_Serie_idx` (`Serie_idSerie` ASC),
  CONSTRAINT `fk_Echipa_Serie`
    FOREIGN KEY (`Serie_idSerie`)
    REFERENCES `EcoAventuraDB`.`Serie` (`idSerie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`ActivitateGenerala`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`ActivitateGenerala` (
  `idActivitateGenerala` INT NOT NULL AUTO_INCREMENT,
  `numeActivitateGenerala` VARCHAR(45) NULL,
  PRIMARY KEY (`idActivitateGenerala`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`Activitate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`Activitate` (
  `idActivitate` INT NOT NULL AUTO_INCREMENT,
  `organizator` VARCHAR(150) NULL,
  `data` VARCHAR(45) NULL,
  `Echipa_idEchipa` INT NOT NULL,
  `ActivitateGenerala_idActivitateGenerala` INT NOT NULL,
  `loactie` VARCHAR(50) NULL,
  `post` VARCHAR(50) NULL,
  `idProgram` INT(20) NULL,
  PRIMARY KEY (`idActivitate`),
  INDEX `fk_Activitate_Echipa1_idx` (`Echipa_idEchipa` ASC),
  INDEX `fk_Activitate_ActivitateGenerala1_idx` (`ActivitateGenerala_idActivitateGenerala` ASC),
  CONSTRAINT `fk_Activitate_Echipa1`
    FOREIGN KEY (`Echipa_idEchipa`)
    REFERENCES `EcoAventuraDB`.`Echipa` (`idEchipa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Activitate_ActivitateGenerala1`
    FOREIGN KEY (`ActivitateGenerala_idActivitateGenerala`)
    REFERENCES `EcoAventuraDB`.`ActivitateGenerala` (`idActivitateGenerala`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`JocGeneral`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`JocGeneral` (
  `idJocGeneral` INT NOT NULL AUTO_INCREMENT,
  `numeJocGeneral` VARCHAR(45) NULL,
  `descriereJoc` VARCHAR(1010) NULL,
  PRIMARY KEY (`idJocGeneral`))
ENGINE = InnoDB;

INSERT INTO `jocgeneral` (`idJocGeneral`, `numeJocGeneral`, `descriereJoc`)
VALUES (1, 'penalizare', 'penalizare');


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`Joc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`Joc` (
  `idJoc` INT NOT NULL AUTO_INCREMENT,
  `organizator` VARCHAR(150) NULL,
  `punctaj` INT NULL,
  `data` VARCHAR(45) NULL,
  `Echipa_idEchipa` INT NOT NULL,
  `JocGeneral_idJocGeneral` INT NOT NULL,
  `locatie` VARCHAR(50) NULL,
  `post` VARCHAR(50) NULL,
  `absent` TINYINT(1) NULL,
  `idProgram` INT(20) NULL,
  PRIMARY KEY (`idJoc`),
  INDEX `fk_Joc_Echipa1_idx` (`Echipa_idEchipa` ASC),
  INDEX `fk_Joc_JocGeneral1_idx` (`JocGeneral_idJocGeneral` ASC),
  CONSTRAINT `fk_Joc_Echipa1`
    FOREIGN KEY (`Echipa_idEchipa`)
    REFERENCES `EcoAventuraDB`.`Echipa` (`idEchipa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Joc_JocGeneral1`
    FOREIGN KEY (`JocGeneral_idJocGeneral`)
    REFERENCES `EcoAventuraDB`.`JocGeneral` (`idJocGeneral`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`MembruEchipa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`MembruEchipa` (
  `idMembruEchipa` INT NOT NULL AUTO_INCREMENT,
  `numeMembruEchipa` VARCHAR(45) NULL,
  `Echipa_idEchipa` INT NOT NULL,
  PRIMARY KEY (`idMembruEchipa`),
  INDEX `fk_MembruEchipa_Echipa1_idx` (`Echipa_idEchipa` ASC),
  CONSTRAINT `fk_MembruEchipa_Echipa1`
    FOREIGN KEY (`Echipa_idEchipa`)
    REFERENCES `EcoAventuraDB`.`Echipa` (`idEchipa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EcoAventuraDB`.`Animator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `EcoAventuraDB`.`Animator` (
  `idAnimator` INT NOT NULL AUTO_INCREMENT,
  `numeAnimator` VARCHAR(45) NULL,
  `disponibilAnimator` TINYINT(1) NULL,
  PRIMARY KEY (`idAnimator`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
