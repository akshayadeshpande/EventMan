package planner.gui;

import planner.*;

import java.util.*;

/**
 * The model for the event allocator program.
 */
public class EventAllocatorModel {
	// List which stores all of the loaded venues from venues.txt file
	private static List<Venue> venues;
	// List which stores all of the allocated events
	private static ArrayList<Event> events;
	// HashMap which stores the allocation of an event to a venue
	private static HashMap<Event, Venue> allocations;
	// Traffic object which keeps track of traffic caused by all allocations
	private static Traffic trafficMain;
	
	/* invariant: 
	 * 
	 * one event will be allocated to only one venue &&
	 * 
	 * one venue will be allocated to only one event &&
	 * 
	 * traffic generated from all allocations would be safe &&
	 * 
	 * no venue or event will be mapped to null values &&
	 * 
	 * list of allocated events may not contain duplicate events
     * 
     */

	/**
	 * Initialises the model for the event allocator program.
	 */
	public EventAllocatorModel() {		
		venues = new ArrayList<Venue>();		
		events = new ArrayList<Event>();		
		allocations = new HashMap<Event, Venue>();		
		trafficMain = new Traffic();
	}

	/**
	 * Returns the list of allocated events.
	 * 
	 * @return events list which contains all of the allocated events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * Adds a list of venues to the current list stored in the model. Used to
	 * add venues loaded by VenueReader in the controller
	 * 
	 * @param venueList
	 *            list which contains venues to add to the model's list of
	 *            venues
	 */
	public void addVenues(List<Venue> venueList) {
		venues.addAll(venueList);
	}

	/**
	 * Removes an event from the list of allocated events.
	 * 
	 * @param event
	 *            event to be removed from list of allocated events
	 */
	public void removeEvent(Event event) {
		events.remove(event);
	}

