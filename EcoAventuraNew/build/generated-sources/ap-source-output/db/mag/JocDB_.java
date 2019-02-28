package db.mag;

import db.EchipaDB;
import db.JocDB;
import db.JocGeneralDB;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-19T19:19:04")
@StaticMetamodel(JocDB.class)
public class JocDB_ { 

    public static volatile SingularAttribute<JocDB, String> data;
    public static volatile SingularAttribute<JocDB, String> locatie;
    public static volatile SingularAttribute<JocDB, String> post;
    public static volatile SingularAttribute<JocDB, Integer> idJoc;
    public static volatile SingularAttribute<JocDB, Long> idProgram;
    public static volatile SingularAttribute<JocDB, Boolean> absent;
    public static volatile SingularAttribute<JocDB, JocGeneralDB> jocGeneralidJocGeneral;
    public static volatile SingularAttribute<JocDB, EchipaDB> echipaidEchipa;
    public static volatile SingularAttribute<JocDB, Integer> punctaj;
    public static volatile SingularAttribute<JocDB, String> organizator;

}