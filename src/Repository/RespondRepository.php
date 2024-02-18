<?php

namespace App\Repository;

use App\Entity\Respond;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Respond>
 *
 * @method Respond|null find($id, $lockMode = null, $lockVersion = null)
 * @method Respond|null findOneBy(array $criteria, array $orderBy = null)
 * @method Respond[]    findAll()
 * @method Respond[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class RespondRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Respond::class);
    }

//    /**
//     * @return Respond[] Returns an array of Respond objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('r.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Respond
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
