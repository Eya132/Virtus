<?php

namespace App\Repository;

use App\Entity\Produit;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class ProduitRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Produit::class);
    }

    public function findMostOrderedProducts(int $maxResults = 5): array
{
    return $this->createQueryBuilder('p')
        ->select('p as produit', 'COUNT(c.id) as orderCount')
        ->leftJoin('p.commandes', 'c')
        ->groupBy('p.id')
        ->orderBy('orderCount', 'DESC')
        ->setMaxResults($maxResults)
        ->getQuery()
        ->getResult();
}

    // Add custom methods as needed
}