<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240210212531 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application ADD offers_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE application ADD CONSTRAINT FK_A45BDDC1A090B42E FOREIGN KEY (offers_id) REFERENCES offers (id)');
        $this->addSql('CREATE INDEX IDX_A45BDDC1A090B42E ON application (offers_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application DROP FOREIGN KEY FK_A45BDDC1A090B42E');
        $this->addSql('DROP INDEX IDX_A45BDDC1A090B42E ON application');
        $this->addSql('ALTER TABLE application DROP offers_id');
    }
}
