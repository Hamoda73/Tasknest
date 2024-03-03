<?php

namespace App\Repository;

use App\Entity\Complaint;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Complaint>
 *
 * @method Complaint|null find($id, $lockMode = null, $lockVersion = null)
 * @method Complaint|null findOneBy(array $criteria, array $orderBy = null)
 * @method Complaint[]    findAll()
 * @method Complaint[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ComplaintRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Complaint::class);
    }

    public function getGeneralInquiryComplaints()
    {
        return $this->createQueryBuilder('a')
            ->where('a.type = :type')
            ->setParameter('type', 'general inquiry')
            ->getQuery()
            ->getResult();
    }

    public function getCustomerSupportComplaints()
    {
        return $this->createQueryBuilder('a')
            ->where('a.type = :type')
            ->setParameter('type', 'customer support')
            ->getQuery()
            ->getResult();
    }

    public function getBillingIssueComplaints()
    {
        return $this->createQueryBuilder('a')
            ->where('a.type = :type')
            ->setParameter('type', 'billing issue')
            ->getQuery()
            ->getResult();
    }


public function countAllComplaints()
{
    return $this->createQueryBuilder('c')
        ->select('COUNT(c.id) as totalComplaints')
        ->getQuery()
        ->getSingleScalarResult();
}
public function countGeneralInquiryComplaints()
{
    return $this->createQueryBuilder('c')
        ->select('COUNT(c.id) as generalInquiryComplaintsCount')
        ->where('c.type = :type')
        ->setParameter('type', 'general inquiry')
        ->getQuery()
        ->getSingleScalarResult();
}

public function countBillingIssueComplaints()
{
    return $this->createQueryBuilder('c')
        ->select('COUNT(c.id) as billingIssueComplaintsCount')
        ->where('c.type = :type')
        ->setParameter('type', 'billing issue')
        ->getQuery()
        ->getSingleScalarResult();
}

public function countCustomerSupportComplaints()
{
    return $this->createQueryBuilder('c')
        ->select('COUNT(c.id) as customerSupportComplaintsCount')
        ->where('c.type = :type')
        ->setParameter('type', 'customer support')
        ->getQuery()
        ->getSingleScalarResult();
}




    




    //    /**
    //     * @return Complaint[] Returns an array of Complaint objects
    //     */
    //    public function findByExampleField($value): array
    //    {
    //        return $this->createQueryBuilder('c')
    //            ->andWhere('c.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->orderBy('c.id', 'ASC')
    //            ->setMaxResults(10)
    //            ->getQuery()
    //            ->getResult()
    //        ;
    //    }

    //    public function findOneBySomeField($value): ?Complaint
    //    {
    //        return $this->createQueryBuilder('c')
    //            ->andWhere('c.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->getQuery()
    //            ->getOneOrNullResult()
    //        ;
    //    }
}
