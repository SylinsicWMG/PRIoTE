package uk.sylinsic.custom;

import static uk.gov.gchq.hqdm.iri.HQDM.ENTITY_NAME;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.REF_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.USER_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.event;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.uid;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import uk.gov.gchq.hqdm.iri.IRI;
import uk.gov.gchq.hqdm.model.Association;
import uk.gov.gchq.hqdm.model.ClassOfStateOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.ClassOfStateOfFunctionalSystemComponent;
import uk.gov.gchq.hqdm.model.ClassOfStateOfPerson;
import uk.gov.gchq.hqdm.model.Event;
import uk.gov.gchq.hqdm.model.FunctionalSystem;
import uk.gov.gchq.hqdm.model.FunctionalSystemComponent;
import uk.gov.gchq.hqdm.model.KindOfAssociation;
import uk.gov.gchq.hqdm.model.KindOfBiologicalSystemComponent;
import uk.gov.gchq.hqdm.model.KindOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.KindOfFunctionalSystemComponent;
import uk.gov.gchq.hqdm.model.KindOfPerson;
import uk.gov.gchq.hqdm.model.Participant;
import uk.gov.gchq.hqdm.model.Person;
import uk.gov.gchq.hqdm.model.PossibleWorld;
import uk.gov.gchq.hqdm.model.Role;
import uk.gov.gchq.hqdm.model.StateOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.StateOfFunctionalSystemComponent;
import uk.gov.gchq.hqdm.model.StateOfPerson;
import uk.gov.gchq.hqdm.model.Thing;
import uk.gov.gchq.hqdm.model.impl.AssociationImpl;
import uk.gov.gchq.hqdm.model.impl.ClassOfStateOfFunctionalSystemComponentImpl;
import uk.gov.gchq.hqdm.model.impl.ClassOfStateOfFunctionalSystemImpl;
import uk.gov.gchq.hqdm.model.impl.ClassOfStateOfPersonImpl;
import uk.gov.gchq.hqdm.model.impl.FunctionalSystemComponentImpl;
import uk.gov.gchq.hqdm.model.impl.FunctionalSystemImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfAssociationImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfBiologicalSystemComponentImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfFunctionalSystemComponentImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfFunctionalSystemImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfPersonImpl;
import uk.gov.gchq.hqdm.model.impl.ParticipantImpl;
import uk.gov.gchq.hqdm.model.impl.PersonImpl;
import uk.gov.gchq.hqdm.model.impl.PossibleWorldImpl;
import uk.gov.gchq.hqdm.model.impl.RoleImpl;
import uk.gov.gchq.hqdm.model.impl.StateOfFunctionalSystemComponentImpl;
import uk.gov.gchq.hqdm.model.impl.StateOfFunctionalSystemImpl;
import uk.gov.gchq.hqdm.model.impl.StateOfPersonImpl;

/**
 * Constructs a custom dataset of HQDM objects for demonstrating Magma Core with a central heating system.
 */
public final class CustomDataset {
    
    private CustomDataset() {}

    /**
     * Creates and populates a Jena dataset with the custom data objects.
     *
     * @return The populated Jena dataset.
     */
    public static final Dataset buildDataset() {
        final Model model = ModelFactory.createDefaultModel();
        
        createDataObjects().forEach(object -> {
            final Resource resource = model.createResource(object.getIri().toString());
            object.getPredicates()
                    .forEach((iri, predicates) -> predicates.forEach(predicate -> resource
                    .addProperty(model.createProperty(iri.toString()), predicate.toString())));
        });
        return DatasetFactory.create(model);
    }

