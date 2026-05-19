module br.senac.sp.games_omega {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;
    requires java.sql;

    opens br.senac.sp.games_omega to javafx.fxml;
    opens br.senac.sp.games_omega.model to javafx.base;
    exports br.senac.sp.games_omega;
}