	/**
	 * Returns an list of string representations of each corridor and the
	 * traffic on it.
	 * 
	 * @return result list which contains a string representation of a corridor
	 *         and its traffic at each index
	 */
	public List<String> getCorridorInformation() {
		List<String> result = new ArrayList<String>();
		// Iterates through the set of corridors with traffic and adds a string
		// representation of corridor and its traffic
		for (Corridor corridor : trafficMain.getCorridorsWithTraffic()) {
			result.add(corridor.toString() + " : "
					+ trafficMain.getTraffic(corridor));
		}
		// sorts the list of strings
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns the venue that an event is allocated to in allocations.
	 * 
	 * @param event
	 *            event which is allocated in allocations
	 * 
	 * @return venue which is allocated to event passed as the paramater
	 */
	public Venue getAllocatedVenue(Event event) {
		return allocations.get(event);
	}

	/**
	 * Returns an list of string representations of mapping in the HashMap which
	 * stores the allocations.
	 * 
	 * @return result list which contains a string representation for each
	 *         mapping of event to its venue
	 */
	public List<String> getAllocations() {
		List<String> result = new ArrayList<String>();
		// iterates through the allocations HashMap and creates string for each
		// mapping using .toString methods of event and venue
		for (Event event : allocations.keySet()) {
			result.add(String.format("%s : %s (%d)", 
					event.toString(), allocations.get(event).getName()
							,allocations.get(event).getCapacity()));
		}
		// Sorts the list of string
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns the list of venues loaded from the venues.txt file.
	 * 
	 * @return venues list which contains all of the loaded venue
	 */
	public List<Venue> getVenues() {
		return venues;
	}

	/**
	 * Attempts to add an allocation after making a new event object. If any of
	 * the conditions required to make an event or allocate it to a venue are
	 * not satisfied, throws a IllegalArgumentException with appropriate
	 * message.
	 * 
	 * @param name
	 *            name of the event to be added to allocations
	 * @param capacity
	 *            capacity of the event as a to be added to allocations
	 * @param venue
	 *            venue to the be allocated the new event to
	 * 
	 * @throws IllegalArgumentException
	 *             if name of event is null or if the new traffic generated
	 *             might be unsafe or if the venue cannot host the event or if
	 *             the venue or event are already allocated
	 */
	public void addUserAllocation(String name, String capacity, Venue venue)
			throws IllegalArgumentException {
		try {
			if (name.isEmpty()) {
				throw new IllegalArgumentException("Invalid event name");
			}
			if(capacity.isEmpty()){
				throw new IllegalArgumentException("Invalid event capacity");
			}
			int capacityInt = Integer.parseInt(capacity);
			if (venue == null) {
				throw new IllegalArgumentException(
						"Select venue to allocate event to");
			}
			// attempts to make a new event object based on parameters
			Event newEvent = new Event(name, capacityInt);
			Traffic trafficGenerated = venue.getTraffic(newEvent);
			// makes a copy of the traffic generated from allocation
			Traffic trafficGeneratedCopy = venue.getTraffic(newEvent);
			// adds the main traffic object to the copy to test if the traffic
			// resulting from the new allocation is safe
			trafficGeneratedCopy.addTraffic(trafficMain);
			
			// checks if the copy after adding the main traffic is safe or not
			if (!trafficGeneratedCopy.isSafe()) {
				throw new IllegalArgumentException(
						"Traffic generated is not safe");
			}
			// checks if venue can host the event
			if (!venue.canHost(newEvent)) {
				throw new IllegalArgumentException(
						"Selected venue cannot host event");
			}
			// check if the event is already in the allocations
			if (allocations.containsKey(newEvent)) {
				throw new IllegalArgumentException(
						"Event is already allocated");
			}
			// checks if the venue is already in allocations
			if (allocations.containsValue(venue)) {
				throw new IllegalArgumentException(
						"Venue is already allocated");
			}			
			// if all conditions are satisfied, add mapping
			allocations.put(newEvent, venue);
			// adds the new traffic to the main traffic object
			trafficMain.addTraffic(trafficGenerated);
			// adds new event o list of allocated events
			events.add(newEvent);
			// catches any error which may be resulted from trying to make a new
			// event
		} catch(NumberFormatException e){
			throw new IllegalArgumentException("Invalid event capacity");
		} catch (NullPointerException | IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Attempts to delete an from the allocations based on the event to be
	 * deleted.
	 * 
	 * @param event
	 *            event to be deleted from the allocations
	 * 
	 * @throws IllegalArgumentException
	 *             				if event is null
	 */
	public void removeEventAllocations(Event event)
			throws IllegalArgumentException {
		if (event.equals(null)) {
			throw new IllegalArgumentException();
		}
		// checks traffic generated from allocation of the specific event
		Traffic trafficGenerated = allocations.get(event).getTraffic(event);
		// for all corridors shared with other allocations, subtracts the
		//traffic caused by this allocation from the current traffic on the 
		//corridors
		for (Corridor corridor : trafficGenerated.getCorridorsWithTraffic()) {
			trafficMain.updateTraffic(corridor,
					-1 * trafficGenerated.getTraffic(corridor));
		}
		//removes the event from allocations and list of allocated events
		allocations.remove(event);
		events.remove(event);
	}
	
	/**
     * Determines whether this class is internally consistent (i.e. it satisfies
     * its class invariant).
     * 
     * @return true if this class is internally consistent, and false otherwise.
     */
    public boolean checkInvariant() {
    	if(allocations.containsKey(null) || allocations.containsValue(null)){
    		return false;
    	}
    	List<Venue> venuesList = (List<Venue>) allocations.values();
    	Set<Event> eventSet = allocations.keySet();
    	if(venuesList.size() != eventSet.size()){
    		return false;
    	}
    	if(!trafficMain.isSafe()){
    		return false;
    	}
    	if(events.size() != eventSet.size()){
    		return false;
    	}
       return true;
    }
}
