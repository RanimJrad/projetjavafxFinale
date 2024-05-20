package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class clientController implements Initializable{

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button availbleBooks_btn;

    @FXML
    private Button purchase_btn;

    @FXML
    private Button logout;

    @FXML
    private Label username;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label home_nameIm;

    @FXML
    private Label home_nameIm1;

    @FXML
    private Label home_nameIm11;

    @FXML
    private Label home_nameIm2;

    @FXML
    private Label home_nameIm12;

    @FXML
    private Label home_nameIm111;

    @FXML
    private AnchorPane availableBooks_form;

    @FXML
    private TextField availableBooks_search;

    @FXML
    private TableView<bookData> availableBooks_tableView;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_bookID;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_bookTitle;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_author;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_genre;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_date;

    @FXML
    private TableColumn<bookData, String> availableBooks_col_price;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_bookID;

    @FXML
    private ComboBox<?> purchase_bookTitle;

    @FXML
    private Label purchase_total;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private Label purchase_info_bookID;

    @FXML
    private Label purchase_info_bookTitle;

    @FXML
    private Label purchase_info_author;

    @FXML
    private Label purchase_info_genre;

    @FXML
    private Label purchase_info_date;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private TableColumn<customerData, String> purchase_col_bookID;

    @FXML
    private TableColumn<customerData, String> purchase_col_bookTitle;

    @FXML
    private TableColumn<customerData, String> purchase_col_author;

    @FXML
    private TableColumn<customerData, String> purchase_col_genre;

    @FXML
    private TableColumn<customerData, String> purchase_col_quantity;

    @FXML
    private TableColumn<customerData, String> purchase_col_price;

    @FXML
    private GridPane menu_gridPane;
    
    
    private SpinnerValueFactory<Integer> spinner;
	private int customerId;
	private Connection connect;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;
	
	private ObservableList<bookData> cardListData= FXCollections.observableArrayList();
	
	public ObservableList<bookData> bookGetData(){
		
		String sql = "select * from book";
		
		ObservableList<bookData> listData = FXCollections.observableArrayList();
		connect = connexion.connectDb();
		
		try {
			
			prepare = connect.prepareStatement(sql);
			result=prepare.executeQuery();
			bookData book;
			
			while(result.next()) {
				book =  new bookData (result.getString("title"),result.getString("image"));
				listData.add(book);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}
	
	public void bookDisplayCard() {
		
		cardListData.clear();
		cardListData.addAll(bookGetData());
		
		int row=0;
		int column=0;
		
		menu_gridPane.getRowConstraints().clear();
		menu_gridPane.getColumnConstraints().clear();
		
		for (int q=0 ; q<cardListData.size();q++) {
			try {
			FXMLLoader load = new FXMLLoader();
			load.setLocation(getClass().getResource("/FXML/cardBook.FXML"));
			AnchorPane pane = load.load();
			cardBookController cardC = load.getController();
			cardC.setData(cardListData.get(q));
			
			if(column ==4) {
				column=0;
				row+=1;
			}
			
			menu_gridPane.add(pane,column++ , row);
			GridPane.setMargin(pane, new Insets(50,5,5,20));
					
		}catch (Exception e) {
			e.printStackTrace();
		}}
		
	}
	
	public void purchaseBookId() {

		String sql = "SELECT book_id FROM book";

		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			ObservableList listData = FXCollections.observableArrayList();

			while (result.next()) {
				listData.add(result.getString("book_id"));
			}

			purchase_bookID.setItems(listData);
			purchaseBookTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void purchaseBookTitle() {

		String sql = "SELECT book_id, title FROM book WHERE book_id = '"
				+ purchase_bookID.getSelectionModel().getSelectedItem() + "'";

		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			ObservableList listData = FXCollections.observableArrayList();

			while (result.next()) {
				listData.add(result.getString("title"));
			}

			purchase_bookTitle.setItems(listData);

			purchaseBookInfo();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void purchaseBookInfo() {

		String sql = "SELECT * FROM book WHERE title = '" + purchase_bookTitle.getSelectionModel().getSelectedItem()
				+ "'";

		connect = connexion.connectDb();

		String bookId = "";
		String title = "";
		String author = "";
		String genre = "";
		String date = "";

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			if (result.next()) {
				bookId = result.getString("book_id");
				title = result.getString("title");
				author = result.getString("author");
				genre = result.getString("genre");
				date = result.getString("pub_date");
			}

			purchase_info_bookID.setText(bookId);
			purchase_info_bookTitle.setText(title);
			purchase_info_author.setText(author);
			purchase_info_genre.setText(genre);
			purchase_info_date.setText(date);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public ObservableList<customerData> purchaseListData() {
		purchasecustomerId();
		String sql = "SELECT * FROM customer WHERE customer_id = '" + customerId + "'";

		ObservableList<customerData> listData = FXCollections.observableArrayList();

		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			customerData customerD;

			while (result.next()) {
				customerD = new customerData(result.getInt("customer_id"), result.getInt("book_id"),
						result.getString("title"), result.getString("author"), result.getString("genre"),
						result.getInt("quantity"), result.getDouble("price"), result.getDate("date"));

				listData.add(customerD);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}
	
	private ObservableList<customerData> purchaseCustomerList;

	public void purchaseShowCustomerListData() {
		purchaseCustomerList = purchaseListData();

		purchase_col_bookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		purchase_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		purchase_col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
		purchase_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

		purchase_tableView.setItems(purchaseCustomerList);

	}

    public void purchaseDisplayQTY() {
		spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
		purchase_quantity.setValueFactory(spinner);
	}

	private int qty;
	
	public void purhcaseQty() {
		qty = purchase_quantity.getValue();
	}
	
	private double totalP;

	public void purchaseAdd() {
		purchasecustomerId();

		String sql = "INSERT INTO customer (customer_id, book_id, title, author, genre, quantity, price, date) "
				+ "VALUES(?,?,?,?,?,?,?,?)";

		connect = connexion.connectDb();

		try {
			Alert alert;

			if (purchase_bookTitle.getSelectionModel().getSelectedItem() == null
					|| purchase_bookID.getSelectionModel().getSelectedItem() == null) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error message");
				alert.setHeaderText(null);
				alert.setContentText("Please choose book first");
				alert.showAndWait();
			} else {

				prepare = connect.prepareStatement(sql);
				prepare.setString(1, String.valueOf(customerId));
				prepare.setString(2, purchase_info_bookID.getText());
				prepare.setString(3, purchase_info_bookTitle.getText());
				prepare.setString(4, purchase_info_author.getText());
				prepare.setString(5, purchase_info_genre.getText());
				prepare.setString(6, String.valueOf(qty));

				String checkData = "SELECT title, price FROM book WHERE title = '"
						+ purchase_bookTitle.getSelectionModel().getSelectedItem() + "'";

				double priceD = 0;

				statement = connect.createStatement();
				result = statement.executeQuery(checkData);

				if (result.next()) {
					priceD = result.getDouble("price");
				}

				totalP = (qty * priceD);

				prepare.setString(7, String.valueOf(totalP));

				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());

				prepare.setString(8, String.valueOf(sqlDate));

				prepare.executeUpdate();

				purchaseDisplayTotal();
				purchaseShowCustomerListData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void purchasecustomerId() {

		String sql = "SELECT MAX(customer_id) FROM customer";
		int checkCID = 0;
		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			if (result.next()) {
				customerId = result.getInt("MAX(customer_id)");
			}

			String checkData = "SELECT MAX(customer_id) FROM customer_info";

			prepare = connect.prepareStatement(checkData);
			result = prepare.executeQuery();

			if (result.next()) {
				checkCID = result.getInt("MAX(customer_id)");
			}

			if (customerId == 0) {
				customerId += 1;
			} else if (checkCID == customerId) {
				customerId = checkCID + 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private double displayTotal;

	public void purchaseDisplayTotal() {
		purchasecustomerId();

		String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '" + customerId + "'";

		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			if (result.next()) {
				displayTotal = result.getDouble("SUM(price)");
			}

			purchase_total.setText("DNT  " + String.valueOf(displayTotal));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void purchasePay() {

		String sql = "INSERT INTO customer_info (customer_id, total, date) " + "VALUES(?,?,?)";

		connect = connexion.connectDb();

		try {
			Alert alert;
			if (displayTotal == 0) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error message");
				alert.setHeaderText(null);
				alert.setContentText("Invalid ");
				alert.showAndWait();
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure?");
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					prepare = connect.prepareStatement(sql);
					prepare.setString(1, String.valueOf(customerId));
					prepare.setString(2, String.valueOf(displayTotal));
					Date date = new Date();
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					prepare.setString(3, String.valueOf(sqlDate));
					prepare.executeUpdate();

					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information message");
					alert.setHeaderText(null);
					alert.setContentText("Successful!");
					alert.showAndWait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ObservableList<bookData> availableBooksListData() {

		ObservableList<bookData> listData = FXCollections.observableArrayList();
		String sql = "SELECT * FROM book";

		connect = connexion.connectDb();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			bookData bookD;

			while (result.next()) {
				bookD = new bookData(result.getInt("book_id"), result.getString("title"), result.getString("author"),
						result.getString("genre"), result.getDate("pub_date"), result.getDouble("price"),
						result.getString("image"));

				listData.add(bookD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}

	private ObservableList<bookData> availableBooksList;

	public void availableBooksShowListData() {
		availableBooksList = availableBooksListData();

		availableBooks_col_bookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		availableBooks_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		availableBooks_col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
		availableBooks_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		availableBooks_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
		availableBooks_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

		availableBooks_tableView.setItems(availableBooksList);
	}
	
	public void availableBooksSeach() {
	    FilteredList<bookData> filteredData = new FilteredList<>(availableBooksList, b -> true);

	    availableBooks_search.textProperty().addListener((observable, oldValue, newValue) -> {
	        filteredData.setPredicate(book -> {
	            if (newValue == null || newValue.isEmpty()) {
	                return true;
	            }

	            String lowerCaseFilter = newValue.toLowerCase();

	            if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
	                return true; 
	            }
	            return false;
	        });

	        availableBooks_tableView.setItems(filteredData);
	    });
	}
	
	public void switchForm(ActionEvent event) {

		if (event.getSource() == dashboard_btn) {
			dashboard_form.setVisible(true);
			availableBooks_form.setVisible(false);
			purchase_form.setVisible(false);

			dashboard_btn.setStyle("-fx-background-color:#D7C3A2;");
			availbleBooks_btn.setStyle("-fx-background-color: transparent");
			purchase_btn.setStyle("-fx-background-color: transparent");
			 

		} else if (event.getSource() == availbleBooks_btn) {
			dashboard_form.setVisible(false);
			availableBooks_form.setVisible(true);
			purchase_form.setVisible(false);

			availbleBooks_btn.setStyle("-fx-background-color:#D7C3A2;");
			dashboard_btn.setStyle("-fx-background-color: transparent");
			purchase_btn.setStyle("-fx-background-color: transparent");

			availableBooksShowListData();
			availableBooksSeach();

		} else if (event.getSource() == purchase_btn) {
			dashboard_form.setVisible(false);
			availableBooks_form.setVisible(false);
			purchase_form.setVisible(true);

			purchase_btn.setStyle("-fx-background-color:#D7C3A2;");
			availbleBooks_btn.setStyle("-fx-background-color: transparent");
			dashboard_btn.setStyle("-fx-background-color: transparent");

			purchaseBookTitle();
			purchaseBookId();
			purchaseShowCustomerListData();
			purchaseDisplayQTY();
			purchaseDisplayTotal();

		}
	}

	private double x = 0;
	private double y = 0;

	public void logout() {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Message");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to logout?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {

				logout.getScene().getWindow().hide();
				Parent root = FXMLLoader.load(getClass().getResource("/FXML/SignIn.FXML"));
				Stage stage = new Stage();
				Scene scene = new Scene(root);

				root.setOnMousePressed((MouseEvent event) -> {
					x = event.getSceneX();
					y = event.getSceneY();
				});

				root.setOnMouseDragged((MouseEvent event) -> {
					stage.setX(event.getScreenX() - x);
					stage.setY(event.getScreenY() - y);

					stage.setOpacity(.8);
				});

				root.setOnMouseReleased((MouseEvent event) -> {
					stage.setOpacity(1);
				});

				stage.initStyle(StageStyle.TRANSPARENT);

				stage.setScene(scene);
				stage.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		System.exit(0);
	}

	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	public void displayUsername() {
		String user = getData.username;
		user = user.substring(0, 1).toUpperCase() + user.substring(1);
		username.setText(user);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		displayUsername();	
		bookDisplayCard();
	}

}
