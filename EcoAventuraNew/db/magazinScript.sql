-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema MagazinDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema MagazinDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `MagazinDB` DEFAULT CHARACTER SET utf8 ;
USE `MagazinDB` ;

-- -----------------------------------------------------
-- Table `MagazinDB`.`Produs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MagazinDB`.`Produs` (
  `idProdus` INT NOT NULL AUTO_INCREMENT,
  `Denumire` VARCHAR(150) NOT NULL,
  `Pret` FLOAT NOT NULL,
  `Stoc` INT NULL DEFAULT 0,
  `Observatii` VARCHAR(200) NULL,
  PRIMARY KEY (`idProdus`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MagazinDB`.`Tranzactie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MagazinDB`.`Tranzactie` (
  `idTranzactie` INT NOT NULL AUTO_INCREMENT,
  `Bucati` INT NOT NULL,
  `Adaugate` TINYINT(1) NOT NULL,
  `Data` VARCHAR(50) NOT NULL,
  `idProdus` INT NOT NULL,
  `Observatii` VARCHAR(200) NULL,
  `Pret` FLOAT NULL,
  PRIMARY KEY (`idTranzactie`),
  INDEX `fk_Tranzactie_Produs_idx` (`idProdus` ASC),
  CONSTRAINT `fk_Tranzactie_Produs`
    FOREIGN KEY (`idProdus`)
    REFERENCES `MagazinDB`.`Produs` (`idProdus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
