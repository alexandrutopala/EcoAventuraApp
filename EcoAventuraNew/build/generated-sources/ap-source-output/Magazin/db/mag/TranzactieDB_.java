package Magazin.db.mag;

import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(TranzactieDB.class)
public class TranzactieDB_ { 

    public static volatile SingularAttribute<TranzactieDB, String> observatii;
    public static volatile SingularAttribute<TranzactieDB, Integer> idTranzactie;
    public static volatile SingularAttribute<TranzactieDB, String> data;
    public static volatile SingularAttribute<TranzactieDB, Float> pret;
    public static volatile SingularAttribute<TranzactieDB, ProdusDB> idProdus;
    public static volatile SingularAttribute<TranzactieDB, Boolean> adaugate;
    public static volatile SingularAttribute<TranzactieDB, Integer> bucati;

}