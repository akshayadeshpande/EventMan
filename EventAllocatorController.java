package planner.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import planner.*;

/**
 * The controller for the event allocator program.
 */
public class EventAllocatorController {

    // the model of the event allocator
    private EventAllocatorModel model;
    // the view of the event allocator
    private EventAllocatorView view;
    

    /**
     * Initialises the controller for the event allocator program.
     * 
     * @param model
     *            the model of the event allocator
     * @param view
     *            the view of the event allocator
     */
    public EventAllocatorController(EventAllocatorModel model,
            EventAllocatorView view) {
        this.model = model;
        this.view = view; 
        //adds handler for the add allocation button from view
        view.addEventHandler(new AddEventHandler());
        //adds handler for the delete allocation button from view
        view.delEventHandler(new DelEventHandler());
        try{
        	this.model.addVenues(VenueReader.read("venues.txt"));
        }
    	catch(FormatException | IOException e){
    		view.showError("Could not open file: " + e.getMessage());
    		//exist the program if the file could not be loaded
    		System.exit(1); 
    	}  
        //sets the contents of the DropDown menu to loaded venue list
        view.setVenueDropDown(this.model.getVenues());
    }
    
    /**
     * EventHandler class for the add allocation button
     */    
    private class AddEventHandler implements EventHandler<ActionEvent> {
    	@Override
    	public void handle(ActionEvent event){  		
    		try{
    			String eventName = view.getAddEventName();  
    			String eventCapacity = view.getAddEventCap();
    			Venue eventVenue = view.getSelectedVenue();
    			model.addUserAllocation(eventName, eventCapacity, eventVenue);    	
    			view.updateView();
    			view.clearAll();
    		}
    		catch(IllegalArgumentException | NullPointerException e){
    			view.showError(e.getMessage());
    		}   	    		
    	}
    }
    
    
    /**
     * EventHandler class for the remove allocation button
     */
    private class DelEventHandler implements EventHandler<ActionEvent> {
    	@Override
    	public void handle(ActionEvent event){
    		try{
	    		Event userEvent = view.getSelectedEvent();
	    		model.removeEventAllocations(userEvent);
	    		view.updateView();
    		}
    		catch(IllegalArgumentException | NullPointerException e){
    			view.showError("Select a event to remove ");
    		}
    	}
    }
}
