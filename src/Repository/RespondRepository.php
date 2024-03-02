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

    public function getGeneralInquiryResponds()
    {
        return $this->createQueryBuilder('a')
            ->join('a.Complaint', 'c')
            ->where('c.type = :type')
            ->setParameter('type', 'general inquiry')
            ->getQuery()
            ->getResult();
    }

    public function getCustomerSupportResponds()
    {
        return $this->createQueryBuilder('a')
            ->join('a.Complaint', 'c')
            ->where('c.type = :type')
            ->setParameter('type', 'Customer Support')
            ->getQuery()
            ->getResult();
    }

    public function getBillingIssueResponds()
    {
        return $this->createQueryBuilder('a')
            ->join('a.Complaint', 'c')
            ->where('c.type = :type')
            ->setParameter('type', 'Billing issue')
            ->getQuery()
            ->getResult();
    }

    public function countDoneComplaints()
    {
    $result = $this->createQueryBuilder('a')
        ->leftJoin('a.Complaint', 'c')
        ->select('COUNT(a.id) as doneComplaintCount')
        ->getQuery()
        ->getScalarResult();

    return $result[0]['doneComplaintCount'] ?? 0;
    }

    public function countDoneGeneralInquiryComplaints()
{
    $result = $this->createQueryBuilder('a')
        ->leftJoin('a.Complaint', 'c')
        ->select('COUNT(a.id) as doneGeneralInquiryComplaintCount')
        ->where('c.type = :type')
        ->setParameter('type', 'general inquiry')
        ->getQuery()
        ->getScalarResult();

    return $result[0]['doneGeneralInquiryComplaintCount'] ?? 0;
}

public function countDoneBillingIssueComplaints()
{
    $result = $this->createQueryBuilder('a')
        ->leftJoin('a.Complaint', 'c')
        ->select('COUNT(a.id) as doneBillingIssueComplaintCount')
        ->where('c.type = :type')
        ->setParameter('type', 'billing issue')
        ->getQuery()
        ->getScalarResult();

    return $result[0]['doneBillingIssueComplaintCount'] ?? 0;
}

public function countDoneCustomerSupportComplaints()
{
    $result = $this->createQueryBuilder('a')
        ->leftJoin('a.Complaint', 'c')
        ->select('COUNT(a.id) as doneCustomerSupportComplaintCount')
        ->where('c.type = :type')
        ->setParameter('type', 'customer support')
        ->getQuery()
        ->getScalarResult();

    return $result[0]['doneCustomerSupportComplaintCount'] ?? 0;
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
