package uk.sylinsic.custom;

import static uk.gov.gchq.hqdm.iri.HQDM.ENTITY_NAME;
import static uk.gov.gchq.hqdm.iri.HQDM.HQDM;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.REF_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.USER_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.uid;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import uk.gov.gchq.hqdm.iri.HqdmIri;
import uk.gov.gchq.hqdm.iri.IRI;
import uk.gov.gchq.hqdm.model.ClassOfPhysicalQuantity;
import uk.gov.gchq.hqdm.model.IdentificationOfPhysicalQuantity;
import uk.gov.gchq.hqdm.model.KindOfAssociation;
import uk.gov.gchq.hqdm.model.KindOfPhysicalQuantity;
import uk.gov.gchq.hqdm.model.Pattern;
import uk.gov.gchq.hqdm.model.PhysicalQuantity;
import uk.gov.gchq.hqdm.model.PossibleWorld;
import uk.gov.gchq.hqdm.model.RecognizingLanguageCommunity;
import uk.gov.gchq.hqdm.model.RepresentationByPattern;
import uk.gov.gchq.hqdm.model.RepresentationBySign;
import uk.gov.gchq.hqdm.model.Role;
import uk.gov.gchq.hqdm.model.Scale;
import uk.gov.gchq.hqdm.model.Sign;
import uk.gov.gchq.hqdm.model.Thing;
import uk.gov.gchq.hqdm.model.UnitOfMeasure;
import uk.gov.gchq.hqdm.model.impl.ClassOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.IdentificationOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfAssociationImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.PatternImpl;
import uk.gov.gchq.hqdm.model.impl.PhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.PossibleWorldImpl;
import uk.gov.gchq.hqdm.model.impl.RecognizingLanguageCommunityImpl;
import uk.gov.gchq.hqdm.model.impl.RepresentationByPatternImpl;
import uk.gov.gchq.hqdm.model.impl.RepresentationBySignImpl;
import uk.gov.gchq.hqdm.model.impl.RoleImpl;
import uk.gov.gchq.hqdm.model.impl.ScaleImpl;
import uk.gov.gchq.hqdm.model.impl.SignImpl;
import uk.gov.gchq.hqdm.model.impl.UnitOfMeasureImpl;

/**
 * Constructs a custom dataset of HQDM objects for demonstrating Magma Core with a central heating system.
 */
public final class CustomDataset {
    
    private CustomDataset() {}

    /**
     * Creates and populates a Jena dataset with the custom data objects based on ontology.
     *
     * @param model represents the ontological model to base the dataset off.
     * @return The populated Jena dataset.
     */
    public static final Dataset buildDataset(final Model model) {
        createDataObjects().forEach(object -> {
            final Resource resource = model.createResource(object.getIri().toString());
            object.getPredicates()
                    .forEach((iri, predicates) -> predicates.forEach(predicate -> resource
                    .addProperty(model.createProperty(iri.toString()), predicate.toString())));
        });            

        return DatasetFactory.create(model);
    }

    /**
    * Creates and populates a Jena dataset with the custom data objects based on default ontology.
    *
    * @return The populated Jena dataset.
    */
    public static final Dataset buildDataset() {
        return buildDataset(ModelFactory.createDefaultModel());
    }

    /**
     * Generate a custom set of data objects using HQDM.
     *
     * @return A list of HQDM objects.
     */
    public static List<Thing> createDataObjects() {
        final List<Thing> objects = new ArrayList<>();

        // Begin create classes of unit types
        final ClassOfPhysicalQuantity classOfUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .build();
        classOfUnits.addStringValue(ENTITY_NAME, "CLASS_OF_UNITS");
        objects.add(classOfUnits);

        final ClassOfPhysicalQuantity classOfImperialUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .has_Superclass(classOfUnits)
            .build();
        classOfImperialUnits.addStringValue(ENTITY_NAME, "CLASS_OF_IMPERIAL_UNITS");
        objects.add(classOfImperialUnits);
        
        final ClassOfPhysicalQuantity classOfMetricUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .has_Superclass(classOfUnits)
            .build();
        classOfMetricUnits.addStringValue(ENTITY_NAME, "CLASS_OF_METRIC_UNITS");
        objects.add(classOfMetricUnits);

        final ClassOfPhysicalQuantity classOfBaseSiUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .has_Superclass(classOfMetricUnits)
            .build();
        classOfBaseSiUnits.addStringValue(ENTITY_NAME, "CLASS_OF_BASE_SI_UNITS");
        objects.add(classOfBaseSiUnits);

        final ClassOfPhysicalQuantity classOfDerivedSiUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .has_Superclass(classOfBaseSiUnits)
            .build();
        classOfDerivedSiUnits.addStringValue(ENTITY_NAME, "CLASS_OF_DERIVED_SI_UNITS");
        objects.add(classOfDerivedSiUnits);
        // End create classes of unit types

        // Begin create IRI for equivalence
        final HqdmIri equivalentTo = new HqdmIri(HQDM, "EQUIVALENT_TO");
        // End create IRI for equivalence

        // Begin create arbitrary units
        final KindOfPhysicalQuantity kindOfArbitraryUnit = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfArbitraryUnit.addStringValue(ENTITY_NAME, "KIND_OF_ARBITRARY_UNIT");
        objects.add(kindOfArbitraryUnit);

        final UnitOfMeasure unitlessUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfUnits)
            .build();
        unitlessUnit.addStringValue(ENTITY_NAME, "UNITLESS_UNIT");
        objects.add(unitlessUnit);

