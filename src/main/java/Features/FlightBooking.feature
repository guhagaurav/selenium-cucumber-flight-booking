Feature: Flight booking

Scenario: To select cheapest fastest evening flight
	Given I login into the application
	And I click on oneway trip
	And I enter from destination 'PNQ - Pune, India' and to destination 'HYD - Hyderabad, India'
	And I enter travel date 'Aug 17 2020'
	When I click on search
	When I click on shortest tab
	Then I select cheapest shortest evening flight
	

	