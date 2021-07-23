package uk.sylinsic.custom;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.XSD;

/**
 * Loads an ontology and adds our custom fields to it.
 */
public class CustomOntology {
    
    private CustomOntology() {}
    
    /**
     * Creates an OWL ontology from HQDM and our custom implementation.
     *
     * @return The built ontology model
     */
    public static final OntModel buildOntology() {
        // Begin declare the ontology prefixes we'll be using
        final String customPre = "http://www.semanticweb.org/magma-core/ontologies/custom#";
        final String hqdmPre = "http://www.semanticweb.org/magma-core/ontologies/hqdm#";
        // End declare the ontology prefixes we'll be using

        // Begin create an OWL ontology and load in the default HQDM ontololgy
        final OntModel ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ontology.read("/usr/src/mymaven/HQDM/hqdm.owl");
        // End create an OWL ontology and load in the default HQDM ontololgy

        // Begin create default custom prefixes
        ontology.setNsPrefix("magmardl", "http://www.semanticweb.org/magma-core/rdl#");
        ontology.setNsPrefix("magmauser", "http://www.semanticweb.org/magma-core/user#");
        ontology.setNsPrefix("hqdm", "http://www.semanticweb.org/hqdm#");
        ontology.setNsPrefix("hqdmontol", hqdmPre);
        ontology.setNsPrefix("customont", customPre);
        // End create default custom prefixes
        
        // Begin create classes
        // Begin create classes for general usage
        final OntClass dimensions = ontology.createClass(customPre + "dimensions");
        dimensions.addComment("The class of the dimensions of a component.", "en");
        dimensions.addSuperClass(ontology.getOntClass(hqdmPre + "physical_quantity"));

        final OntClass functionality = ontology.createClass(customPre + "functionality");
        functionality.addComment("The class of the functionality of a component.", "en");
        functionality.addSuperClass(ontology.getOntClass(hqdmPre + "physical_property"));
        // End create classes for general usage        

        // Begin create classes for environment
        final OntClass weatherSystem = ontology.createClass(customPre + "weather_system");
        weatherSystem.addComment("The class of a weather system of a location.", "en");
        weatherSystem.addComment("Subclass of functional_system_component.", "en");
        weatherSystem.addSuperClass(ontology.getOntClass(hqdmPre + "functional_system_component"));
        // End create classes for environment

        // Begin create classes for building components
        final OntClass buildingComponent = ontology.createClass(customPre + "building_component");
        buildingComponent.addComment("The high-level class of a building's components.", "en");
        buildingComponent.addComment("Subclass of functional_system_component.", "en");
        buildingComponent.addSuperClass(ontology.getOntClass(hqdmPre + "functional_system_component"));
        
        final OntClass building = ontology.createClass(customPre + "building");
        building.addComment("The class of a building itself.", "en");
        building.addComment("Subclass of building_component.", "en");
        building.addSuperClass(buildingComponent);
        
        final OntClass floor = ontology.createClass(customPre + "floor");
        floor.addComment("The class of a floor (level) of a building.", "en");
        floor.addComment("Not to be mistaken for the physical flooring of a room.", "en");
        floor.addComment("Subclass of building_component.", "en");
        floor.addSuperClass(buildingComponent);
        
        final OntClass room = ontology.createClass(customPre + "room");
        room.addComment("The class of a room of a building.", "en");
        room.addComment("Subclass of building_component.", "en");
        room.addSuperClass(buildingComponent);
        
        final OntClass wall = ontology.createClass(customPre + "wall");
        wall.addComment("The class of a wall of a building.", "en");
        wall.addComment("Subclass of building_component.", "en");
        wall.addSuperClass(buildingComponent);
        // End create classes for building components
        
        // Begin create classes for sensors
        final OntClass sensor = ontology.createClass(customPre + "sensor");
        sensor.addComment("The class of a sensor of a building.", "en");
        sensor.addComment("Subclass of building_component", "en");
        sensor.addSuperClass(buildingComponent);
        
        final OntClass temperatureSensor = ontology.createClass(customPre + "temperature_sensor");
        temperatureSensor.addComment("The class of a temperature sensor of a building.", "en");
        temperatureSensor.addComment("Subclass of sensor", "en");
        temperatureSensor.addSuperClass(sensor);
        
        final OntClass motionSensor = ontology.createClass(customPre + "motion_sensor");
        motionSensor.addComment("The class of a motion sensor of a building.", "en");
        motionSensor.addComment("Subclass of sensor", "en");
        motionSensor.addSuperClass(sensor);
        // End create classes for sensors

        // Begin create classes for heating system
        final OntClass heatingSystem = ontology.createClass(customPre + "heating_system");
        heatingSystem.addComment("The class of an entire heating system.", "en");
        heatingSystem.addComment("Subclass of functional_system_component.", "en");
        heatingSystem.addSuperClass(ontology.getOntClass(hqdmPre + "functional_system_component"));
        
        final OntClass heatingSystemComponent = ontology.createClass(customPre + "heating_system_component");
        heatingSystemComponent.addComment("The class of a heating system component of a building.", "en");
        heatingSystemComponent.addComment("Subclass of building_component & heating_system.", "en");
        heatingSystemComponent.addSuperClass(buildingComponent);
        heatingSystemComponent.addSuperClass(heatingSystem);
        
        final OntClass radiator = ontology.createClass(customPre + "radiator");
        radiator.addComment("The class of a radiator of a heating system.", "en");
        radiator.addComment("Subclass of heating_system_component.", "en");
        radiator.addSuperClass(heatingSystemComponent);
        
        final OntClass thermostat = ontology.createClass(customPre + "thermostat");
        thermostat.addComment("The class of a thermostat of a heating system.", "en");
        thermostat.addComment("Subclass of heating_system_component.", "en");
        thermostat.addSuperClass(heatingSystemComponent);
        
        final OntClass boiler = ontology.createClass(customPre + "boiler");
        boiler.addComment("The class of a boiler of a heating system.", "en");
        boiler.addComment("Subclass of heating_system_component.", "en");
        boiler.addSuperClass(heatingSystemComponent);
        // End create classes for heating system
        // End create classes

        // Begin create properties
        // Begin create properties for building components
        final ObjectProperty buildingLocation = ontology.createObjectProperty(customPre + "building_location");
        buildingLocation.addComment("The property of the location of a building.", "en");
        buildingLocation.addComment("Domain: building", "en");
        buildingLocation.addComment("Range: string", "en");
        buildingLocation.addDomain(building);
        buildingLocation.addRange(XSD.xstring);
        
        final ObjectProperty hasDimensions = ontology.createObjectProperty(customPre + "has_dimensions");
        hasDimensions.addComment("The property of the dimensions of a building component.", "en");
        hasDimensions.addComment("Domain: building_component", "en");
        hasDimensions.addComment("Range: dimensions", "en");
        hasDimensions.addDomain(buildingComponent);
        hasDimensions.addRange(dimensions);
        
        final ObjectProperty numFloors = ontology.createObjectProperty(customPre + "number_of_floors");
        numFloors.addComment("The property of the number of floors (levels) in a building.", "en");
        numFloors.addComment("Domain: building", "en");
        numFloors.addComment("Range: integer", "en");
        numFloors.addDomain(building);
        numFloors.addRange(XSD.integer);
        
        final ObjectProperty numRooms = ontology.createObjectProperty(customPre + "number_of_rooms");
        numRooms.addComment("The property of the number of rooms in a building/on a floor (level).", "en");
        numRooms.addComment("Domain: building, room", "en");
        numRooms.addComment("Range: integer", "en");
        numRooms.addDomain(building);
        numRooms.addDomain(floor);
        numRooms.addRange(XSD.integer);
        
        final ObjectProperty numWalls = ontology.createObjectProperty(customPre + "number_of_walls");
        numWalls.addComment("The property of the number of walls in a building/on a floor (level).", "en");
        numWalls.addComment("Domain: building, floor", "en");
        numWalls.addComment("Range: integer", "en");
        numWalls.addDomain(building);
        numWalls.addDomain(floor);        
        numWalls.addRange(XSD.integer);

        final ObjectProperty position = ontology.createObjectProperty(customPre + "position");
        position.addComment("The property of the position of a floor, room or wall.", "en");
        position.addComment("Domain: floor, room, wall", "en");
        position.addComment("Range: string", "en");
        position.addDomain(floor);
        position.addDomain(room);
        position.addDomain(wall);
        position.addRange(XSD.xstring);
        // End create properties for building components
        
        // Begin create properties for environment
        final ObjectProperty humidity = ontology.createObjectProperty(customPre + "humidity");
        humidity.addComment("The property of the humidity of a weather system.", "en");
        humidity.addComment("Domain: state_of_weather_system", "en");
        humidity.addComment("Range: decimal", "en");
        humidity.addDomain(stateOfWeatherSystem);
        humidity.addRange(XSD.decimal);

        final ObjectProperty cloudCover = ontology.createObjectProperty(customPre = "cloud_cover");
        cloudCover.addComment("The property of the cloud cover of a weather system.", "en");
        cloudCover.addComment("Domain: state_of_weather_system", "en");
        cloudCover.addComment("Range: decimal", "en");
        cloudCover.addDomain(stateOfWeatherSystem);
        cloudCover.addRange(XSD.decimal);

        final ObjectProperty temperature = ontology.createObjectProperty(customPre + "temperature");
        temperature.addComment("The property of the temperature of a floor (level), room or weather system.", "en");
        temperature.addComment("Domain: state_of_floor, state_of_room, state_of_temperature_sensor, state_of_weather_system", "en");
        temperature.addComment("Range: decimal", "en");
        temperature.addDomain(stateOfFloor);
        temperature.addDomain(stateOfRoom);
        temperature.addDomain(stateOfWeatherSystem);
        temperature.addRange(XSD.decimal);
        // End create properties for environment

        // Begin create properties for heating system states
        final ObjectProperty boilerPowerStatus = ontology.createObjectProperty(customPre + "boiler_power_status");
        boilerPowerStatus.addComment("The property of the power status of a boiler state.", "en");
        boilerPowerStatus.addComment("Domain: state_of_boiler", "en");
        boilerPowerStatus.addComment("Range: boolean", "en");
        boilerPowerStatus.addDomain(stateOfBoiler);
        boilerPowerStatus.addRange(XSD.xboolean);
        
        final ObjectProperty boilerPerformanceSetting = ontology
        .createObjectProperty(customPre + "boiler_performance_setting");
        boilerPerformanceSetting.addComment("The property of the performance setting of a boiler state.", "en");
        boilerPerformanceSetting.addComment("Domain: state_of_boiler", "en");
        boilerPerformanceSetting.addComment("Range: decimal", "en");
        boilerPerformanceSetting.addDomain(stateOfBoiler);
        boilerPerformanceSetting.addRange(XSD.decimal);
        
        final ObjectProperty radiatorFlowAdjustment = ontology.createObjectProperty(customPre
        + "radiator_flow_adjustment");
        radiatorFlowAdjustment.addComment("The property of the flow adjustment of a radiator state.", "en");
        radiatorFlowAdjustment.addComment("Domain: state_of_radiator", "en");
        radiatorFlowAdjustment.addComment("Range: decimal", "en");
        radiatorFlowAdjustment.addDomain(stateOfRadiator);
        radiatorFlowAdjustment.addRange(XSD.decimal);
        
        final ObjectProperty thermostatTemperatureSetting = ontology
        .createObjectProperty(customPre + "thermostat_temperature_setting");
        thermostatTemperatureSetting.addComment("The property of the temperature setting of a thermostat state.", "en");
        thermostatTemperatureSetting.addComment("Domain: state_of_thermostat", "en");
        thermostatTemperatureSetting.addComment("Range: decimal", "en");
        thermostatTemperatureSetting.addDomain(stateOfThermostat);
        thermostatTemperatureSetting.addRange(XSD.decimal);
        // End create properties for heating system states
        
        // Begin create properties for general usage
        final ObjectProperty width = ontology.createObjectProperty(customPre + "width");
        width.addComment("The property of the width of a component.", "en");
        width.addComment("Domain: dimensions", "en");
        width.addComment("Range: decimal", "en");
        width.addDomain(dimensions);
        width.addRange(XSD.decimal);
        
        final ObjectProperty height = ontology.createObjectProperty(customPre + "height");
        height.addComment("The property of the height of a component.", "en");
        height.addComment("Domain: dimensions", "en");
        height.addComment("Range: decimal", "en");
        height.addDomain(dimensions);
        height.addRange(XSD.decimal);
        
        final ObjectProperty length = ontology.createObjectProperty(customPre + "length");
        length.addComment("The property of the length of a component.", "en");
        length.addComment("Domain: dimensions", "en");
        length.addComment("Range: decimal", "en");
        length.addDomain(dimensions);
        length.addRange(XSD.decimal);

        final ObjectProperty functionalityStatus = ontology.createObjectProperty(customPre + "functionality_status");
        functionalityStatus.addComment("The property of the functionality status of a component.", "en");
        functionalityStatus.addComment("Domain: functionality", "en");
        functionalityStatus.addComment("Range: boolean", "en");
        functionalityStatus.addDomain(stateOfFunctionality);
        functionalityStatus.addRange(XSD.xboolean);

        final ObjectProperty functionalityDescription = ontology.createObjectProperty(customPre + "functionality_description");
        functionalityDescription.addComment("The property of the description of a component's functionality");
        functionalityDescription.addComment("Domain: functionality", "en");
        functionalityDescription.addComment("Range: string", "en");
        functionalityDescription.addDomain(stateOfFunctionality);
        functionalityDescription.addRange(XSD.xstring);
        // End create properties for general usage
        // End create properties

        // Begin create relations
        // Begin create relations for between rooms
        final ObjectClass roomRelation = ontology.createClass(customPre + "room_relation");
        roomRelation.addComment("The class for relations between rooms.", "en");
        roomRelation.addSuperClass(ontology.getOntClass(hqdmPre + "relationship"));

        final ObjectProperty hasRoomRelation = ontology.createObjectProperty(customPre + "has_room_relation");
        hasRoomRelation.addComment("The property of the relationship of a room with another.", "en");
        hasRoomRelation.addComment("Domain: room", "en");
        hasRoomRelation.addComment("Range: room_relation", "en");
        hasRoomRelation.addDomain(room);
        hasRoomRelation.addRange(roomRelation);

        final ObjectProperty relatedRoom = ontology.createObjectProperty(customPre + "related_room");
        relatedRoom.addComment("The property of a related room.", "en");
        relatedRoom.addComment("Domain: room_relation", "en");
        relatedRoom.addComment("Range: room", "en");
        relatedRoom.addDomain(roomRelation);
        relatedRoom.addRange(room);

        final ObjectProperty relatedRoomDirection = ontology.createObjectProperty(customPre + "related_room_direction");
        relatedRoomDirection.addComment("The property for the direction of a related room");
        relatedRoomDirection.addComment("Domain: room_relation", "en");
        relatedRoomDirection.addComment("Range: string", "en");
        relatedRoomDirection.addDomain(roomRelation);
        relatedRoomDirection.addRange(XSD.xstring);
        // End create relations between rooms

        // Begin create relations for building components
        final ObjectProperty onFloor = ontology.createObjectProperty(customPre + "on_floor");
        onFloor.addComment("The property of what floor (level) a building component is on.", "en");
        onFloor.addComment("Domain: heating_system_component, room, sensor, wall", "en");
        onFloor.addComment("Range: floor", "en");
        onFloor.addDomain(heatingSystemComponent);
        onFloor.addDomain(room);
        onFloor.addDomain(sensor);
        onFloor.addDomain(wall);
        onFloor.addRange(floor);
        
        final ObjectProperty hasRoom = ontology.createObjectProperty(customPre + "has_room");
        hasRoom.addComment("The property of which room(s) a building or floor (level) contains.", "en");
        hasRoom.addComment("Domain: building, floor", "en");
        hasRoom.addComment("Range: room", "en");
        hasRoom.addDomain(building);
        hasRoom.addDomain(floor);
        hasRoom.addRange(room);
        
        final ObjectProperty inBuilding = ontology.createObjectProperty(customPre + "in_building");
        inBuilding.addComment("The property of which building a component is in.", "en");
        inBuilding.addComment("Domain: floor, heating_system_component, room, sensor, wall", "en");
        inBuilding.addComment("Range: building", "en");
        inBuilding.addDomain(floor);
        inBuilding.addDomain(heatingSystemComponent);
        inBuilding.addDomain(room);
        inBuilding.addDomain(sensor);
        inBuilding.addDomain(wall);
        inBuilding.addRange(building);
        
        final ObjectProperty hasWall = ontology.createObjectProperty(customPre + "has_wall");
        hasWall.addComment("The property of which which wall(s) a building, room or floor (level) contanis.", "en");
        hasWall.addComment("Domain: building, floor, room", "en");
        hasWall.addComment("Range: wall", "en");
        hasWall.addDomain(building);
        hasWall.addDomain(floor);
        hasWall.addDomain(room);
        hasWall.addRange(wall);
        
        final ObjectProperty inRoom = ontology.createObjectProperty(customPre + "in_room");
        inRoom
            .addComment("The property of which room a wall(s), heating system component(s) or sensor(s) is in.", "en");
        inRoom.addComment("Domain: heating_system_component, sensor, wall", "en");
        inRoom.addComment("Range: room", "en");
        inRoom.addDomain(heatingSystemComponent);
        inRoom.addDomain(sensor);
        inRoom.addDomain(wall);
        inRoom.addDomain(room);
        // End create relations for building components
        
        // Begin create relations for heating system components
        final ObjectProperty hasHeatingSystemComponent = ontology
        .createObjectProperty(customPre + "has_heating_system_component");
        hasHeatingSystemComponent.addComment("The property of which heating system component(s) a "
            + "building, floor (level), room or wall contains.", "en");
        hasHeatingSystemComponent.addComment("Domain: building, floor, room, wall", "en");
        hasHeatingSystemComponent.addComment("Range: heating_system_component", "en");
        hasHeatingSystemComponent.addDomain(building);
        hasHeatingSystemComponent.addDomain(floor);
        hasHeatingSystemComponent.addDomain(room);
        hasHeatingSystemComponent.addDomain(wall);
        hasHeatingSystemComponent.addRange(heatingSystemComponent);
        
        final ObjectProperty hasRadiator = ontology.createObjectProperty(customPre + "has_radiator");
        hasRadiator.addComment("The property of which radiator(s) a building, floor (level), " 
            + "room or wall contains.", "en");
        hasRadiator.addComment("Domain: building, floor, room, wall", "en");
        hasRadiator.addComment("Range: radiator", "en");
        hasRadiator.addSuperProperty(hasHeatingSystemComponent);
        clearRange(hasRadiator);
        hasRadiator.addRange(radiator);
        
        final ObjectProperty hasThermostat = ontology.createObjectProperty(customPre + "has_thermostat");
        hasThermostat
            .addComment("The property of which thermostat(s) a building, floor (level), room or wall contains.", "en");
        hasThermostat.addComment("Domain: building, floor, room, wall", "en");
        hasThermostat.addComment("Range: thermostat", "en");
        hasThermostat.addSuperProperty(hasHeatingSystemComponent);
        clearRange(hasThermostat);
        hasThermostat.addRange(thermostat);
        
        final ObjectProperty hasBoiler = ontology.createObjectProperty(customPre + "has_boiler");
        hasBoiler.addComment("The property of which boiler(s) a building, floor (level), room or wall contains.", "en");
        hasBoiler.addComment("Domain: building, floor, room, wall", "en");
        hasBoiler.addComment("Range: boiler", "en");
        hasBoiler.addSuperProperty(hasHeatingSystemComponent);
        clearRange(hasBoiler);
        hasBoiler.addRange(boiler);
        
        final ObjectProperty controlledByThermostat = ontology.createObjectProperty(customPre
            + "controlled_by_thermostat");
        controlledByThermostat
            .addComment("The property of which thermostat is controlling a boiler or radiator.", "en");
        controlledByThermostat.addComment("Domain: boiler, radiator", "en");
        controlledByThermostat.addComment("Range: thermostat", "en");
        controlledByThermostat.addDomain(boiler);
        controlledByThermostat.addDomain(radiator);
        controlledByThermostat.addRange(thermostat);
        
        final ObjectProperty thermostatIsControlling = ontology.createObjectProperty(customPre
            + "thermostat_is_controlling");
        thermostatIsControlling
            .addComment("The property of which boiler or radiator a thermostat is controlling.", "en");
        thermostatIsControlling.addComment("Domain: thermostat", "en");
        thermostatIsControlling.addComment("Range: boiler, radiator", "en");
        thermostatIsControlling.addDomain(thermostat);
        thermostatIsControlling.addRange(boiler);
        thermostatIsControlling.addRange(radiator);
        
        final ObjectProperty providingBoiler = ontology.createObjectProperty(customPre + "providing_boiler");
        providingBoiler.addComment("The property of which boiler is providing a radiator with a heat source.", "en");
        providingBoiler.addComment("Domain: radiator", "en");
        providingBoiler.addComment("Range: boiler", "en");
        providingBoiler.addDomain(radiator);
        providingBoiler.addRange(boiler);
        // End create relations for heating system components

        // Begin create relations for general usage
        final ObjectProperty hasFunctionality = ontology.createObjectProperty(customPre + "has_functionality");
        hasFunctionality.addComment("The property of a component's functionality.", "en");
        hasFunctionality.addComment("Domain: building_component, heating_system_component", "en");
        hasFunctionality.addComment("Range: functionality", "en");
        hasFunctionality.addDomain(buildingComponent);
        hasFunctionality.addDomain(heatingSystemComponent);
        hasFunctionality.addRange(functionality);
        // End create relations for general usage
        // End create relations

        // Begin create states
        // Begin create states for building components
        final OntClass stateOfBuildingComponent = ontology.createClass(customPre + "state_of_building_component");
        stateOfBuildingComponent.addComment("The state of a building component.", "en");
        stateOfBuildingComponent.addComment("Subclass of state_of_functional_system_component.", "en");
        stateOfBuildingComponent.addSuperClass(ontology.getOntClass(hqdmPre + "state_of_functional_system_component"));
        
        final OntClass stateOfBuilding = ontology.createClass(customPre + "state_of_building");
        stateOfBuilding.addComment("The state of a building.", "en");
        stateOfBuilding.addComment("Subclass of state_of_building_component.", "en");
        stateOfBuilding.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfRoom = ontology.createClass(customPre + "state_of_room");
        stateOfRoom.addComment("The state of a room.", "en");
        stateOfRoom.addComment("Subclass of state_of_building_component.", "en");
        stateOfRoom.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfFloor = ontology.createClass(customPre + "state_of_floor");
        stateOfFloor.addComment("The state of a floor (level).", "en");
        stateOfFloor.addComment("Subclass of state_of_building_component.", "en");
        stateOfFloor.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfWall = ontology.createClass(customPre + "state_of_wall");
        stateOfWall.addComment("The state of a wall.", "en");
        stateOfWall.addComment("Subclass of state_of_building_component.", "en");
        stateOfWall.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfHeatingSystemComponent = ontology.createClass(customPre
            + "state_of_heating_system_component");
        stateOfHeatingSystemComponent.addComment("The state of a heating system component.", "en");
        stateOfHeatingSystemComponent.addComment("Subclass of state_of_building_component.", "en");
        stateOfHeatingSystemComponent.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfRadiator = ontology.createClass(customPre + "state_of_radiator");
        stateOfRadiator.addComment("The state of a radiator.", "en");
        stateOfRadiator.addComment("Subclass of state_of_heating_system_component.", "en");
        stateOfRadiator.addSuperClass(stateOfHeatingSystemComponent);
        
        final OntClass stateOfThermostat = ontology.createClass(customPre + "state_of_thermostat");
        stateOfThermostat.addComment("The state of a thermostat.", "en");
        stateOfThermostat.addComment("Subclass of state_of_heating_system_component.", "en");
        stateOfThermostat.addSuperClass(stateOfHeatingSystemComponent);
        
        final OntClass stateOfBoiler = ontology.createClass(customPre + "state_of_boiler");
        stateOfBoiler.addComment("The state of a boiler.", "en");
        stateOfBoiler.addComment("Subclass of state_of_heating_system_component.", "en");
        stateOfBoiler.addSuperClass(stateOfHeatingSystemComponent);
        
        final OntClass stateOfSensor = ontology.createClass(customPre + "state_of_sensor");
        stateOfSensor.addComment("The state of a sensor.", "en");
        stateOfSensor.addComment("Subclass of state_of_building_component.", "en");
        stateOfSensor.addSuperClass(stateOfBuildingComponent);
        
        final OntClass stateOfTemperatureSensor = ontology.createClass(customPre + "state_of_temperature_sensor");
        stateOfTemperatureSensor.addComment("The state of a temperature sensor.", "en");
        stateOfTemperatureSensor.addComment("Subclass of state_of_sensor.", "en");
        stateOfTemperatureSensor.addSuperClass(stateOfSensor);
        
        final OntClass stateOfMotionSensor = ontology.createClass(customPre + "state_of_motion_sensor");
        stateOfMotionSensor.addComment("The state of a motion sensor.", "en");
        stateOfMotionSensor.addComment("Subclass of state_of_sensor.", "en");
        stateOfMotionSensor.addSuperClass(stateOfSensor);
        // End create states for building components
        
        // Begin create states for environment components
        final OntClass stateOfWeatherSystem = ontology.createClass(customPre + "state_of_weather_system");
        stateOfWeatherSystem.addComment("The state of a weather system.", "en");
        stateOfWeatherSystem.addComment("Subclass of state_of_functional_system_component.", "en");
        stateOfWeatherSystem.addSuperClass(ontology.getOntClass(hqdmPre + "state_of_functional_system_component"));
        // End create states for environment components

        // Begin create states for general usage
        final OntClass stateOfFunctionality = ontology.createClass(customPre = "state_of_functionality");
        stateOfFunctionality.addComment("The state of functionality of a component", "en");
        stateOfFunctionality.addComment("Subclass of state_of_functional_system_component.", "en");
        stateOfFunctionality.addSuperClass(ontology.getOntClass(hqdmPre + "state_of_functional_system_component"));
        // End create states for general usage

        // Begin create states for heating system components
        final ObjectProperty heatingSystemComponentState = ontology
            .createObjectProperty(customPre + "heating_system_component_state");
        heatingSystemComponentState.addComment("The property of the state of a heating system component.", "en");
        heatingSystemComponentState.addComment("Domain: heating_system_component", "en");
        heatingSystemComponentState.addComment("Range: state_of_heating_system_component", "en");
        heatingSystemComponentState.addDomain(heatingSystemComponent);
        heatingSystemComponentState.addRange(stateOfHeatingSystemComponent);
        
        final ObjectProperty radiatorState = ontology.createObjectProperty(customPre + "radiator_state");
        radiatorState.addComment("The property of the state of a radiator.", "en");
        radiatorState.addComment("Domain: radiator", "en");
        radiatorState.addComment("Range: state_of_radiator", "en");
        radiatorState.addDomain(radiator);
        radiatorState.addRange(stateOfRadiator);
        
        final ObjectProperty thermostatState = ontology.createObjectProperty(customPre + "thermostat_state");
        thermostatState.addComment("The property of the state of a thermostat.", "en");
        thermostatState.addComment("Domain: thermostat", "en");
        thermostatState.addComment("Range: state_of_thermostat", "en");
        thermostatState.addDomain(thermostat);
        thermostatState.addRange(stateOfThermostat);
        
        final ObjectProperty boilerState = ontology.createObjectProperty(customPre + "boiler_state");
        boilerState.addComment("The property of the state of a boiler.", "en");
        boilerState.addComment("Domain: boiler", "en");
        boilerState.addComment("Range: state_of_boiler", "en");
        boilerState.addDomain(boiler);
        boilerState.addRange(stateOfBoiler);
        // End create states for heating system components
        // End create states

        return ontology;
    }