        final Scale unitlessScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArbitraryUnit)
            .member__Of(classOfUnits)
            .unit(unitlessUnit)
            .build();
        unitlessScale.addStringValue(ENTITY_NAME, "UNITLESS_SCALE");
        objects.add(unitlessScale);
        // End create arbitrary units

        // Begin create temperature units
        final KindOfPhysicalQuantity kindOfTemperature = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfTemperature.addStringValue(ENTITY_NAME, "KIND_OF_TEMPERATURE");
        objects.add(kindOfTemperature);

        final PhysicalQuantity temperature = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfTemperature)
            .build();
        temperature.addStringValue(ENTITY_NAME, "TEMPERATURE");
        objects.add(temperature);
        
        final UnitOfMeasure degreesCelsiusUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfDerivedSiUnits)
            .build();
        degreesCelsiusUnit.addStringValue(ENTITY_NAME, "DEGREES_CELSIUS_UNIT");
        objects.add(degreesCelsiusUnit);

        final Scale degreesCelsiusScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfTemperature)
            .member__Of(classOfDerivedSiUnits)
            .unit(degreesCelsiusUnit)
            .build();
        degreesCelsiusScale.addStringValue(ENTITY_NAME, "DEGREES_CELSIUS_SCALE");
        objects.add(degreesCelsiusScale);

        final UnitOfMeasure degreesKelvinUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        degreesKelvinUnit.addStringValue(ENTITY_NAME, "DEGREES_KELVIN_UNIT");
        objects.add(degreesKelvinUnit);

        final Scale degreesKelvinScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfTemperature)
            .member__Of(classOfBaseSiUnits)
            .unit(degreesKelvinUnit)
            .build();
        degreesKelvinScale.addStringValue(ENTITY_NAME, "DEGREES_KELVIN_SCALE");
        objects.add(degreesKelvinScale);
        // End create temperature units

        // Begin create duration units
        final KindOfPhysicalQuantity kindOfDuration = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfDuration.addStringValue(ENTITY_NAME, "KIND_OF_DURATION");
        objects.add(kindOfDuration);

        final PhysicalQuantity duration = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfDuration)
            .build();
        duration.addStringValue(ENTITY_NAME, "DURATION");
        objects.add(duration);

        final UnitOfMeasure millisecondUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        millisecondUnit.addStringValue(ENTITY_NAME, "MILLISECOND_UNIT");
        objects.add(millisecondUnit);

        final Scale millisecondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfMetricUnits)
            .unit(millisecondUnit)
            .build();
        millisecondScale.addStringValue(ENTITY_NAME, "MILLISECOND_SCALE");
        objects.add(millisecondScale);

        final UnitOfMeasure secondUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        secondUnit.addStringValue(ENTITY_NAME, "SECOND_UNIT");
        objects.add(secondUnit);

        final Scale secondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfBaseSiUnits)
            .unit(secondUnit)
            .build();
        secondScale.addStringValue(ENTITY_NAME, "SECOND_SCALE");
        objects.add(secondScale);

        final UnitOfMeasure minuteUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        minuteUnit.addStringValue(ENTITY_NAME, "MINUTE_UNIT");
        objects.add(minuteUnit);

        final Scale minuteScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfImperialUnits)
            .unit(minuteUnit)
            .build();
        minuteScale.addStringValue(ENTITY_NAME, "MINUTE_SCALE");
        objects.add(minuteScale);
        
        final UnitOfMeasure hourUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        hourUnit.addStringValue(ENTITY_NAME, "HOUR_UNIT");
        objects.add(hourUnit);
        
        final Scale hourScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfImperialUnits)
            .unit(hourUnit)
            .build();
        hourScale.addStringValue(ENTITY_NAME, "HOUR_SCALE");
        objects.add(hourScale);

        final UnitOfMeasure dayUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        dayUnit.addStringValue(ENTITY_NAME, "DAY_UNIT");
        objects.add(dayUnit);
        
        final Scale dayScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfMetricUnits)
            .unit(dayUnit)
            .build();
        dayScale.addStringValue(ENTITY_NAME, "DAY_SCALE");
        objects.add(dayScale);

        final UnitOfMeasure weekUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        weekUnit.addStringValue(ENTITY_NAME, "WEEK_UNIT");
        objects.add(weekUnit);
        
        final Scale weekScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfImperialUnits)
            .unit(weekUnit)
            .build();
        weekScale.addStringValue(ENTITY_NAME, "WEEK_SCALE");
        objects.add(weekScale);

        final UnitOfMeasure monthUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        monthUnit.addStringValue(ENTITY_NAME, "MONTH_UNIT");
        objects.add(monthUnit);
        
        final Scale monthScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfImperialUnits)
            .unit(monthUnit)
            .build();
        monthScale.addStringValue(ENTITY_NAME, "MONTH_SCALE");
        objects.add(monthScale);

        final UnitOfMeasure yearUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        yearUnit.addStringValue(ENTITY_NAME, "YEAR_UNIT");
        objects.add(yearUnit);
        
        final Scale yearScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfImperialUnits)
            .unit(yearUnit)
            .build();
        yearScale.addStringValue(ENTITY_NAME, "YEAR_SCALE");
        objects.add(yearScale);
        // End create time units

        // Begin create angle units
        final KindOfPhysicalQuantity kindOfAngle = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfAngle.addStringValue(ENTITY_NAME, "KIND_OF_ANGLE");
        objects.add(kindOfAngle);

        final PhysicalQuantity angle = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfAngle)
            .build();
        angle.addStringValue(ENTITY_NAME, "ANGLE");
        objects.add(angle);

        final UnitOfMeasure radianUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfDerivedSiUnits)
            .build();
        radianUnit.addStringValue(ENTITY_NAME, "RADIAN_UNIT");
        objects.add(radianUnit);

        final Scale radianScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfDerivedSiUnits)
            .unit(radianUnit)
            .build();
        radianScale.addStringValue(ENTITY_NAME, "RADIAN_SCALE");
        objects.add(radianScale);

        final UnitOfMeasure degreeUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        degreeUnit.addStringValue(ENTITY_NAME, "DEGREE_UNIT");
        objects.add(degreeUnit);

        final Scale degreeScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDuration)
            .member__Of(classOfMetricUnits)
            .unit(degreeUnit)
            .build();
        degreeScale.addStringValue(ENTITY_NAME, "DEGREE_SCALE");
        objects.add(degreeScale);      
        // End create angle units

        // Begin create distance units
        final KindOfPhysicalQuantity kindOfDistance = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfDistance.addStringValue(ENTITY_NAME, "KIND_OF_DISTANCE");
        objects.add(kindOfDistance);

        final PhysicalQuantity distance = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfDistance)
            .build();
        distance.addStringValue(ENTITY_NAME, "DISTANCE");
        objects.add(distance);
 
        // Metric measurements
        final UnitOfMeasure millimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        millimetreUnit.addStringValue(ENTITY_NAME, "MILLIMETRE_UNIT");
        objects.add(millimetreUnit);

        final Scale millimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(millimetreUnit)
            .build();
        millimetreScale.addStringValue(ENTITY_NAME, "MILLIMETRE_SCALE");
        objects.add(millimetreScale);
        
        final UnitOfMeasure centimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        centimetreUnit.addStringValue(ENTITY_NAME, "CENTIMETRE_UNIT");
        objects.add(centimetreUnit);

        final Scale centimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(centimetreUnit)
            .build();
        centimetreScale.addStringValue(ENTITY_NAME, "CENTIMETRE_SCALE");
        objects.add(centimetreScale);
        
        final UnitOfMeasure metreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        metreUnit.addStringValue(ENTITY_NAME, "METRE_UNIT");
        objects.add(metreUnit);

        final Scale metreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfBaseSiUnits)
            .unit(metreUnit)
            .build();
        metreScale.addStringValue(ENTITY_NAME, "METRE_SCALE");
        objects.add(metreScale);
        
        final UnitOfMeasure kilometreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        kilometreUnit.addStringValue(ENTITY_NAME, "KILOMETRE_UNIT");
        objects.add(kilometreUnit);

        final Scale kilometreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(kilometreUnit)
            .build();
        kilometreScale.addStringValue(ENTITY_NAME, "KILOMETRE_SCALE");
        objects.add(kilometreScale);

        // Imperial measurements
        final UnitOfMeasure inchUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        inchUnit.addStringValue(ENTITY_NAME, "INCH_UNIT");
        objects.add(inchUnit);
        
        final Scale inchScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(inchUnit)
            .build();
        inchScale.addStringValue(ENTITY_NAME, "INCH_SCALE");
        objects.add(inchScale);

        final UnitOfMeasure footUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        footUnit.addStringValue(ENTITY_NAME, "FOOT_UNIT");
        objects.add(footUnit);

        final Scale footScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(footUnit)
            .build();
        footScale.addStringValue(ENTITY_NAME, "FOOT_SCALE");
        objects.add(footScale);

        final UnitOfMeasure yardUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        yardUnit.addStringValue(ENTITY_NAME, "YARD_UNIT");
        objects.add(yardUnit);

        final Scale yardScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(yardUnit)
            .build();
        yardScale.addStringValue(ENTITY_NAME, "YARD_SCALE");
        objects.add(yardScale);

        final UnitOfMeasure mileUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        mileUnit.addStringValue(ENTITY_NAME, "MILE_UNIT");
        objects.add(mileUnit);

        final Scale mileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(mileUnit)
            .build();
        mileScale.addStringValue(ENTITY_NAME, "MILE_SCALE");
        objects.add(mileScale);

        final UnitOfMeasure nauticalMileUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        nauticalMileUnit.addStringValue(ENTITY_NAME, "NAUTICAL_MILE_UNIT");
        objects.add(nauticalMileUnit);

        final Scale nauticalMileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(nauticalMileUnit)
            .build();
        nauticalMileScale.addStringValue(ENTITY_NAME, "NAUTICAL_MILE_SCALE");
        objects.add(nauticalMileScale);
        // End create distance units

        // Begin create area units
        final KindOfPhysicalQuantity kindOfArea = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfArea.addStringValue(ENTITY_NAME, "KIND_OF_AREA");
        objects.add(kindOfArea);
                
        final PhysicalQuantity area = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfArea)
            .build();
        area.addStringValue(ENTITY_NAME, "area");
        objects.add(area);

        // Metric units
        final UnitOfMeasure squareCentimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        squareCentimetreUnit.addStringValue(ENTITY_NAME, "SQUARE_CENTIMETRE_UNIT");
        objects.add(squareCentimetreUnit);

        final Scale squareCentimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .unit(squareCentimetreUnit)
            .build(); 
        squareCentimetreScale.addStringValue(ENTITY_NAME, "SQUARE_CENTIMETRE_SCALE");
        objects.add(squareCentimetreScale);

        final UnitOfMeasure squareMetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        squareMetreUnit.addStringValue(ENTITY_NAME, "SQUARE_METRE_UNIT");
        objects.add(squareMetreUnit);

        final Scale squareMetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfDerivedSiUnits)
            .unit(squareMetreUnit)
            .build(); 
        squareMetreScale.addStringValue(ENTITY_NAME, "SQUARE_METRE_SCALE");
        objects.add(squareMetreScale);

        final UnitOfMeasure squareKilometreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        squareKilometreUnit.addStringValue(ENTITY_NAME, "SQUARE_KILOMETRE_UNIT");
        objects.add(squareKilometreUnit);

        final Scale squareKilometreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .unit(squareKilometreUnit)
            .build(); 
        squareKilometreScale.addStringValue(ENTITY_NAME, "SQUARE_KILOMETRE_SCALE");
        objects.add(squareKilometreScale);

        // Imperial units
        final UnitOfMeasure squareInchUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        squareInchUnit.addStringValue(ENTITY_NAME, "SQUARE_INCH_UNIT");
        objects.add(squareInchUnit);

        final Scale squareInchScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(squareInchUnit)
            .build();
        squareInchScale.addStringValue(ENTITY_NAME, "SQUARE_INCH_SCALE");
        objects.add(squareInchScale);

        final UnitOfMeasure squareFootUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        squareFootUnit.addStringValue(ENTITY_NAME, "SQUARE_FOOT_UNIT");
        objects.add(squareFootUnit);

        final Scale squareFootScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(squareFootUnit)
            .build();
        squareFootScale.addStringValue(ENTITY_NAME, "SQUARE_FOOT_SCALE");
        objects.add(squareFootScale);

        final UnitOfMeasure squareYardUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        squareYardUnit.addStringValue(ENTITY_NAME, "SQUARE_YARD_UNIT");
        objects.add(squareYardUnit);

        final Scale squareYardScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(squareYardUnit)
            .build();
        squareYardScale.addStringValue(ENTITY_NAME, "SQUARE_YARD_SCALE");
        objects.add(squareYardScale);

        final UnitOfMeasure squareMileUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        squareMileUnit.addStringValue(ENTITY_NAME, "SQUARE_MILE_UNIT");
        objects.add(squareMileUnit);

        final Scale squareMileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(squareMileUnit)
            .build();
        squareMileScale.addStringValue(ENTITY_NAME, "SQUARE_MILE_SCALE");
        objects.add(squareMileScale);

        final UnitOfMeasure acreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        acreUnit.addStringValue(ENTITY_NAME, "ACRE_UNIT");
        objects.add(acreUnit);

        final Scale acreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(acreUnit)
            .build();
        acreScale.addStringValue(ENTITY_NAME, "ACRE_SCALE");
        objects.add(acreScale);
        // End create area units

        // Begin create volume units
        final KindOfPhysicalQuantity kindOfVolume = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfVolume.addStringValue(ENTITY_NAME, "KIND_OF_VOLUME");
        objects.add(kindOfVolume);
        
        final PhysicalQuantity volume = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfVolume)
            .build();
        volume.addStringValue(ENTITY_NAME, "VOLUME");
        objects.add(volume);

        // Metric units
        final UnitOfMeasure cubicCentimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        cubicCentimetreUnit.addStringValue(ENTITY_NAME, "CUBIC_CENTIMETRE_UNIT");
        objects.add(cubicCentimetreUnit);

        final Scale cubicCentimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .unit(cubicCentimetreUnit)
            .build();
        cubicCentimetreScale.addStringValue(ENTITY_NAME, "CUBIC_CENTIMETRE_SCALE");
        cubicCentimetreScale.addStringValue(equivalentTo, "MILLILITRE");
        objects.add(cubicCentimetreScale);

        final UnitOfMeasure cubicDecimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        cubicDecimetreUnit.addStringValue(ENTITY_NAME, "CUBIC_DECIMETRE_UNIT");
        objects.add(cubicDecimetreUnit);

        final Scale cubicDecimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .unit(cubicDecimetreUnit)
            .build();
        cubicDecimetreScale.addStringValue(ENTITY_NAME, "CUBIC_DECIMETRE_SCALE");
        cubicDecimetreScale.addStringValue(equivalentTo, "LITRE");
        objects.add(cubicDecimetreScale);
        
        final UnitOfMeasure cubicMetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        cubicMetreUnit.addStringValue(ENTITY_NAME, "CUBIC_METRE_UNIT");
        objects.add(cubicMetreUnit);

        final Scale cubicMetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfDerivedSiUnits)
            .build();
        cubicMetreScale.addStringValue(ENTITY_NAME, "CUBIC_METRE_SCALE");
        cubicMetreScale.addStringValue(equivalentTo, "KILOLITRE");
        objects.add(cubicMetreScale);

        // Imperial units
        final UnitOfMeasure cubicInchUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        cubicInchUnit.addStringValue(ENTITY_NAME, "CUBIC_INCH_UNIT");
        objects.add(cubicInchUnit);

        final Scale cubicInchScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(cubicInchUnit)
            .build();
        cubicInchScale.addStringValue(ENTITY_NAME, "CUBIC_INCH_SCALE");
        objects.add(cubicInchScale);

        final UnitOfMeasure cubicFeetUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        cubicFeetUnit.addStringValue(ENTITY_NAME, "CUBIC_FEET_UNIT");
        objects.add(cubicFeetUnit);

        final Scale cubicFeetScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(cubicFeetUnit)
            .build();
        cubicFeetScale.addStringValue(ENTITY_NAME, "CUBIC_FEET_SCALE");
        objects.add(cubicFeetScale);

        final UnitOfMeasure fluidOunceUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        fluidOunceUnit.addStringValue(ENTITY_NAME, "FLUID_OUNCE_UNIT");
        objects.add(fluidOunceUnit);

        final Scale fluidOunceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(fluidOunceUnit)
            .build();
        fluidOunceScale.addStringValue(ENTITY_NAME, "FLUID_OUNCE_SCALE");
        objects.add(fluidOunceScale);

        final UnitOfMeasure pintUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        pintUnit.addStringValue(ENTITY_NAME, "PINT_UNIT");
        objects.add(pintUnit);

        final Scale pintScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(pintUnit)
            .build();
        pintScale.addStringValue(ENTITY_NAME, "PINT_SCALE");
        objects.add(pintScale);

        final UnitOfMeasure quartUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        quartUnit.addStringValue(ENTITY_NAME, "QUART_UNIT");
        objects.add(quartUnit);

        final Scale quartScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(quartUnit)
            .build();
        quartScale.addStringValue(ENTITY_NAME, "QUART_SCALE");
        objects.add(quartScale);

        final UnitOfMeasure gallonUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        gallonUnit.addStringValue(ENTITY_NAME, "GALLON_UNIT");
        objects.add(gallonUnit);

        final Scale gallonScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(gallonUnit)
            .build();
        gallonScale.addStringValue(ENTITY_NAME, "GALLON_SCALE");
        objects.add(gallonScale);
        // End create volume units

        // Begin create mass units
        final KindOfPhysicalQuantity kindOfMass = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfMass.addStringValue(ENTITY_NAME, "KIND_OF_MASS");
        objects.add(kindOfMass);

        final PhysicalQuantity mass = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfMass)
            .build();
        mass.addStringValue(ENTITY_NAME, "MASS");
        objects.add(mass);

        // Metric units
        final UnitOfMeasure milligramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        milligramUnit.addStringValue(ENTITY_NAME, "MILLIGRAM_UNIT");
        objects.add(milligramUnit);

        final Scale milligramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(milligramUnit)
            .build();
        milligramScale.addStringValue(ENTITY_NAME, "MILLIGRAM_SCALE");
        objects.add(milligramScale);

        final UnitOfMeasure gramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        gramUnit.addStringValue(ENTITY_NAME, "GRAM_UNIT");
        objects.add(gramUnit);

        final Scale gramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(gramUnit)
            .build();
        gramScale.addStringValue(ENTITY_NAME, "GRAM_SCALE");
        objects.add(gramScale);

        final UnitOfMeasure kilogramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        kilogramUnit.addStringValue(ENTITY_NAME, "KILOGRAM_UNIT");
        objects.add(kilogramUnit);

        final Scale kilogramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfBaseSiUnits)
            .unit(kilogramUnit)
            .build();
        kilogramScale.addStringValue(ENTITY_NAME, "KILOGRAM_SCALE");
        objects.add(kilogramScale);

        final UnitOfMeasure tonneUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        tonneUnit.addStringValue(ENTITY_NAME, "TONNE_UNIT");
        objects.add(tonneUnit);

        final Scale tonneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(tonneUnit)
            .build();
        tonneScale.addStringValue(ENTITY_NAME, "TONNE_SCALE");
        objects.add(tonneScale);

        // Imperial units
        final UnitOfMeasure ounceUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        ounceUnit.addStringValue(ENTITY_NAME, "OUNCE_UNIT");
        objects.add(ounceUnit);

        final Scale ounceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(ounceUnit)
            .build();
        ounceScale.addStringValue(ENTITY_NAME, "OUNCE_SCALE");
        objects.add(ounceScale);

        final UnitOfMeasure poundUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        poundUnit.addStringValue(ENTITY_NAME, "POUND_UNIT");
        objects.add(poundUnit);

        final Scale poundScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(poundUnit)
            .build();
        poundScale.addStringValue(ENTITY_NAME, "POUND_SCALE");
        objects.add(poundScale);

        final UnitOfMeasure stoneUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        stoneUnit.addStringValue(ENTITY_NAME, "STONE_UNIT");
        objects.add(stoneUnit);

        final Scale stoneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(stoneUnit)
            .build();
        stoneScale.addStringValue(ENTITY_NAME, "STONE_SCALE");
        objects.add(stoneScale);

        final UnitOfMeasure tonUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfImperialUnits)
            .build();
        tonUnit.addStringValue(ENTITY_NAME, "TON_UNIT");
        objects.add(tonUnit);

        final Scale tonScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(tonUnit)
            .build();
        tonScale.addStringValue(ENTITY_NAME, "TON_SCALE");
        objects.add(tonScale);
        // End create mass units
        
        // Begin create light units
        final KindOfPhysicalQuantity kindOfLightIntensity = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfLightIntensity.addStringValue(ENTITY_NAME, "KIND_OF_LIGHT_INTENSITY");
        objects.add(kindOfLightIntensity);

        final PhysicalQuantity lightIntensity = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfLightIntensity)
            .build();
        lightIntensity.addStringValue(ENTITY_NAME, "LIGHT_INTENSITY");
        objects.add(lightIntensity);

        final UnitOfMeasure candelaUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        candelaUnit.addStringValue(ENTITY_NAME, "CANDELA_UNIT");
        objects.add(candelaUnit);

        final Scale candelaScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfLightIntensity)
            .member__Of(classOfBaseSiUnits)
            .unit(candelaUnit)
            .build();
        candelaScale.addStringValue(ENTITY_NAME, "CANDELA_SCALE");
        objects.add(candelaScale);
        // End create light units

        // Begin create molecular units
        final KindOfPhysicalQuantity kindOfMolarQuantity = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfMolarQuantity.addStringValue(ENTITY_NAME, "KIND_OF_MOLAR_QUANTITY");
        objects.add(kindOfMolarQuantity);

        final PhysicalQuantity molarQuantity = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfMolarQuantity)
            .build();
        molarQuantity.addStringValue(ENTITY_NAME, "MOLAR_QUANTITY");
        objects.add(molarQuantity);

        final UnitOfMeasure moleUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        moleUnit.addStringValue(ENTITY_NAME, "MOLE_UNIT");
        objects.add(moleUnit);

        final Scale moleScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfMolarQuantity)
            .member__Of(classOfBaseSiUnits)
            .unit(moleUnit)
            .build();
        moleScale.addStringValue(ENTITY_NAME, "MOLE_SCALE");
        objects.add(moleScale);
        // End create molecular units

        // Begin create electrical current units
        final KindOfPhysicalQuantity kindOfElectricCurrent = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfElectricCurrent.addStringValue(ENTITY_NAME, "KIND_OF_ELECTRICAL_CURRENT");
        objects.add(kindOfElectricCurrent);

        final PhysicalQuantity electricCurrent = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfElectricCurrent)
            .build();
        electricCurrent.addStringValue(ENTITY_NAME, "ELECTRIC_CURRENT");
        objects.add(electricCurrent);

        final UnitOfMeasure milliampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        milliampereUnit.addStringValue(ENTITY_NAME, "MILLIAMPERE_UNIT");
        objects.add(milliampereUnit);

        final Scale milliampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
            .unit(milliampereUnit)
            .build();
        milliampereScale.addStringValue(ENTITY_NAME, "MILLIAMPERE_SCALE");
        objects.add(milliampereScale);

        final UnitOfMeasure ampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfBaseSiUnits)
            .build();
        ampereUnit.addStringValue(ENTITY_NAME, "AMPERE_UNIT");
        objects.add(ampereUnit);

        final Scale ampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfBaseSiUnits)
            .unit(ampereUnit)
            .build();
        ampereScale.addStringValue(ENTITY_NAME, "AMPERE_SCALE");
        objects.add(ampereScale);

        final UnitOfMeasure kiloampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        kiloampereUnit.addStringValue(ENTITY_NAME, "KILOAMPERE_UNIT");
        objects.add(kiloampereUnit);

        final Scale kiloampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
            .unit(kiloampereUnit)
            .build();
        kiloampereScale.addStringValue(ENTITY_NAME, "KILOAMPERE_SCALE");
        objects.add(kiloampereScale);
        // End create electrical current units

        // Begin create electrical potential units
        final KindOfPhysicalQuantity kindOfElectricalPotential = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of(classOfUnits)
            .build();
        kindOfElectricalPotential.addStringValue(ENTITY_NAME, "KIND_OF_ELECTRICAL_POTENTIAL");
        objects.add(kindOfElectricalPotential);

        final PhysicalQuantity electricalPotential = new PhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of_Kind_M(kindOfElectricalPotential)
            .build();
        electricalPotential.addStringValue(ENTITY_NAME, "ELECTRICAL_POTENTIAL");
        objects.add(electricalPotential);

        final UnitOfMeasure millivoltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        millivoltUnit.addStringValue(ENTITY_NAME, "MILLIVOLT_UNIT");
        objects.add(millivoltUnit);

        final Scale millivoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .unit(millivoltUnit)
            .build();
        millivoltScale.addStringValue(ENTITY_NAME, "MILLIVOLT_SCALE");
        objects.add(millivoltScale);

        final UnitOfMeasure voltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfDerivedSiUnits)
            .build();
        voltUnit.addStringValue(ENTITY_NAME, "VOLT_UNIT");
        objects.add(voltUnit);

        final Scale voltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfDerivedSiUnits)
            .unit(voltUnit)
            .build();
        voltScale.addStringValue(ENTITY_NAME, "VOLT_SCALE");
        objects.add(voltScale);

        final UnitOfMeasure kilovoltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member__Of(classOfMetricUnits)
            .build();
        kilovoltUnit.addStringValue(ENTITY_NAME, "KILOVOLT_UNIT");
        objects.add(kilovoltUnit);

        final Scale kilovoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .unit(kilovoltUnit)
            .build();
        kilovoltScale.addStringValue(ENTITY_NAME, "KILOVOLT_SCALE");
        objects.add(kilovoltScale);
        // End create electrical potential units
        
        // Begin create recognising language community
        final PossibleWorld world = new PossibleWorldImpl(new IRI(REF_BASE, uid()));
        world.addStringValue(ENTITY_NAME, "WORLD");
        objects.add(world);

        final Pattern quantityUnitPattern = new PatternImpl.Builder(
            new IRI(REF_BASE, uid()))
            .build();
        quantityUnitPattern.addStringValue(ENTITY_NAME, "QUANTITY_UNIT_PATTERN");
        objects.add(quantityUnitPattern);
        
        final Role communityRole = new RoleImpl.Builder(
            new IRI(REF_BASE, uid()))
            .build();
        communityRole.addStringValue(ENTITY_NAME, "COMMUNITY_ROLE");
        objects.add(communityRole);

        final RecognizingLanguageCommunity communityUnderstandingUnitsOfMeasurement = new 
            RecognizingLanguageCommunityImpl.Builder(
                new IRI(REF_BASE, uid()))
                .member_Of_Kind_M(communityRole)
                .part_Of_Possible_World_M(world)
                .build();
        communityUnderstandingUnitsOfMeasurement.addStringValue(ENTITY_NAME,
            "COMMUNITY_UNDERSTANDING_UNITS_OF_MEASUREMENT");
        objects.add(communityUnderstandingUnitsOfMeasurement);

        final RepresentationByPattern quantityUnitPatternRepresentation = new 
            RepresentationByPatternImpl.Builder(
            new IRI(REF_BASE, uid()))
            .consists_Of_By_Class_M(quantityUnitPattern)
            .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
            .represented_M(quantityUnitPattern)
            .build();
        quantityUnitPatternRepresentation.addStringValue(ENTITY_NAME,
            "QUANTITY_UNIT_PATTERN_REPRESENTATION");
        objects.add(quantityUnitPatternRepresentation);

        final KindOfAssociation quantityUnitAssociationKind = new KindOfAssociationImpl.Builder(
            new IRI(REF_BASE, uid()))
            .build();
        quantityUnitAssociationKind.addStringValue(ENTITY_NAME, "CLASS_OF_QUANTITY_UNIT_REPRESENTATION");
        objects.add(quantityUnitAssociationKind);

        final RepresentationBySign quantitySignRepresentation = new RepresentationBySignImpl.Builder(
            new IRI(REF_BASE, uid()))
            .consists_Of_(communityUnderstandingUnitsOfMeasurement)
            .member_Of__M(quantityUnitPatternRepresentation)
            .member_Of_Kind_M(quantityUnitAssociationKind)
            .part_Of_Possible_World_M(world)
            .represents_M(classOfUnits)
            .build();
        quantitySignRepresentation.addStringValue(ENTITY_NAME, "QUANTITY_SIGN_REPRESENTATION");
        objects.add(quantitySignRepresentation);

        final RepresentationBySign unitSignRepresentation = new RepresentationBySignImpl.Builder(
            new IRI(REF_BASE, uid()))
            .consists_Of_(communityUnderstandingUnitsOfMeasurement)
            .member_Of__M(quantityUnitPatternRepresentation)
            .member_Of_Kind_M(quantityUnitAssociationKind)
            .part_Of_Possible_World_M(world)
            .represents_M(classOfUnits)
            .build();
        unitSignRepresentation.addStringValue(ENTITY_NAME, "UNIT_SIGN_REPRESENTATION");
        objects.add(unitSignRepresentation);

        final Sign quantitySign = new SignImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of__M(quantityUnitPattern)
            .part_Of_Possible_World_M(world)
            .participant_In_M(quantitySignRepresentation)
            .build();
        quantitySign.addStringValue(ENTITY_NAME, "QUANTITY_SIGN");
        objects.add(quantitySign);

        final Sign unitSign = new SignImpl.Builder(
            new IRI(REF_BASE, uid()))
            .member_Of__M(quantityUnitPattern)
            .part_Of_Possible_World_M(world)
            .participant_In_M(unitSignRepresentation)
            .build();
        unitSign.addStringValue(ENTITY_NAME, "MEASUREMENT_SIGN");
        objects.add(unitSign);
        // End create recognising language community

        // Begin create temperature quantities
        final IdentificationOfPhysicalQuantity roomTempCelsius = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(temperature)
                .uses_M(degreesCelsiusScale)
                .value__M(21.0)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        roomTempCelsius.addStringValue(ENTITY_NAME, "ROOM_TEMP_CELSIUS");
        objects.add(roomTempCelsius);

        final IdentificationOfPhysicalQuantity bodyTempKelvin = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(temperature)
                .uses_M(degreesKelvinScale)
                .value__M(310.15)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        bodyTempKelvin.addStringValue(ENTITY_NAME, "BODY_TEMP_KELVIN");
        objects.add(bodyTempKelvin);
        // End create temperature quantities

        // Begin create time quantities
        final IdentificationOfPhysicalQuantity averageBlinkTime = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(millisecondScale)
                .value__M(100)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        averageBlinkTime.addStringValue(ENTITY_NAME, "AVERAGE_BLINK_TIME");
        objects.add(averageBlinkTime);

        final IdentificationOfPhysicalQuantity oneHundredMetreRecord = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(secondScale)
                .value__M(9.58)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        oneHundredMetreRecord.addStringValue(ENTITY_NAME, "ONE_HUNDRED_METRE_RECORD");
        objects.add(oneHundredMetreRecord);

        final IdentificationOfPhysicalQuantity minimumTimeOfFootballGame = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(minuteScale)
                .value__M(80)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        minimumTimeOfFootballGame.addStringValue(ENTITY_NAME, "MINIMUM_TIME_OF_FOOTBALL_GAME");
        objects.add(minimumTimeOfFootballGame);

        final IdentificationOfPhysicalQuantity averagePrivatePilotLicenseWorkTime = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(hourScale)
                .value__M(60)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        averagePrivatePilotLicenseWorkTime.addStringValue(ENTITY_NAME, "AVERAGE_PRIVATE_PILOT_LICENSE_WORK_TIME");
        objects.add(averagePrivatePilotLicenseWorkTime);

        final IdentificationOfPhysicalQuantity leapYearDays = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(dayScale)
                .value__M(366)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        leapYearDays.addStringValue(ENTITY_NAME, "LEAP_YEAR_DAYS");
        objects.add(leapYearDays);

        final IdentificationOfPhysicalQuantity schoolYearWeeks = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(weekScale)
                .value__M(39)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        schoolYearWeeks.addStringValue(ENTITY_NAME, "SCHOOL_YEAR_WEEKS");
        objects.add(schoolYearWeeks);

        final IdentificationOfPhysicalQuantity pregnancyMonths = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(monthScale)
                .value__M(9)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        pregnancyMonths.addStringValue(ENTITY_NAME, "PREGNANY_MONTHS");
        objects.add(pregnancyMonths);

        final IdentificationOfPhysicalQuantity sixthFormYears = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(duration)
                .uses_M(yearScale)
                .value__M(2)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        sixthFormYears.addStringValue(ENTITY_NAME, "SIXTH_FORM_YEARS");
        objects.add(sixthFormYears);
        // End create time quantities
        
        // Begin create angle quantities
        final IdentificationOfPhysicalQuantity radiansInCircle = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(angle)
                .uses_M(radianScale)
                .value__M(6.28)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        radiansInCircle.addStringValue(ENTITY_NAME, "RADIANS_IN_CIRCLE");
        objects.add(radiansInCircle);

        final IdentificationOfPhysicalQuantity degreesInCircle = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(angle)
                .uses_M(degreeScale)
                .value__M(360)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        degreesInCircle.addStringValue(ENTITY_NAME, "DEGREES_IN_CIRCLE");
        objects.add(degreesInCircle);
        // End create angle quantities
        
        // Begin create distance quantities
        // Metric measurements
        final IdentificationOfPhysicalQuantity millimetresInCentimetre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(millimetreScale)
                .value__M(10)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        millimetresInCentimetre.addStringValue(ENTITY_NAME, "MILLIMETRES_IN_CENTIMETRE");
        objects.add(millimetresInCentimetre);

        final IdentificationOfPhysicalQuantity centimetresInMetre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(centimetreScale)
                .value__M(100)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        centimetresInMetre.addStringValue(ENTITY_NAME, "CENTIMETRES_IN_METRE");
        objects.add(centimetresInMetre);

        final IdentificationOfPhysicalQuantity metresInKilometre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(metreScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        metresInKilometre.addStringValue(ENTITY_NAME, "METRES_IN_KILOMETRE");
        objects.add(metresInKilometre);

        final IdentificationOfPhysicalQuantity approximateHeightOfIssKilometres = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(kilometreScale)
                .value__M(420)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        approximateHeightOfIssKilometres.addStringValue(ENTITY_NAME, "APPROXIMATE_HEIGHT_OF_ISS_KILOMETRES");
        objects.add(approximateHeightOfIssKilometres);

        // Imperial measurements
        final IdentificationOfPhysicalQuantity inchesInFoot = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(inchScale)
                .value__M(12)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        inchesInFoot.addStringValue(ENTITY_NAME, "INCHES_IN_FOOT");
        objects.add(inchesInFoot);

        final IdentificationOfPhysicalQuantity feetInYard = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(footScale)
                .value__M(3)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        feetInYard.addStringValue(ENTITY_NAME, "FEET_IN_YARD");
        objects.add(feetInYard);

        final IdentificationOfPhysicalQuantity yardsInMile = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(yardScale)
                .value__M(1760)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        yardsInMile.addStringValue(ENTITY_NAME, "YARDS_IN_MILE");
        objects.add(yardsInMile);

        final IdentificationOfPhysicalQuantity approximateHeightOfIssMiles = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(distance)
                .uses_M(mileScale)
                .value__M(260)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        approximateHeightOfIssMiles.addStringValue(ENTITY_NAME, "APPROXIMATE_HEIGHT_OF_ISS_MILES");
        objects.add(approximateHeightOfIssMiles);
        // End create distance quantities

        // Begin create area quantities
        // Metric measurements
        final IdentificationOfPhysicalQuantity squareCentimetresInSquareMetre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareCentimetreScale)
                .value__M(10000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        squareCentimetresInSquareMetre.addStringValue(ENTITY_NAME, "SQUARE_CENTIMETRES_IN_SQUARE_METRE");
        objects.add(squareCentimetresInSquareMetre);

        final IdentificationOfPhysicalQuantity squareMetresInSquareKilometre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareMetreScale)
                .value__M(1000000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        squareMetresInSquareKilometre.addStringValue(ENTITY_NAME, "SQUARE_METRES_IN_SQUARE_KILOMETRE");
        objects.add(squareMetresInSquareKilometre);

        final IdentificationOfPhysicalQuantity areaOfEnglandInSquareKilometres = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareKilometreScale)
                .value__M(130279)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        areaOfEnglandInSquareKilometres.addStringValue(ENTITY_NAME, "AREA_OF_ENGLAND_IN_SQUARE_KILOMETRES");
        objects.add(areaOfEnglandInSquareKilometres);

        // Imperial measurements
        final IdentificationOfPhysicalQuantity squareInchesInSquareFoot = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareInchScale)
                .value__M(144)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        squareInchesInSquareFoot.addStringValue(ENTITY_NAME, "SQUARE_INCHES_IN_SQUARE_FOOT");
        objects.add(squareInchesInSquareFoot);

        final IdentificationOfPhysicalQuantity squareFeetInSquareYard = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareFootScale)
                .value__M(9)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        squareFeetInSquareYard.addStringValue(ENTITY_NAME, "SQUARE_FEET_IN_SQUARE_YARD");
        objects.add(squareFeetInSquareYard);

        final IdentificationOfPhysicalQuantity squareYardsInAcre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareYardScale)
                .value__M(4840)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        squareYardsInAcre.addStringValue(ENTITY_NAME, "SQUARE_YARDS_IN_ACRE");
        objects.add(squareYardsInAcre);

        final IdentificationOfPhysicalQuantity acresInSquareMile = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(acreScale)
                .value__M(640)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        acresInSquareMile.addStringValue(ENTITY_NAME, "ACRES_IN_SQUARE_MILE");
        objects.add(acresInSquareMile);

        final IdentificationOfPhysicalQuantity areaOfEnglandInSquareMiles = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(area)
                .uses_M(squareMileScale)
                .value__M(50301)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        areaOfEnglandInSquareKilometres.addStringValue(ENTITY_NAME, "AREA_OF_ENGLAND_IN_SQUARE_MILES");
        objects.add(areaOfEnglandInSquareMiles);
        // End create area quantities
        
        // Begin create volume quantities
        // Metric measurements
        final IdentificationOfPhysicalQuantity cubicCentimetresInCubicDecimetre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(cubicCentimetreScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        cubicCentimetresInCubicDecimetre.addStringValue(ENTITY_NAME, "CUBIC_CENTIMETRES_IN_CUBIC_DECIMETRE");
        objects.add(cubicCentimetresInCubicDecimetre);

        final IdentificationOfPhysicalQuantity cubicDecimetresInCubicMetre = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(cubicDecimetreScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        cubicDecimetresInCubicMetre.addStringValue(ENTITY_NAME, "CUBIC_DECIMETRES_IN_CUBIC_METRE");
        objects.add(cubicDecimetresInCubicMetre);

        final IdentificationOfPhysicalQuantity minimumVolumeOlympicSwimmingPoolCubicMetres = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(cubicMetreScale)
                .value__M(2500)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        minimumVolumeOlympicSwimmingPoolCubicMetres.addStringValue(ENTITY_NAME, 
            "MINIMUM_VOLUME_OLYMPIC_SWIMMING_POOL_CUBIC_METRES");
        objects.add(minimumVolumeOlympicSwimmingPoolCubicMetres);

        // Imperial measurements
        final IdentificationOfPhysicalQuantity cubicInchesInCubicFoot = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(cubicInchScale)
                .value__M(1728)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        cubicInchesInCubicFoot.addStringValue(ENTITY_NAME, "CUBIC_INCHES_IN_CUBIC_FOOT");
        objects.add(cubicInchesInCubicFoot);

        final IdentificationOfPhysicalQuantity averageHotTubVolumeCubicFeet = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(cubicFeetScale)
                .value__M(25.4)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        averageHotTubVolumeCubicFeet.addStringValue(ENTITY_NAME, "AVERAGE_HOT_TUB_VOLUME_CUBIC_FEET");
        objects.add(averageHotTubVolumeCubicFeet);

        final IdentificationOfPhysicalQuantity fluidOuncesInPint = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(fluidOunceScale)
                .value__M(20)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        fluidOuncesInPint.addStringValue(ENTITY_NAME, "FLUID_OUNCES_IN_PINT");
        objects.add(fluidOuncesInPint);

        final IdentificationOfPhysicalQuantity pintsInQuart = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(pintScale)
                .value__M(2)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        pintsInQuart.addStringValue(ENTITY_NAME, "PINTS_IN_QUART");
        objects.add(pintsInQuart);

        final IdentificationOfPhysicalQuantity quartsInGallon = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(quartScale)
                .value__M(4)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        quartsInGallon.addStringValue(ENTITY_NAME, "QUARTS_IN_GALLON");
        objects.add(quartsInGallon);

        final IdentificationOfPhysicalQuantity minimumVolumeOlympicSwimmingPoolGallons = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(volume)
                .uses_M(gallonScale)
                .value__M(660430)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        minimumVolumeOlympicSwimmingPoolGallons.addStringValue(ENTITY_NAME, 
            "MINIMUM_VOLUME_OLYMPIC_SWIMMING_POOL_GALLONS");
        // End create volume quantities
        
        // Begin create mass quantities
        // Metric measurements
        final IdentificationOfPhysicalQuantity milligramsInGram = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(milligramScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        milligramsInGram.addStringValue(ENTITY_NAME, "MILLIGRAMS_IN_A_GRAM");
        objects.add(milligramsInGram);

        final IdentificationOfPhysicalQuantity gramsInKilogram = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(gramScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        gramsInKilogram.addStringValue(ENTITY_NAME, "GRAMS_IN_A_KILOGRAM");
        objects.add(gramsInKilogram);

        final IdentificationOfPhysicalQuantity kilogramsInTonne = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(kilogramScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        kilogramsInTonne.addStringValue(ENTITY_NAME, "KILOGRAMS_IN_A_TONNE");
        objects.add(kilogramsInTonne);

        final IdentificationOfPhysicalQuantity maximumLightGoodsVehicleMassTonnes = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(tonneScale)
                .value__M(3.5)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        maximumLightGoodsVehicleMassTonnes.addStringValue(ENTITY_NAME,
            "MAXIMUM_LIGHT_GOODS_VEHICLE_MASS_TONNES");
            objects.add(maximumLightGoodsVehicleMassTonnes);

        // Imperial measurements
        final IdentificationOfPhysicalQuantity ouncesInPound = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(ounceScale)
                .value__M(16)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        ouncesInPound.addStringValue(ENTITY_NAME, "OUNCES_IN_POUND");
        objects.add(ouncesInPound);

        final IdentificationOfPhysicalQuantity poundsInStone = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(poundScale)
                .value__M(14)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        poundsInStone.addStringValue(ENTITY_NAME, "POUNDS_IN_STONE");
        objects.add(poundsInStone);

        final IdentificationOfPhysicalQuantity stonesInTon = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(stoneScale)
                .value__M(160)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        stonesInTon.addStringValue(ENTITY_NAME, "STONES_IN_TON");
        objects.add(stonesInTon);

        final IdentificationOfPhysicalQuantity maximumLightGoodsVehicleMassTons = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(mass)
                .uses_M(tonScale)
                .value__M(3.85809)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        maximumLightGoodsVehicleMassTons.addStringValue(ENTITY_NAME, 
            "MAXIMUM_LIGHT_GOODS_VEHICLE_MASS_TONS");
        objects.add(maximumLightGoodsVehicleMassTons);
        // End create mass quantities
        
        // Begin create light quantities
        final IdentificationOfPhysicalQuantity averageCandleLightIntensityCandela = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(lightIntensity)
                .uses_M(candelaScale)
                .value__M(1)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        averageCandleLightIntensityCandela.addStringValue(ENTITY_NAME, "AVERAGE_CANDLE_LIGHT_INTENSITY_CANDELA");
        objects.add(averageCandleLightIntensityCandela);
        // End create light quantities
        
        // Begin create molecular quantities
        final IdentificationOfPhysicalQuantity molesInTwentyFourGramsCarbonTwelve = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(molarQuantity)
                .uses_M(moleScale)
                .value__M(2)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        molesInTwentyFourGramsCarbonTwelve.addStringValue(ENTITY_NAME, 
            "MOLES_IN_TWENTY_FOUR_GRAMS_OF_CARBON_TWELVE");
        objects.add(molesInTwentyFourGramsCarbonTwelve);
        // End create molecular quantities
        
        // Begin create electrical current quantities
        final IdentificationOfPhysicalQuantity milliamperesInAmpere = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricCurrent)
                .uses_M(milliampereScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        milliamperesInAmpere.addStringValue(ENTITY_NAME, "MILLIAMPERES_IN_AMPERE");
        objects.add(milliamperesInAmpere);

        final IdentificationOfPhysicalQuantity amperesInKiloampere = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricCurrent)
                .uses_M(ampereScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        amperesInKiloampere.addStringValue(ENTITY_NAME, "AMPERES_IN_KILOAMPERE");
        objects.add(amperesInKiloampere);

        final IdentificationOfPhysicalQuantity averageColdCrankingKiloamperesForCar = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricCurrent)
                .uses_M(kiloampereScale)
                .value__M(0.4)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        averageColdCrankingKiloamperesForCar.addStringValue(ENTITY_NAME, 
            "AVERAGE_COLD_CRANKING_KILOAMPERES_FOR_CAR");
        objects.add(averageColdCrankingKiloamperesForCar);
        // End create electrical current quantities
        
        // Begin create electrical potential quantities
        final IdentificationOfPhysicalQuantity millivoltsInVolt = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricalPotential)
                .uses_M(millivoltScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        millivoltsInVolt.addStringValue(ENTITY_NAME, "MILLIVOLTS_IN_VOLT");
        objects.add(millivoltsInVolt);

        final IdentificationOfPhysicalQuantity voltsInKilovolt = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricalPotential)
                .uses_M(voltScale)
                .value__M(1000)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        voltsInKilovolt.addStringValue(ENTITY_NAME, "VOLTS_IN_KILOVOLT");
        objects.add(voltsInKilovolt);

        final IdentificationOfPhysicalQuantity maxNationalGridVoltage = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, uid()))
                .represented_M(electricalPotential)
                .uses_M(kilovoltScale)
                .value__M(400)
                .consists_Of_In_Members_M(communityUnderstandingUnitsOfMeasurement)
                .build();
        maxNationalGridVoltage.addStringValue(ENTITY_NAME, "MAXIMUM_NATIONAL_GRID_VOLTAGE");
        objects.add(maxNationalGridVoltage);
        // End create electrical potential quantities
        return objects;
    }
                    
}
