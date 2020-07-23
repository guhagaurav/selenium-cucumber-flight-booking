package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dto.AirlineDetailsDTO;

public class AirlineFareSorter {
	List<AirlineDetailsDTO> airlineDetail = new ArrayList<>();  
	

	  public AirlineFareSorter(List<AirlineDetailsDTO> airlinePmList) {         
	    this.airlineDetail = airlinePmList;     
	  }       
	  
	  public List<AirlineDetailsDTO> getSortedAirlineByFare() {         
	    Collections.sort(airlineDetail);         
	    return airlineDetail;     
	  } 

}
