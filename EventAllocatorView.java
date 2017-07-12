package planner.gui;

import planner.*;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * The view for the event allocator program.
 */
public class EventAllocatorView {

	// the model of the event allocator
	private EventAllocatorModel model;
	// main layout which organises all of the contents of the window
	private HBox layout;
	// main scene which holds all of the elements
	private Scene scene;
	// textfield for event name for making allocation
	private TextField eventName = new TextField();
	// textfield for event capacity for making allocation
	private TextField eventCap = new TextField();
	// dropdown for selecting list of venues for making allocation
	private ChoiceBox<Venue> venueSel = new ChoiceBox<Venue>();
	// label for event name in the make allocation section
	private Label eventNameLbl = new Label("Event Name");
	// label for event capacity in the make allocation section
	private Label eventCapLbl = new Label("Event Capacity");
	// label for Venue selection in the make allocation section
	private Label venueSelectLbl = new Label("Venues Available");
	// button for addding allocation
	private Button addAllocationBtn = new Button("Add Event");
	// label for selecting event to remove from allocation
	private Label delEventLbl = new Label("Select Event");
	// dropdwon menu for selecting from a list of available events
	private ChoiceBox<Event> eventSel = new ChoiceBox<Event>();
	// button for removing allocation
	private Button delAllocationBtn = new Button("Delete Allocation");
	// listview for displaying all of the traffic information
	private ListView<String> trafficListView = new ListView<String>();
	// listproperty which holds the data of the traffic
	private ListProperty<String> trafficListProp = new SimpleListProperty<>();
	// list view for displaying all of the allocation
	private ListView<String> allocListView = new ListView<String>();
	// listproperty which holds the data of the allocation
	private ListProperty<String> allocListProp = new SimpleListProperty<>();

	/**
	 * Initialises the view for the event allocator program.
	 * 
	 * @param model
	 *            the model of the event allocator
	 */
	public EventAllocatorView(EventAllocatorModel model) {
		this.model = model;
		layout = new HBox();
		// makes a scene with the HBox as the main layout
		scene = new Scene(layout, 700, 420);
		// adds the left most element to the HBox
		addLeftPane(layout);
		// adds the middle element to the HBox
		allocationLayout(layout);
		// sets right most element to the HBox
		trafficLayout(layout);
		// sets the content of the list property to be a the list which holds
		// corridor information
		trafficListProp.set(FXCollections
				.observableArrayList(model.getCorridorInformation()));
		// binds the traffic ListView to the traffic list property
		trafficListView.itemsProperty().bind(trafficListProp);
		// sets the content of the list property to be the list which holds
		// allocation information
		allocListProp.set(
				FXCollections.observableArrayList(model.getAllocations()));
		// binds the allocation ListView to the allocation list property
		allocListView.itemsProperty().bind(allocListProp);
	}

	/**
	 * Returns the scene for the event allocator application.
	 * 
	 * @return returns the scene for the application
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Clears all of the input boxes and selected items of drop down menus.
	 * 
	 */
	public void clearAll() {
		eventName.clear();
		eventCap.clear();
		venueSel.setValue(null);
		eventSel.setValue(null);
	}

	/**
	 * Updates all of the respective GUI elements after an allocation has been
	 * added by calling helper functions of updating each element.
	 * 
	 */
	public void updateView() {
		updateTrafficText();
		updateAllocationText();
		setEventDropDown(model.getEvents());
	}

	/**
	 * Returns the text inputed in the TextField where event name is required.
	 * 
	 */
	public String getAddEventName() {
		return eventName.getText();
	}

	/**
	 * Returns the text inputed in the TextField where event capacity is
	 * required.
	 * 
	 * @return text from event capacity TextField
	 * 
	 */
	public String getAddEventCap() {
		return eventCap.getText();
	}

	/**
	 * Returns the the selected venue option from the DropDown menu when adding
	 * an allocation.
	 * 
	 * @return a venue object which is the selected item in the DropDown menu
	 * 
	 */
	public Venue getSelectedVenue() {
		return venueSel.getValue();
	}

	/**
	 * Returns the the selected event option from the DropDown menu when
	 * removing an allocation.
	 * 
	 * @return a event object which is the selected item in the DropDown menu
	 * 
	 */
	public Event getSelectedEvent() {
		return eventSel.getValue();
	}

	/**
	 * Creates an alert box for an error type and displays the correct error.
	 * 
	 * @param error
	 *            String which describes the error
	 */
	public void showError(String error) {
		Alert alertWindow = new Alert(AlertType.ERROR, error);
		alertWindow.showAndWait();
	}

	/**
	 * Updates the DropDown menu from which a venue can be selected to be
	 * allocate to an event.
	 * 
	 * @param venueList
	 *            list of venue items read from VenueReader set as the content
	 *            of the drop down menu in add allocation section
	 * 
	 */
	public void setVenueDropDown(List<Venue> venueList) {
		venueSel.setItems(FXCollections.observableArrayList(venueList));
	}

