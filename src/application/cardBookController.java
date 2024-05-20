package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class cardBookController implements Initializable {

	@FXML
	private AnchorPane card_form;

	@FXML
	private ImageView imageView;

	@FXML
	private Label book_name;
	
	private bookData bookD;
	private Image image;
	
	public void setData(bookData bookD) {
		this.bookD=bookD;
		book_name.setText(bookD.getTitle());
		String path = "file:" + bookD.getImage();
		image =new Image(path , 173,174, false , true);
		imageView.setImage(image);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
