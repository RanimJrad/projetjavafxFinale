module MiniProjetFinal {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.base,javafx.graphics, javafx.fxml;
}