	/**
	 * Adds a handler to the add allocation button.
	 * 
	 * @param handler
	 *            the handler to be added to the button
	 * 
	 */
	public void addEventHandler(EventHandler<ActionEvent> handler) {
		addAllocationBtn.setOnAction(handler);
	}

	/**
	 * Adds a handler to the remove allocation button.
	 * 
	 * @param handler
	 *            the handler to be added to the button
	 * 
	 */
	public void delEventHandler(EventHandler<ActionEvent> handler) {
		delAllocationBtn.setOnAction(handler);
	}

	/**
	 * Adds a TitledPane to the HBox. The TitlePane is set to contain the
	 * ListView which shows the traffic informations.
	 * 
	 * @param layout
	 *            the HBox to add a TitledPane at its right
	 */
	private void trafficLayout(HBox layout) {
		TitledPane trafficPane = new TitledPane
				("Current Traffic", trafficListView);
		// adds the TitledPane to the HBox
		layout.getChildren().add(trafficPane);
		trafficPane.setCollapsible(false);
		//trafficPane.heightProperty().addListener(arg0);
	}

	/**
	 * Adds a TitledPane the HBox. The TitlePane is set 
	 * to contain the ListView which shows the allocations.
	 * 
	 * @param layout
	 * 			 HBox to set its center as a TitledPane
	 */
	private void allocationLayout(HBox layout) {
		TitledPane allocPane = new TitledPane
				("Current Allocations", allocListView);
		layout.getChildren().add(allocPane);
		allocPane.setCollapsible(false);
	}

	/**
	 * Adds VBox to the HBox which contains layouts for adding allocations and
	 * deleting allocations.
	 * 
	 * @param layout
	 *            HBox to add to its left section
	 */
	private void addLeftPane(HBox layout) {
		VBox leftPane = new VBox();
		layout.getChildren().add(leftPane);
		addEventLayout(leftPane);
		delEventLayout(leftPane);
	}

	/**
	 * Adds a TitledPane to a VBox which contains all the elements required to
	 * add an allocation. All of the elements are arranged using a GridPane and
	 * the GridPane is added to the TitledPane.
	 * 
	 * @param vBox
	 *            VBox to add all elements required to add allocations
	 */
	private void addEventLayout(VBox vBox) {
		GridPane addEventLayout = new GridPane();
		// creates a TitledPane and sets the content of it to be the GridPane
		TitledPane pane = new TitledPane("Add Event Section", addEventLayout);
		pane.setCollapsible(false);
		addEventLayout.setVgap(10);
		addEventLayout.setPadding(new Insets(10, 10, 10, 10));
		// adds all of the elements in their respective grid positions
		addEventLayout.add(eventNameLbl, 0, 0);
		addEventLayout.add(eventName, 0, 1);
		addEventLayout.add(eventCapLbl, 0, 2);
		addEventLayout.add(eventCap, 0, 3);
		addEventLayout.add(venueSelectLbl, 0, 4);
		addEventLayout.add(venueSel, 0, 5);
		venueSel.setMinHeight(60);
		venueSel.setMaxHeight(60);
		addEventLayout.add(addAllocationBtn, 0, 6);
		vBox.getChildren().add(pane);
	}

	/**
	 * Adds a TitledPane to a VBox which contains all the elements required to
	 * deleted an allocation. All of the elements are arranged using a GridPane
	 * and the GridPane is added to the TitledPane.
	 * 
	 * @param vBox
	 *            a VBox which contains all of the elements to delete allocation
	 */
	private void delEventLayout(VBox vBox) {
		GridPane delEventLayout = new GridPane();
		TitledPane pane = new TitledPane("Delete Allocation Section",
				delEventLayout);
		pane.setCollapsible(false);
		vBox.getChildren().add(pane);
		delEventLayout.setVgap(10);
		delEventLayout.setPadding(new Insets(10, 10, 10, 10));
		// adding all elements required to remove an allocation
		delEventLayout.add(delEventLbl, 0, 0);
		delEventLayout.add(eventSel, 0, 1);
		delEventLayout.add(delAllocationBtn, 0, 2);
		eventSel.setPrefWidth(170);
	}

	/**
	 * Updates the SimpleListProperty of the allocation list to be
	 * representative of the current allocations list, so that the ListView
	 * which shows it is updated.
	 * 
	 */
	private void updateAllocationText() {
		allocListProp.set(
				FXCollections.observableArrayList(model.getAllocations()));
	}

	/**
	 * Updates the SimpleListProperty of the traffic list to be representative
	 * of the current corridor list, so that the ListView which shows it is
	 * updated.
	 * 
	 */
	private void updateTrafficText() {
		trafficListProp.set(FXCollections
				.observableArrayList(model.getCorridorInformation()));
	}

	/**
	 * Sets the content of the DropDown menu in remove allocation section to be
	 * list of events which are currently allocated.
	 * 
	 */
	private void setEventDropDown(List<Event> eventList) {
		eventSel.setItems(FXCollections.observableArrayList(eventList));
	}
}
