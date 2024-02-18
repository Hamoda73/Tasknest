<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240216214547 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application ADD cv VARCHAR(255) NOT NULL, DROP salary_d, CHANGE dispo dispo DATETIME NOT NULL');
        $this->addSql('ALTER TABLE offers DROP img');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application ADD salary_d DOUBLE PRECISION NOT NULL, DROP cv, CHANGE dispo dispo VARCHAR(20) NOT NULL');
        $this->addSql('ALTER TABLE offers ADD img VARCHAR(20) NOT NULL');
    }
}
