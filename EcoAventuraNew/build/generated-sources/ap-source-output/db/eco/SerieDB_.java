package db.eco;

import db.EchipaDB;
import db.SerieDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(SerieDB.class)
public class SerieDB_ { 

    public static volatile SingularAttribute<SerieDB, String> dataInceput;
    public static volatile SingularAttribute<SerieDB, Integer> idSerie;
    public static volatile CollectionAttribute<SerieDB, EchipaDB> echipaDBCollection;
    public static volatile SingularAttribute<SerieDB, Integer> numarSerie;

}