    // Begin create functions for clearing domain and range
    /**
     * A function to clear the domain and/or range of an ObjectProperty.
     *
     * @param property - The ObjectProperty to clear the domain and/or range of.
     * @param clearDomain - A Boolean of whether to clear the domain.
     * @param clearRange - A Boolean of whether to clear the range.
     */
    private static void clearDomainRange(final ObjectProperty property, 
        final Boolean clearDomain, final Boolean clearRange) {
            if (clearDomain) {
                for (ExtendedIterator<? extends OntResource> iter = property.listDomain(); iter.hasNext();) {
                    final OntClass res = iter.next().asClass();
                    property.removeDomain(res);
                }
            }
            if (clearRange) {
                for (ExtendedIterator<? extends OntResource> iter = property.listRange(); iter.hasNext();) {
                    final OntClass res = iter.next().asClass();
                    property.removeRange(res);
                }
            }
    }

    /**
     * A function to clear the both domain and range of an ObjectProperty.
     *
     * @param property - The ObjectProperty to clear both the domain and range of.
     */
    private static void clearDomainRange(final ObjectProperty property) {
        clearDomainRange(property, true, true);
    }    

    /**
     * A function to clear the domain of an ObjectProperty.
     *
     * @param property - The ObjectProperty to clear the domain of.
     */
    private static void clearDomain(final ObjectProperty property) {
        clearDomainRange(property, true, false);
    }

    /**
     * A function to clear the range of an ObjectProperty.
     *
     * @param property - The ObjectProperty to clear the range of.
     */
    private static void clearRange(final ObjectProperty property) {
        clearDomainRange(property, false, true);
    }
    // End create functions for clearing domain and range

}
