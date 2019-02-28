package db.eco;

import db.JocDB;
import db.JocGeneralDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(JocGeneralDB.class)
public class JocGeneralDB_ { 

    public static volatile SingularAttribute<JocGeneralDB, String> numeJocGeneral;
    public static volatile CollectionAttribute<JocGeneralDB, JocDB> jocDBCollection;
    public static volatile SingularAttribute<JocGeneralDB, String> descriereJoc;
    public static volatile SingularAttribute<JocGeneralDB, Integer> idJocGeneral;

}