<?php



use App\Entity\Produit;
use Doctrine\ORM\EntityManagerInterface;

class StatsProduitService
{
    private $em;
    
    public function __construct(EntityManagerInterface $em)
    {
        $this->em = $em;
    }
    
    public function getStats(): array
    {
        $repo = $this->em->getRepository(Produit::class);
        
        return [
            'total' => $repo->count([]),
            'stock_critique' => $repo->createQueryBuilder('p')
                ->select('COUNT(p.idProduit)')
                ->where('p.quantiteProduit <= :seuil')
                ->setParameter('seuil', Produit::STOCK_CRITIQUE)
                ->getQuery()
                ->getSingleScalarResult(),
            'stock_moyen' => $repo->createQueryBuilder('p')
                ->select('COUNT(p.idProduit)')
                ->where('p.quantiteProduit > :seuil_critique AND p.quantiteProduit <= :seuil_moyen')
                ->setParameter('seuil_critique', Produit::STOCK_CRITIQUE)
                ->setParameter('seuil_moyen', Produit::STOCK_MOYEN)
                ->getQuery()
                ->getSingleScalarResult(),
            'valeur_stock' => $repo->createQueryBuilder('p')
                ->select('SUM(p.prixProduit * p.quantiteProduit)')
                ->getQuery()
                ->getSingleScalarResult()
        ];
    }
}