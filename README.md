# Snowflake Code Off
2017/03/04

Created by Scott Williams

##Background
Snowflake Software's Laminar Data platform provides a number of data APIs for application developers building applications for airlines, airports and air traffic control. It serves information about flights, aeronautical information (airspaces, airports, routes etc.) and weather. Snowflake receives information about flights from a range of commercial and public data sources. These include schedules from airlines, flight plans filed by pilots, and aircraft surveillance from radar and aircraft transponders (ADS-B).

The various messages we receive about flights are not correlated. They each contain different subsets of information about the flight but have no common identifier. To get the whole picture of a flight we need to cross reference all the messages about that flight to build up a single, canonical representation.

##The Problem
###User Story
**AS** A Laminar Data product manager

**I WANT** to know which flight messages relate to the same flight

**SO THAT I** can tell my customers the current state of the flight

What we want you to do is compare flight messages with each other to work out what flights exist and which messages relate to each flight. We have provided you with a file containing a set of flight messages. This includes the flight messages and also, to help you work out how well you are doing, the set of flight identifiers (Globally Unique Flight Identifiers - GUFIs) we have assigned to the messages.

##Solution
Create an SQL database from the given .csv files then add weights to records which have the same plane, departure aerodrome, and destination aerodrome, among other things, to find which records are likely beyond reasonable doubt to belong to the same flight.