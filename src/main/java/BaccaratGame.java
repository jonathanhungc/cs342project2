import javafx.application.Application;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class BaccaratGame extends Application {

	// used for game logic
	ArrayList<Card> playerHand;
	ArrayList<Card> bankerHand;
	BaccaratDealer theDealer;
	BaccaratGameLogic gameLogic;
	double currentBet; // stores the current bet of the user
	double totalWinnings; // stores all the winnings of the user
	String handBet; // to store where the user is betting

	// event handlers
	EventHandler<ActionEvent> goToPlayingScene, goToResultsPopup;

	// JavaFX members
	HashMap<String, Scene> sceneMap;
	MenuBar menuBar;
	Menu options;
	MenuItem exitMenu, freshStart;
	Button buttonBidPlayer, buttonBidBanker, buttonBidDraw, buttonEndGame, buttonStartGame;

	Text textTotalWinnings;
	TextField textFieldBid;


	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("BACCARAT");

		// map to hold scenes
		sceneMap = new HashMap<String,Scene>();

		// game members
		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();
		playerHand = new ArrayList<>();
		bankerHand = new ArrayList<>();

		theDealer.shuffleDeck(); // creating and shuffling deck

		// these members are used for the menubar --------------------------
		menuBar = new MenuBar();
		options = new Menu("OPTIONS");
		exitMenu = new MenuItem("EXIT");
		exitMenu.setOnAction(e -> Platform.exit());
		freshStart = new MenuItem("FRESH START");
		freshStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				currentBet = 0;
				totalWinnings = 0;
			}
		});
		options.getItems().add(exitMenu);
		options.getItems().add(freshStart);
		menuBar.getMenus().add(options);
		// ---------------------------------------------------------------------

		// buttons to bid for player, banker or draw, and text field to enter bid -------
		buttonBidPlayer = createBidButton("PLAYER");
		buttonBidBanker = createBidButton("BANKER");
		buttonBidDraw = createBidButton("DRAW");

		textFieldBid = new TextField();
		textFieldBid.setPromptText("ENTER YOUR BID");
		textFieldBid.setPrefSize(260, 35);
		textFieldBid.setStyle("-fx-background-radius: 1em; ");

		textTotalWinnings = new Text("0");
		textTotalWinnings.setStyle("-fx-background-radius: 1em; " +
				"-fx-background-color: white");
		textTotalWinnings.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));
		// ----------------------------------------------------------------------------

		// button to start game and go to playing scene
		buttonStartGame = new Button("Start");
		buttonStartGame.setPrefSize(150, 75);
		buttonStartGame.setStyle("-fx-background-radius: 1em; " +
				"-fx-background-color: #afc7bb;" +
				"-fx-font-size: 20;" +
				"-fx-font-family: 'Courier New';" +
				"-fx-font-weight: bold;");
		buttonStartGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				primaryStage.setScene(createPlayingScene());
				primaryStage.centerOnScreen();
			}
		});

		// button to exit game and go to the results screen
		buttonEndGame = new Button("EXIT");
		buttonEndGame.setPrefSize(100, 35);
		buttonEndGame.setStyle("-fx-background-radius: 1em; " +
						"-fx-background-color: #afc7bb;");
		buttonEndGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				primaryStage.setScene(createResultsScene());
				primaryStage.centerOnScreen();
			}
		});

		// this event does the game logic of the game, and displays the results in the pop up game
		// TODO: display the playing cards in the playingScene
		goToResultsPopup = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				Button b = (Button) actionEvent.getSource();

				// set where the user is betting
				if (b.getText().equals("PLAYER"))
					handBet = "Player";

				else if (b.getText().equals("BANKER"))
					handBet = "Banker";

				else
					handBet = "Draw";

				currentBet = Double.parseDouble(textFieldBid.getText()); // set the current bid

				playerHand = theDealer.dealHand(); // deal player hand
				bankerHand = theDealer.dealHand(); // deal banker hand

				// checking if player or bank should be given third cards
				if (!gameLogic.evaluatePlayerDraw(playerHand)) { // stand player, hand value is 6-7

					if (gameLogic.handTotal(bankerHand) <= 5) { // hit banker, hand value is 0-5
						bankerHand.add(theDealer.drawOne());
					}

					// stand banker, hand value is 6-7

				} else { // hit player, hand value is 0-5

					Card playerThirdCard = theDealer.drawOne();
					playerHand.add(playerThirdCard); // add another card to player hand

					if (gameLogic.evaluateBankerDraw(bankerHand, playerThirdCard)) { // hit banker, get a third card
						bankerHand.add(theDealer.drawOne());
					}

					// stand banker, do nothing
				}

				getResultsPopup().show();
			}
		};

		sceneMap.put("playingScene", createPlayingScene());
		sceneMap.put("startScene", createStartScene());

		primaryStage.setScene(sceneMap.get("startScene"));
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	// used to create bid buttons
	private Button createBidButton(String name) {

		Button b = new Button(name);
		b.setPrefSize(80, 35);
		b.setStyle("-fx-background-radius: 1em; " +
				"-fx-background-color: #afc7bb;");

		return b;
	}

	// method used to get card icons
	public ImageView getCardIcon(Card card) {
		String cardVal = Integer.toString(card.value);
		String cardSuit = card.suite;

		switch (cardVal) {
			case "11":
				cardVal = "jack";
				break;
			case "12":
				cardVal = "queen";
				break;
			case "13":
				cardVal = "king";
				break;
			case "14":
				cardVal = "ace";
				break;
		}

		String filename = cardVal + "_of_" + cardSuit + ".png";

		ImageView cardIcon = new ImageView(new Image("icons/" + filename));
		cardIcon.setFitHeight(181.5);
		cardIcon.setFitWidth(125);

		return cardIcon;
	}

	public Scene createPlayingScene() {

		// text for playing scene --------------------------------------------------------------------
		Text textPlayer = new Text("PLAYER");
		textPlayer.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 40));

		Text textBanker = new Text("BANKER");
		textBanker.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 40));

		Text textCurrentEarnings = new Text("CURRENT EARNINGS");
		textCurrentEarnings.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));
		// -----------------------------------------------------------------------------------------

		buttonBidPlayer.setOnAction(goToResultsPopup);
		buttonBidBanker.setOnAction(goToResultsPopup);
		buttonBidDraw.setOnAction(goToResultsPopup);

		// holds all elements related to the player
		VBox playerArea = new VBox();
		playerArea.setPrefSize(670, 400);
		playerArea.setAlignment(Pos.CENTER);

		// horizontal for the player cards
		HBox playerCards = new HBox(30);
		playerCards.setPrefSize(670, 300);
		playerCards.setAlignment(Pos.CENTER);

		// adding test cards
		playerCards.getChildren().add(getCardIcon(new Card("clubs", 2)));
		playerCards.getChildren().add(getCardIcon(new Card("clubs", 5)));
		playerCards.getChildren().add(getCardIcon(new Card("diamonds", 12)));

		// add text for player and area for player cards
		playerArea.getChildren().add(textPlayer);
		playerArea.getChildren().add(playerCards);


		// holds all elements related to the banker
		VBox bankerArea = new VBox();
		bankerArea.setPrefSize(670, 400);
		bankerArea.setAlignment(Pos.CENTER);

		// horizontal for the banker cards
		HBox bankerCards = new HBox(30);
		bankerCards.setPrefSize(670, 300);
		bankerCards.setAlignment(Pos.CENTER);

		// adding test cards
		bankerCards.getChildren().add(getCardIcon(new Card("hearts", 8)));
		bankerCards.getChildren().add(getCardIcon(new Card("spades", 11)));

		// add text for banker and area for banker cards
		bankerArea.getChildren().add(textBanker);
		bankerArea.getChildren().add(bankerCards);


		// box with an image, buttons to bid, and text area to enter bid
		HBox bidArea = new HBox(15, new ImageView(new Image("icons/casino-chip.png")),
				new VBox(10, new HBox(10, buttonBidPlayer, buttonBidBanker, buttonBidDraw), textFieldBid));

		// bottom features with bidding area and current winnings
		HBox bottom = new HBox(100, bidArea, new VBox(10, textCurrentEarnings, this.textTotalWinnings));
		bottom.setPrefSize(1400, 100);
		bottom.setAlignment(Pos.CENTER);

		// main pane to hold all the elements
		BorderPane playingArea = new BorderPane();
		playingArea.setTop(menuBar);
		playingArea.setBottom(bottom);
		playingArea.setLeft(playerArea);
		playingArea.setRight(bankerArea);
		playingArea.setStyle("-fx-background-color: #278a2e;");

		return new Scene(playingArea, 1400, 600);
	}

	public Scene createStartScene() {
		Text textStart = new Text("Welcome to the casino!");
		textStart.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 40));

		ImageView imageCasino = new ImageView(new Image("icons/casino.png"));
		imageCasino.setFitHeight(170);
		imageCasino.setFitWidth(170);

		// holds an image of a casino, welcome message and start button
		VBox startBox = new VBox(50, imageCasino, textStart, buttonStartGame);
		startBox.setAlignment(Pos.CENTER);
		startBox.setStyle("-fx-background-color: #278a2e;");

		return new Scene(startBox,600, 500);
	}

	public Scene createResultsScene() {

		Text textResults = new Text("RESULTS");
		textResults.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 40));

		String resultsMessage = "";

		if (totalWinnings > 0)
			resultsMessage = "CONGRATULATIONS, YOU WON " + totalWinnings;
		else if (totalWinnings < 0)
			resultsMessage = "SORRY, YOU LOST " + totalWinnings;

		Text textWinnings = new Text(resultsMessage);
		textWinnings.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 30));

		VBox resultsBox = new VBox(70, textResults, textWinnings);
		resultsBox.setAlignment(Pos.CENTER);
		resultsBox.setStyle("-fx-background-color: #278a2e;");

		return new Scene(resultsBox,600, 500);
	}

	public Stage getResultsPopup() {

		String winner, earnings = "", userBet = "";

		Stage resultsPopup = new Stage();

		Text textPlayerTotal = new Text("PLAYER TOTAL: " + gameLogic.handTotal(playerHand));
		textPlayerTotal.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));

		Text textBankerTotal = new Text("BANKER TOTAL: " + gameLogic.handTotal(bankerHand));
		textBankerTotal.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));

		// get winner of the game
		winner = gameLogic.whoWon(playerHand, bankerHand);

		// set text depending on game winner
        switch (winner) {
            case "Player":
                winner = "PLAYER WINS!";
                break;
            case "Banker":
                winner = "BANKER WINS!";
                break;
            case "Draw":
                winner = "DRAW!";
                break;
        }

		Text winnerText = new Text(winner);
		winnerText.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 30));

		// set text depending on where user bet
		switch (handBet) {
			case "Player":
				userBet = "PLAYER";
				break;
			case "Banker":
				userBet = "BANKER";
				break;
			case "Draw":
				userBet = "DRAW";
				break;
		}

		Text userSelection = new Text("YOU BET: " + currentBet + " ON " + userBet);
		userSelection.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));

		// the winnings/losses of the current game for the user
		double roundWinnings = evaluateWinnings();
		if (roundWinnings > 0)
			earnings = "CONGRATULATIONS, YOU WON " + roundWinnings;
		else if (roundWinnings < 0)
			earnings = "SORRY, YOU LOST " + roundWinnings;

		Text earningsMessage = new Text(earnings);
		earningsMessage.setFont(Font.font("courier new", FontWeight.BOLD, FontPosture.REGULAR, 20));

		// set the text that displays the total winnings
		textTotalWinnings.setText(Double.toString(totalWinnings));

		// box to display the hand total from the player and the banker
		HBox totalsBox = new HBox(30, textPlayerTotal, textBankerTotal);
		totalsBox.setAlignment(Pos.CENTER);

		// button to continue playing
		Button buttonNextGame = new Button("CONTINUE");
		buttonNextGame.setPrefSize(100, 35);
		buttonNextGame.setStyle("-fx-background-radius: 1em; " +
				"-fx-background-color: #afc7bb;");
		buttonNextGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				textFieldBid.clear(); // clears the current bid
				resultsPopup.close(); // closes this pop up screen
			}
		});

		// horizontal box to hold the button to the next game and to end the game
		HBox options = new HBox(40, buttonNextGame, buttonEndGame);
		options.setAlignment(Pos.CENTER);

		// holds everything together: shows the winner, total for player and bank, user selection, user earnings,
		// and options (continue playing/exit game)
		VBox resultsBox = new VBox(40, winnerText, totalsBox, userSelection, earningsMessage, options);
		resultsBox.setAlignment(Pos.CENTER);
		resultsBox.setStyle("-fx-background-color: white;" +
							"-fx-border-width: 5;" +
							"-fx-border-color: #278a2e");

		Scene resultsScene = new Scene(resultsBox,500, 400);
		resultsPopup.setScene(resultsScene);
		return resultsPopup;
	}

	public double evaluateWinnings() {

		String winner  = gameLogic.whoWon(playerHand, bankerHand);

		// user bet on winning side (player, bank or tie)
		if (Objects.equals(winner, handBet)) {
			 totalWinnings += currentBet;
			 return currentBet;
		}

		else {
			totalWinnings -= currentBet;
			return currentBet * -1;
		}

	}

}
