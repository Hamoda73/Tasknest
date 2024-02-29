<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240227115554 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE complaint ADD user_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE complaint ADD CONSTRAINT FK_5F2732B5A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('CREATE INDEX IDX_5F2732B5A76ED395 ON complaint (user_id)');
        $this->addSql('ALTER TABLE respond DROP FOREIGN KEY FK_99C5D563EDAE188E');
        $this->addSql('ALTER TABLE respond ADD CONSTRAINT FK_99C5D563EDAE188E FOREIGN KEY (complaint_id) REFERENCES complaint (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE complaint DROP FOREIGN KEY FK_5F2732B5A76ED395');
        $this->addSql('DROP INDEX IDX_5F2732B5A76ED395 ON complaint');
        $this->addSql('ALTER TABLE complaint DROP user_id');
        $this->addSql('ALTER TABLE respond DROP FOREIGN KEY FK_99C5D563EDAE188E');
        $this->addSql('ALTER TABLE respond ADD CONSTRAINT FK_99C5D563EDAE188E FOREIGN KEY (complaint_id) REFERENCES complaint (id) ON DELETE CASCADE');
    }
}
