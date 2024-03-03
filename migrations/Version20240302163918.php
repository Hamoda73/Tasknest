<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240302163918 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE cv CHANGE language language JSON DEFAULT NULL COMMENT \'(DC2Type:json)\'');
        $this->addSql('ALTER TABLE reset_password_request CHANGE id id INT AUTO_INCREMENT NOT NULL');
        $this->addSql('ALTER TABLE reset_password_request ADD CONSTRAINT FK_7CE748AA76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('DROP INDEX name ON skill');
        $this->addSql('CREATE UNIQUE INDEX cv_name_unique ON skill (cv_id, name)');
        $this->addSql('ALTER TABLE user CHANGE password password VARCHAR(180) NOT NULL, CHANGE image image VARCHAR(180) NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE cv CHANGE language language VARCHAR(255) DEFAULT NULL');
        $this->addSql('ALTER TABLE reset_password_request DROP FOREIGN KEY FK_7CE748AA76ED395');
        $this->addSql('ALTER TABLE reset_password_request CHANGE id id INT NOT NULL');
        $this->addSql('DROP INDEX cv_name_unique ON skill');
        $this->addSql('CREATE UNIQUE INDEX name ON skill (name, cv_id)');
        $this->addSql('ALTER TABLE user CHANGE image image VARCHAR(255) DEFAULT NULL, CHANGE password password VARCHAR(255) NOT NULL');
    }
}
