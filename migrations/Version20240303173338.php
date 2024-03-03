<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240303173338 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE complaint (id INT AUTO_INCREMENT NOT NULL, user_id INT DEFAULT NULL, type VARCHAR(255) NOT NULL, message VARCHAR(255) NOT NULL, INDEX IDX_5F2732B5A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE respond (id INT AUTO_INCREMENT NOT NULL, complaint_id INT DEFAULT NULL, message VARCHAR(255) NOT NULL, UNIQUE INDEX UNIQ_99C5D563EDAE188E (complaint_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE complaint ADD CONSTRAINT FK_5F2732B5A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE respond ADD CONSTRAINT FK_99C5D563EDAE188E FOREIGN KEY (complaint_id) REFERENCES complaint (id)');
        $this->addSql('ALTER TABLE application ADD CONSTRAINT FK_A45BDDC1A090B42E FOREIGN KEY (offers_id) REFERENCES offers (id)');
        $this->addSql('ALTER TABLE application ADD CONSTRAINT FK_A45BDDC1A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE cv CHANGE language language JSON DEFAULT NULL COMMENT \'(DC2Type:json)\'');
        $this->addSql('ALTER TABLE offers ADD CONSTRAINT FK_DA460427A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE reset_password_request CHANGE id id INT AUTO_INCREMENT NOT NULL');
        $this->addSql('ALTER TABLE reset_password_request ADD CONSTRAINT FK_7CE748AA76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('DROP INDEX name ON skill');
        $this->addSql('CREATE UNIQUE INDEX cv_name_unique ON skill (cv_id, name)');
        $this->addSql('ALTER TABLE user DROP resettoken, CHANGE password password VARCHAR(180) NOT NULL');
        $this->addSql('ALTER TABLE messenger_messages CHANGE created_at created_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', CHANGE available_at available_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', CHANGE delivered_at delivered_at DATETIME DEFAULT NULL COMMENT \'(DC2Type:datetime_immutable)\'');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE complaint DROP FOREIGN KEY FK_5F2732B5A76ED395');
        $this->addSql('ALTER TABLE respond DROP FOREIGN KEY FK_99C5D563EDAE188E');
        $this->addSql('DROP TABLE complaint');
        $this->addSql('DROP TABLE respond');
        $this->addSql('ALTER TABLE application DROP FOREIGN KEY FK_A45BDDC1A090B42E');
        $this->addSql('ALTER TABLE application DROP FOREIGN KEY FK_A45BDDC1A76ED395');
        $this->addSql('ALTER TABLE cv CHANGE language language VARCHAR(255) DEFAULT NULL');
        $this->addSql('ALTER TABLE messenger_messages CHANGE created_at created_at DATETIME NOT NULL, CHANGE available_at available_at DATETIME NOT NULL, CHANGE delivered_at delivered_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE offers DROP FOREIGN KEY FK_DA460427A76ED395');
        $this->addSql('ALTER TABLE reset_password_request DROP FOREIGN KEY FK_7CE748AA76ED395');
        $this->addSql('ALTER TABLE reset_password_request CHANGE id id INT NOT NULL');
        $this->addSql('DROP INDEX cv_name_unique ON skill');
        $this->addSql('CREATE UNIQUE INDEX name ON skill (name, cv_id)');
        $this->addSql('ALTER TABLE user ADD resettoken VARCHAR(180) NOT NULL, CHANGE password password VARCHAR(255) NOT NULL');
    }
}
