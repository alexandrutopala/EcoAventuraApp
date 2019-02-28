package db.mag;

import db.ActivitateDB;
import db.EchipaDB;
import db.JocDB;
import db.MembruEchipaDB;
import db.SerieDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(EchipaDB.class)
public class EchipaDB_ { 

    public static volatile SingularAttribute<EchipaDB, String> numeEchipa;
    public static volatile SingularAttribute<EchipaDB, String> profEchipa;
    public static volatile SingularAttribute<EchipaDB, String> culoareEchipa;
    public static volatile CollectionAttribute<EchipaDB, MembruEchipaDB> membruEchipaDBCollection;
    public static volatile SingularAttribute<EchipaDB, String> scoalaEchipa;
    public static volatile SingularAttribute<EchipaDB, Integer> idEchipa;
    public static volatile CollectionAttribute<EchipaDB, JocDB> jocDBCollection;
    public static volatile CollectionAttribute<EchipaDB, ActivitateDB> activitateDBCollection;
    public static volatile SingularAttribute<EchipaDB, SerieDB> serieidSerie;

}