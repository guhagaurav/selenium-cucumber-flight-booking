package dto;

public class AirlineDetailsDTO implements Comparable<AirlineDetailsDTO>{
	
	private String airlineName;
	private String airlineTime;
	private int airlineFare;
	private int index;
	
	public AirlineDetailsDTO(String airlineName, String airlineTime, int airlineFare, int index) {
		super();
		this.airlineName = airlineName;
		this.airlineTime = airlineTime;
		this.airlineFare = airlineFare;
		this.index = index;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getAirlineTime() {
		return airlineTime;
	}

	public void setAirlineTime(String airlineTime) {
		this.airlineTime = airlineTime;
	}

	public int getAirlineFare() {
		return airlineFare;
	}

	public void setAirlineFare(int airlineFare) {
		this.airlineFare = airlineFare;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int compareTo(AirlineDetailsDTO airlineDetailsDTO) {
		  return (this.getAirlineFare() < airlineDetailsDTO.getAirlineFare() ? -1 : 
	            (this.getAirlineFare() == airlineDetailsDTO.getAirlineFare() ? 0 : 1));  
	}

	@Override
	public String toString() {
		return "AirlineDetailsDTO [airlineName=" + airlineName + ", airlineTime=" + airlineTime + ", airlineFare="
				+ airlineFare + ", index=" + index + "]";
	}   
	
}
