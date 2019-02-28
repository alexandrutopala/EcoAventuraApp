package Magazin.db.eco;

import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(ProdusDB.class)
public class ProdusDB_ { 

    public static volatile SingularAttribute<ProdusDB, String> observatii;
    public static volatile SingularAttribute<ProdusDB, Integer> stoc;
    public static volatile SingularAttribute<ProdusDB, String> denumire;
    public static volatile SingularAttribute<ProdusDB, Float> pret;
    public static volatile CollectionAttribute<ProdusDB, TranzactieDB> tranzactieDBCollection;
    public static volatile SingularAttribute<ProdusDB, Integer> idProdus;

}