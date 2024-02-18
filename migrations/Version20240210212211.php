<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240210212211 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE application (id INT AUTO_INCREMENT NOT NULL, dispo VARCHAR(20) NOT NULL, salary_d DOUBLE PRECISION NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci`');
        $this->addSql('ALTER TABLE offers ADD user_id INT NOT NULL');
        $this->addSql('ALTER TABLE offers ADD CONSTRAINT FK_DA460427A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('CREATE INDEX IDX_DA460427A76ED395 ON offers (user_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE application');
        $this->addSql('ALTER TABLE offers DROP FOREIGN KEY FK_DA460427A76ED395');
        $this->addSql('DROP INDEX IDX_DA460427A76ED395 ON offers');
        $this->addSql('ALTER TABLE offers DROP user_id');
    }
}