    /**
     * Generate a custom set of data objects using HQDM.
     *
     * @return A list of HQDM objects.
     */
    public static List<Thing> createDataObjects() {
        final List<Thing> objects = new ArrayList<>();

        // RDL CLASSES - Can be created, stored and queried separately.

        // Viewable is a class to assign other data objects to, to indicate that they are likely to
        // be of direct interest to a system user.
        final uk.gov.gchq.hqdm.model.Class viewable =
                new uk.gov.gchq.hqdm.model.impl.ClassImpl.Builder(
                        new IRI(REF_BASE, uid())).build();
        viewable.addStringValue(ENTITY_NAME, "VIEWABLE");
        objects.add(viewable);

        // A sub-set of the Viewable class.
        final uk.gov.gchq.hqdm.model.Class viewableObject =
                new uk.gov.gchq.hqdm.model.impl.ClassImpl.Builder(
                        new IRI(REF_BASE, uid())).has_Superclass(viewable).build();
        viewableObject.addStringValue(ENTITY_NAME, "VIEWABLE_OBJECT");
        objects.add(viewableObject);

        // A sub-set of the Viewable Class for viewable Associations.
        final uk.gov.gchq.hqdm.model.Class viewableAssociation =
                new uk.gov.gchq.hqdm.model.impl.ClassImpl.Builder(
                        new IRI(REF_BASE, uid())).has_Superclass(viewable).build();
        viewableAssociation.addStringValue(ENTITY_NAME, "VIEWABLE_ASSOCIATION");
        objects.add(viewableAssociation);

        // An system is composed of components so this is the class of components that a whole-life
        // person can have.
        final KindOfBiologicalSystemComponent kindOfBiologicalSystemHumanComponent =
                new KindOfBiologicalSystemComponentImpl.Builder(
                        new IRI(REF_BASE, uid())).build();
        kindOfBiologicalSystemHumanComponent.addStringValue(ENTITY_NAME,
                "KIND_OF_BIOLOGICAL_SYSTEM_HUMAN_COMPONENT");
        objects.add(kindOfBiologicalSystemHumanComponent);

        // A class of whole-life person (re-)created as Reference Data.
        final KindOfPerson kindOfPerson = new KindOfPersonImpl.Builder(new IRI(REF_BASE, uid()))
                .member__Of(viewableObject)
                .has_Component_By_Class_M(kindOfBiologicalSystemHumanComponent).build();
        kindOfPerson.addStringValue(ENTITY_NAME, "KIND_OF_PERSON");
        objects.add(kindOfPerson);

        // A class of temporal part (state) of a (whole-life) person.
        final ClassOfStateOfPerson classOfStateOfPerson = new ClassOfStateOfPersonImpl.Builder(
                new IRI(REF_BASE, uid())).member__Of(viewableObject).build();
        classOfStateOfPerson.addStringValue(ENTITY_NAME, "CLASS_OF_STATE_OF_PERSON");
        objects.add(classOfStateOfPerson);

        // A class of whole-life system that is a Building.
        final KindOfFunctionalSystem kindOfFunctionalSystemBuilding =
                new KindOfFunctionalSystemImpl(
                        new IRI(REF_BASE, uid()));
        kindOfFunctionalSystemBuilding.addStringValue(ENTITY_NAME,
                "KIND_OF_FUNCTIONAL_SYSTEM_BUILDING");
        objects.add(kindOfFunctionalSystemBuilding);

        // A Domestic Property is a system composed of components (e.g. walls, floors, roof, front
        // door, etc). This is the class of those whole-life system components.
        final KindOfFunctionalSystemComponent kindOfFunctionalSystemDomesticPropertyComponent =
                new KindOfFunctionalSystemComponentImpl.Builder(
                        new IRI(REF_BASE, uid())).build();
        kindOfFunctionalSystemDomesticPropertyComponent.addStringValue(ENTITY_NAME,
                "KIND_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY_COMPONENT");
        objects.add(kindOfFunctionalSystemDomesticPropertyComponent);

        // The class of whole-life system that is domestic property.
        final KindOfFunctionalSystem kindOfFunctionalSystemDomesticProperty =
                new KindOfFunctionalSystemImpl.Builder(
                        new IRI(REF_BASE, uid()))
                                .has_Superclass(kindOfFunctionalSystemBuilding)
                                .member__Of(viewableObject)
                                .has_Component_By_Class_M(
                                        kindOfFunctionalSystemDomesticPropertyComponent)
                                .build();
        kindOfFunctionalSystemDomesticProperty.addStringValue(ENTITY_NAME,
                "KIND_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY");
        objects.add(kindOfFunctionalSystemDomesticProperty);

        // The class of state of functional system components whose members are temporal parts of functional systems.
        final ClassOfStateOfFunctionalSystemComponent classOfStateOfFunctionalSystemDomesticPropertyComponent =
                new ClassOfStateOfFunctionalSystemComponentImpl.Builder(
                        new IRI(REF_BASE, uid()))
                                .member__Of(viewableObject)
                                .build();
        classOfStateOfFunctionalSystemDomesticPropertyComponent.addStringValue(ENTITY_NAME,
                "STATE_OF_FUNCTIONAL_SYSTEM_COMPONENT");
        objects.add(classOfStateOfFunctionalSystemDomesticPropertyComponent);

        // The class of state of system whose members are temporal parts of domestic properties.
        final ClassOfStateOfFunctionalSystem classOfStateOfFunctionalSystemDomesticProperty =
                new ClassOfStateOfFunctionalSystemImpl.Builder(
                        new IRI(REF_BASE, uid()))
                                .member__Of(viewableObject)
                                .build();
        classOfStateOfFunctionalSystemDomesticProperty.addStringValue(ENTITY_NAME,
                "STATE_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY");
        objects.add(classOfStateOfFunctionalSystemDomesticProperty);


        // The class of role that every member of class of person plays.
        final Role personRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        personRole.addStringValue(ENTITY_NAME, "NATURAL_MEMBER_OF_SOCIETY_ROLE");
        objects.add(personRole);

        // The class of role that every member of class of domestic property plays.
        final Role domesticPropertyRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        domesticPropertyRole.addStringValue(ENTITY_NAME,
                "ACCEPTED_PLACE_OF_SEMI_PERMANENT_HABITATION_ROLE");
        objects.add(domesticPropertyRole);

        final Role domesticOccupantInPropertyRole = new RoleImpl.Builder(new IRI(REF_BASE, uid()))
                .has_Superclass(domesticPropertyRole).build();
        // Would be good to add part_of_by_class_(occupantInPropertyKindOfAssociation) but can't
        // neatly do that in the class as it can only be added after
        // occupantInPropertyKindOfAssociation is created. This can be added later for completeness.
        domesticOccupantInPropertyRole.addStringValue(ENTITY_NAME,
                "DOMESTIC_PROPERTY_THAT_IS_OCCUPIED_ROLE");
        objects.add(domesticOccupantInPropertyRole);

        final Role occupierOfPropertyRole = new RoleImpl.Builder(new IRI(REF_BASE, uid()))
                .has_Superclass(classOfStateOfPerson).build();

        // Would be good to add part_of_by_class_(occupantInPropertyKindOfAssociation) but can't
        // neatly do that in the class as it can only be added after
        // occupantInPropertyKindOfAssociation is created. This can be added later for completeness.
        occupierOfPropertyRole.addStringValue(ENTITY_NAME,
                "OCCUPIER_LOCATED_IN_PROPERTY_ROLE");
        objects.add(occupierOfPropertyRole);

        // Add the Association Types (Participants and Associations).
        final KindOfAssociation occupantInPropertyKindOfAssociation =
                new KindOfAssociationImpl.Builder(
                        new IRI(REF_BASE, uid()))
                        .member__Of(viewableAssociation)
                        .consists_Of_By_Class(domesticOccupantInPropertyRole)
                        .consists_Of_By_Class(occupierOfPropertyRole).build();
                        occupantInPropertyKindOfAssociation.addStringValue(ENTITY_NAME,
                        "OCCUPANT_LOCATED_IN_VOLUME_ENCLOSED_BY_PROPERTY_ASSOCIATION");
                        

        // Create our world
        final PossibleWorld possibleWorld = new PossibleWorldImpl(new IRI(USER_BASE, uid()));
        possibleWorld.addStringValue(ENTITY_NAME, "Example_World");
        objects.add(possibleWorld);
                        
        // Create the roles for our building, room and componenets
        final Role buildingRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        buildingRole.addStringValue(ENTITY_NAME, "BUILDING_IN_WORLD_ROLE");
        
        final Role roomRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        buildingRole.addStringValue(ENTITY_NAME, "ROOM_IN_BUILDING_ROLE");
        
        final Role wallRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        buildingRole.addStringValue(ENTITY_NAME, "WALL_IN_ROOM_ROLE");
        
        final Role heatingSystemRole = new RoleImpl.Builder(new IRI(REF_BASE, uid())).build();
        buildingRole.addStringValue(ENTITY_NAME, "COMPONENT_OF_HEATING_SYSTEM_ROLE");

        objects.add(buildingRole);
        objects.add(roomRole);
        objects.add(wallRole);
        objects.add(heatingSystemRole);

        // Create the building and room
        final Event buildingConstructionEvent = event("2021-07-19T00:00:00", possibleWorld, USER_BASE);
        final FunctionalSystem ourBuilding = new FunctionalSystemImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind(kindOfFunctionalSystemDomesticProperty)
                .part_Of_Possible_World_M(possibleWorld)
                .intended_Role_M(domesticPropertyRole)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(buildingRole)
                .build();
        ourBuilding.addStringValue(ENTITY_NAME, "Our_Building");
        objects.add(buildingConstructionEvent);
        objects.add(ourBuilding);

        final FunctionalSystemComponent ourRoom = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(roomRole)
                .build();
        ourRoom.addStringValue(ENTITY_NAME, "Our_Room");    
        objects.add(ourRoom);        

        // Create the walls of the building
        final FunctionalSystemComponent northWall = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(wallRole)
                .build();
        northWall.addStringValue(ENTITY_NAME, "North_Wall_Of_Building");

        final FunctionalSystemComponent eastWall = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(wallRole)
                .build();
        eastWall.addStringValue(ENTITY_NAME, "East_Wall_Of_Building");

        final FunctionalSystemComponent southWall = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(wallRole)
                .build();
        southWall.addStringValue(ENTITY_NAME, "South_Wall_Of_Building");

        final FunctionalSystemComponent westWall = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(wallRole)
                .build();
        westWall.addStringValue(ENTITY_NAME, "West_Wall_Of_Building");

        objects.add(northWall);
        objects.add(eastWall);
        objects.add(southWall);
        objects.add(westWall);

        // Create our heating components
        final FunctionalSystemComponent radiator = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(heatingSystemRole)
                .build();
        
        final FunctionalSystemComponent thermostat = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(heatingSystemRole)
                .build();
            
        final FunctionalSystemComponent boiler = new FunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(kindOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .component_Of_M(ourBuilding)
                .beginning(buildingConstructionEvent)
                .intended_Role_M(heatingSystemRole)
                .build();

        objects.add(radiator);
        objects.add(thermostat);
        objects.add(boiler);

        // Create our states of heating components
        final StateOfFunctionalSystemComponent stateOfRadiator = new StateOfFunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of(classOfStateOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal_Part_Of(radiator)
                .beginning(buildingConstructionEvent)
                .build();
        stateOfRadiator.addStringValue(ENTITY_NAME, "Radiator heating room");
            
        final StateOfFunctionalSystemComponent stateOfThermostat = new StateOfFunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of(classOfStateOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal_Part_Of(thermostat)
                .beginning(buildingConstructionEvent)
                .build();
        stateOfThermostat.addStringValue(ENTITY_NAME, "Thermostat set to heat room to 20 celsius");
    
        final StateOfFunctionalSystemComponent stateOfBoiler = new StateOfFunctionalSystemComponentImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of(classOfStateOfFunctionalSystemDomesticPropertyComponent)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal_Part_Of(boiler)
                .beginning(buildingConstructionEvent)
                .build();
        stateOfBoiler.addStringValue(ENTITY_NAME, "Boiler heating water to set temperature");
    
        objects.add(stateOfRadiator);
        objects.add(stateOfThermostat);
        objects.add(stateOfBoiler);

        // Create our person
        final Event personBirthEvent = event("2021-07-17T00:00:00", possibleWorld, USER_BASE);
        final Person ourPerson = new PersonImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind(kindOfPerson)
                .natural_Role_M(personRole)
                .part_Of_Possible_World_M(possibleWorld)
                .beginning(personBirthEvent)
                .build();
        ourPerson.addStringValue(ENTITY_NAME, "Our person");
        objects.add(personBirthEvent);
        objects.add(ourPerson);

        // Create our states of person
        final StateOfPerson ourPersonState = new StateOfPersonImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of(classOfStateOfPerson)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal_Part_Of(ourPerson)
                .beginning(personBirthEvent)
                .build();
        ourPersonState.addStringValue(ENTITY_NAME, "Our person is cold");
        objects.add(ourPersonState);

        // Create states of house
        final StateOfFunctionalSystem ourBuildingState = new StateOfFunctionalSystemImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of(classOfStateOfFunctionalSystemDomesticProperty)
                .temporal_Part_Of(ourBuilding)
                .part_Of_Possible_World_M(possibleWorld)
                .beginning(buildingConstructionEvent)
                .build();
        ourBuildingState.addStringValue(ENTITY_NAME, "Our building is cold");
        objects.add(ourBuildingState);

        // Create associations of states
        final Participant personParticipant = new ParticipantImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(occupierOfPropertyRole)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal__Part_Of(ourPersonState)
                .beginning(personBirthEvent)
                .build();
        personParticipant.addStringValue(ENTITY_NAME, "State of our person participating in the association");
        
        final Participant buildingParticipant = new ParticipantImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(domesticOccupantInPropertyRole)
                .part_Of_Possible_World_M(possibleWorld)
                .temporal__Part_Of(ourBuildingState)
                .beginning(personBirthEvent)
                .build();
        buildingParticipant.addStringValue(ENTITY_NAME, "State of our house participating in the association");
        

        final Association buildingAndPersonCold = new AssociationImpl.Builder(
                new IRI(USER_BASE, uid()))
                .member_Of_Kind_M(occupantInPropertyKindOfAssociation)
                .consists_Of_Participant(personParticipant)
                .consists_Of_Participant(buildingParticipant)
                .part_Of_Possible_World_M(possibleWorld)
                .beginning(personBirthEvent)
                .build();
        buildingAndPersonCold.addStringValue(ENTITY_NAME, "Both the person is cold and the house is cold.");
        
        objects.add(personParticipant);
        objects.add(buildingParticipant);
        objects.add(buildingAndPersonCold);

        return objects;
    }
                    
}
