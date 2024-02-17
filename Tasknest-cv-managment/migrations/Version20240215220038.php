<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240215220038 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE user (id INT AUTO_INCREMENT NOT NULL, email VARCHAR(180) NOT NULL, fname VARCHAR(180) NOT NULL, lname VARCHAR(180) NOT NULL, phonenumber INT NOT NULL, birthdate DATE NOT NULL, roles JSON NOT NULL, password VARCHAR(255) NOT NULL, image VARCHAR(255) DEFAULT NULL, UNIQUE INDEX UNIQ_8D93D649E7927C74 (email), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci`');
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL, available_at DATETIME NOT NULL, delivered_at DATETIME DEFAULT NULL, INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci`');
        $this->addSql('ALTER TABLE cv ADD user_id INT DEFAULT NULL, CHANGE language language JSON DEFAULT NULL');
        $this->addSql('ALTER TABLE cv ADD CONSTRAINT FK_B66FFE92A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_B66FFE92A76ED395 ON cv (user_id)');
        $this->addSql('ALTER TABLE skill ADD cv_id INT DEFAULT NULL, CHANGE skill_value skill_value INT NOT NULL, CHANGE skillname name VARCHAR(255) NOT NULL');
        $this->addSql('ALTER TABLE skill ADD CONSTRAINT FK_5E3DE477CFE419E2 FOREIGN KEY (cv_id) REFERENCES cv (id)');
        $this->addSql('CREATE INDEX IDX_5E3DE477CFE419E2 ON skill (cv_id)');
        $this->addSql('CREATE UNIQUE INDEX cv_name_unique ON skill (cv_id, name)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE user');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('ALTER TABLE cv DROP FOREIGN KEY FK_B66FFE92A76ED395');
        $this->addSql('DROP INDEX UNIQ_B66FFE92A76ED395 ON cv');
        $this->addSql('ALTER TABLE cv DROP user_id, CHANGE language language VARCHAR(255) NOT NULL');
        $this->addSql('ALTER TABLE skill DROP FOREIGN KEY FK_5E3DE477CFE419E2');
        $this->addSql('DROP INDEX IDX_5E3DE477CFE419E2 ON skill');
        $this->addSql('DROP INDEX cv_name_unique ON skill');
        $this->addSql('ALTER TABLE skill DROP cv_id, CHANGE skill_value skill_value VARCHAR(255) NOT NULL, CHANGE name skillname VARCHAR(255) NOT NULL');
    }
}
