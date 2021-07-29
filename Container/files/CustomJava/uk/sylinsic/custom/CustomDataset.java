package uk.sylinsic.custom;

import static uk.gov.gchq.hqdm.iri.HQDM.ENTITY_NAME;
import static uk.gov.gchq.hqdm.iri.HQDM.HQDM;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.REF_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.USER_BASE;

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
import uk.gov.gchq.hqdm.model.KindOfPhysicalQuantity;
import uk.gov.gchq.hqdm.model.PhysicalQuantity;
import uk.gov.gchq.hqdm.model.Scale;
import uk.gov.gchq.hqdm.model.Thing;
import uk.gov.gchq.hqdm.model.UnitOfMeasure;
import uk.gov.gchq.hqdm.model.impl.ClassOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.IdentificationOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.PhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.ScaleImpl;
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
            new IRI(REF_BASE, "class_of_units"))
            .build();
        classOfUnits.addStringValue(ENTITY_NAME, "CLASS_OF_UNITS");
        objects.add(classOfUnits);

        final ClassOfPhysicalQuantity classOfImperialUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "class_of_imperial_units"))
            .has_Superclass(classOfUnits)
            .build();
        classOfImperialUnits.addStringValue(ENTITY_NAME, "CLASS_OF_IMPERIAL_UNITS");
        objects.add(classOfImperialUnits);

        final ClassOfPhysicalQuantity classOfMetricUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "class_of_metric_units"))
            .has_Superclass(classOfUnits)
            .build();
        classOfMetricUnits.addStringValue(ENTITY_NAME, "CLASS_OF_METRIC_UNITS");
        objects.add(classOfMetricUnits);

        final ClassOfPhysicalQuantity classOfBaseSiUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "class_of_base_si_units"))
            .has_Superclass(classOfMetricUnits)
            .build();
        classOfBaseSiUnits.addStringValue(ENTITY_NAME, "CLASS_OF_BASE_SI_UNITS");
        objects.add(classOfBaseSiUnits);

        final ClassOfPhysicalQuantity classOfDerivedSiUnits = new ClassOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "class_of_derived_si_units"))
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
            new IRI(REF_BASE, "kind_of_arbitrary_unit"))
            .member_Of(classOfUnits)
            .build();
        kindOfArbitraryUnit.addStringValue(ENTITY_NAME, "KIND_OF_ARBITRARY_UNIT");
        objects.add(kindOfArbitraryUnit);

        final UnitOfMeasure unitlessUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "unitless_unit"))
            .member__Of(classOfUnits)
            .build();
        unitlessUnit.addStringValue(ENTITY_NAME, "UNITLESS_UNIT");
        objects.add(unitlessUnit);

        final Scale unitlessScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "unitless_scale"))
            .domain_M(kindOfArbitraryUnit)
            .member__Of(classOfUnits)
            .unit(unitlessUnit)
            .build();
        unitlessScale.addStringValue(ENTITY_NAME, "UNITLESS_SCALE");
        objects.add(unitlessScale);
        // End create arbitrary units

        // Begin create temperature units
        final KindOfPhysicalQuantity kindOfTemperature = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_temperature"))
            .member_Of(classOfUnits)
            .build();
        kindOfTemperature.addStringValue(ENTITY_NAME, "KIND_OF_TEMPERATURE");
        objects.add(kindOfTemperature);

        final UnitOfMeasure degreesCelsiusUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "degrees_celsius_unit"))
            .member__Of(classOfDerivedSiUnits)
            .build();
        degreesCelsiusUnit.addStringValue(ENTITY_NAME, "DEGREES_CELSIUS_UNIT");
        objects.add(degreesCelsiusUnit);

        final Scale degreesCelsiusScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degrees_celsius_scale"))
            .domain_M(kindOfTemperature)
            .member__Of(classOfDerivedSiUnits)
            .unit(degreesCelsiusUnit)
            .build();
        degreesCelsiusScale.addStringValue(ENTITY_NAME, "DEGREES_CELSIUS_SCALE");
        objects.add(degreesCelsiusScale);

        final UnitOfMeasure degreesKelvinUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "degrees_kelvin_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        degreesKelvinUnit.addStringValue(ENTITY_NAME, "DEGREES_KELVIN_UNIT");
        objects.add(degreesKelvinUnit);

        final Scale degreesKelvinScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degrees_kelvin_scale"))
            .domain_M(kindOfTemperature)
            .member__Of(classOfBaseSiUnits)
            .unit(degreesKelvinUnit)
            .build();
        degreesKelvinScale.addStringValue(ENTITY_NAME, "DEGREES_KELVIN_SCALE");
        objects.add(degreesKelvinScale);
        // End create temperature units

        // Begin create time units
        final KindOfPhysicalQuantity kindOfTime = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_time"))
            .member_Of(classOfUnits)
            .build();
        kindOfTime.addStringValue(ENTITY_NAME, "KIND_OF_TIME");
        objects.add(kindOfTime);

        final UnitOfMeasure millisecondUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "millisecond_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        millisecondUnit.addStringValue(ENTITY_NAME, "MILLISECOND_UNIT");
        objects.add(millisecondUnit);

        final Scale millisecondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millisecond_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .unit(millisecondUnit)
            .build();
        millisecondScale.addStringValue(ENTITY_NAME, "MILLISECOND_SCALE");
        objects.add(millisecondScale);

        final UnitOfMeasure secondUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "second_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        secondUnit.addStringValue(ENTITY_NAME, "SECOND_UNIT");
        objects.add(secondUnit);

        final Scale secondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "second_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfBaseSiUnits)
            .unit(secondUnit)
            .build();
        secondScale.addStringValue(ENTITY_NAME, "SECOND_SCALE");
        objects.add(secondScale);

        final UnitOfMeasure minuteUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "minute_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        minuteUnit.addStringValue(ENTITY_NAME, "MINUTE_UNIT");
        objects.add(minuteUnit);

        final Scale minuteScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "minute_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
            .unit(minuteUnit)
            .build();
        minuteScale.addStringValue(ENTITY_NAME, "MINUTE_SCALE");
        objects.add(minuteScale);
        
        final UnitOfMeasure hourUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "hour_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        hourUnit.addStringValue(ENTITY_NAME, "HOUR_UNIT");
        objects.add(hourUnit);
        
        final Scale hourScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "hour_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
            .unit(hourUnit)
            .build();
        hourScale.addStringValue(ENTITY_NAME, "HOUR_SCALE");
        objects.add(hourScale);

        final UnitOfMeasure dayUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "day_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        dayUnit.addStringValue(ENTITY_NAME, "DAY_UNIT");
        objects.add(dayUnit);
        
        final Scale dayScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "day_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .unit(dayUnit)
            .build();
        dayScale.addStringValue(ENTITY_NAME, "DAY_SCALE");
        objects.add(dayScale);

        final UnitOfMeasure weekUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "week_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        weekUnit.addStringValue(ENTITY_NAME, "WEEK_UNIT");
        objects.add(weekUnit);
        
        final Scale weekScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "week_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
            .unit(weekUnit)
            .build();
        weekScale.addStringValue(ENTITY_NAME, "WEEK_SCALE");
        objects.add(weekScale);

        final UnitOfMeasure monthUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "month_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        monthUnit.addStringValue(ENTITY_NAME, "MONTH_UNIT");
        objects.add(monthUnit);
        
        final Scale monthScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "month_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
            .unit(monthUnit)
            .build();
        monthScale.addStringValue(ENTITY_NAME, "MONTH_SCALE");
        objects.add(monthScale);

        final UnitOfMeasure yearUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "year_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        yearUnit.addStringValue(ENTITY_NAME, "YEAR_UNIT");
        objects.add(yearUnit);
        
        final Scale yearScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "year_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
            .unit(yearUnit)
            .build();
        yearScale.addStringValue(ENTITY_NAME, "YEAR_SCALE");
        objects.add(yearScale);
        // End create time units

        // Begin create angle units
        final KindOfPhysicalQuantity kindOfAngle = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_angle"))
            .member_Of(classOfUnits)
            .build();
        kindOfAngle.addStringValue(ENTITY_NAME, "KIND_OF_ANGLE");
        objects.add(kindOfAngle);

        final UnitOfMeasure radianUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "radian_unit"))
            .member__Of(classOfDerivedSiUnits)
            .build();
        radianUnit.addStringValue(ENTITY_NAME, "RADIAN_UNIT");
        objects.add(radianUnit);

        final Scale radianScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "radian_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfDerivedSiUnits)
            .unit(radianUnit)
            .build();
        radianScale.addStringValue(ENTITY_NAME, "RADIAN_SCALE");
        objects.add(radianScale);

        final UnitOfMeasure degreeUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "degree_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        degreeUnit.addStringValue(ENTITY_NAME, "DEGREE_UNIT");
        objects.add(degreeUnit);

        final Scale degreeScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degree_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .unit(degreeUnit)
            .build();
        degreeScale.addStringValue(ENTITY_NAME, "DEGREE_SCALE");
        objects.add(degreeScale);      
        // End create angle units

        // Begin create distance units
        final KindOfPhysicalQuantity kindOfDistance = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_distance"))
            .member_Of(classOfUnits)
            .build();
        kindOfDistance.addStringValue(ENTITY_NAME, "KIND_OF_DISTANCE");
        objects.add(kindOfDistance);

        // Metric measurements
        final UnitOfMeasure millimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "millimetre_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        millimetreUnit.addStringValue(ENTITY_NAME, "MILLIMETRE_UNIT");
        objects.add(millimetreUnit);

        final Scale millimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millimetre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(millimetreUnit)
            .build();
        millimetreScale.addStringValue(ENTITY_NAME, "MILLIMETRE_SCALE");
        objects.add(millimetreScale);
        
        final UnitOfMeasure centimetreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "centimetre_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        centimetreUnit.addStringValue(ENTITY_NAME, "CENTIMETRE_UNIT");
        objects.add(centimetreUnit);

        final Scale centimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(centimetreUnit)
            .build();
        centimetreScale.addStringValue(ENTITY_NAME, "CENTIMETRE_SCALE");
        objects.add(centimetreScale);
        
        final UnitOfMeasure metreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "metre_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        metreUnit.addStringValue(ENTITY_NAME, "METRE_UNIT");
        objects.add(metreUnit);

        final Scale metreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfBaseSiUnits)
            .unit(metreUnit)
            .build();
        metreScale.addStringValue(ENTITY_NAME, "METRE_SCALE");
        objects.add(metreScale);
        
        final UnitOfMeasure kilometreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "kilometre_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        kilometreUnit.addStringValue(ENTITY_NAME, "KILOMETRE_UNIT");
        objects.add(kilometreUnit);

        final Scale kilometreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilometre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .unit(kilometreUnit)
            .build();
        kilometreScale.addStringValue(ENTITY_NAME, "KILOMETRE_SCALE");
        objects.add(kilometreScale);

        // Imperial measurements
        final UnitOfMeasure inchUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "inch_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        inchUnit.addStringValue(ENTITY_NAME, "INCH_UNIT");
        objects.add(inchUnit);
        
        final Scale inchScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(inchUnit)
            .build();
        inchScale.addStringValue(ENTITY_NAME, "INCH_SCALE");
        objects.add(inchScale);

        final UnitOfMeasure footUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "foot_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        footUnit.addStringValue(ENTITY_NAME, "FOOT_UNIT");
        objects.add(footUnit);

        final Scale footScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "foot_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(footUnit)
            .build();
        footScale.addStringValue(ENTITY_NAME, "FOOT_SCALE");
        objects.add(footScale);

        final UnitOfMeasure yardUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "yard_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        yardUnit.addStringValue(ENTITY_NAME, "YARD_UNIT");
        objects.add(yardUnit);

        final Scale yardScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "yard_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(yardUnit)
            .build();
        yardScale.addStringValue(ENTITY_NAME, "YARD_SCALE");
        objects.add(yardScale);

        final UnitOfMeasure mileUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "mile_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        mileUnit.addStringValue(ENTITY_NAME, "MILE_UNIT");
        objects.add(mileUnit);

        final Scale mileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mile_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(mileUnit)
            .build();
        mileScale.addStringValue(ENTITY_NAME, "MILE_SCALE");
        objects.add(mileScale);

        final UnitOfMeasure nauticalMileUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "nautical_mile_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        nauticalMileUnit.addStringValue(ENTITY_NAME, "NAUTICAL_MILE_UNIT");
        objects.add(nauticalMileUnit);

        final Scale nauticalMileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "nautical_mile_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .unit(nauticalMileUnit)
            .build();
        nauticalMileScale.addStringValue(ENTITY_NAME, "NAUTICAL_MILE_SCALE");
        objects.add(nauticalMileScale);
        // End create distance units

        // Begin create area units
        final KindOfPhysicalQuantity kindOfArea = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_area"))
            .member_Of(classOfUnits)
            .build();
        kindOfArea.addStringValue(ENTITY_NAME, "KIND_OF_AREA");
        objects.add(kindOfArea);
                
        // Metric units
        final UnitOfMeasure millimetreSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "millimetre_squared_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        millimetreSquaredUnit.addStringValue(ENTITY_NAME, "MILLIMETRE_SQUARED_UNIT");
        objects.add(millimetreSquaredUnit);

        final Scale millimetreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millimetre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .unit(millimetreSquaredUnit)
            .build(); 
        millimetreSquaredScale.addStringValue(ENTITY_NAME, "MILLIMETRE_SQUARED_SCALE");
        objects.add(millimetreSquaredScale);

        final UnitOfMeasure centimetreSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "centimetre_squared_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        centimetreSquaredUnit.addStringValue(ENTITY_NAME, "CENTIMETRE_SQUARED_UNIT");
        objects.add(centimetreSquaredUnit);

        final Scale centimetreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .unit(centimetreSquaredUnit)
            .build(); 
        centimetreSquaredScale.addStringValue(ENTITY_NAME, "CENTIMETRE_SQUARED_SCALE");
        objects.add(centimetreSquaredScale);

        final UnitOfMeasure metreSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "metre_squared_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        metreSquaredUnit.addStringValue(ENTITY_NAME, "METRE_SQUARED_UNIT");
        objects.add(metreSquaredUnit);

        final Scale metreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfDerivedSiUnits)
            .unit(metreSquaredUnit)
            .build(); 
        metreSquaredScale.addStringValue(ENTITY_NAME, "METRE_SQUARED_SCALE");
        objects.add(metreSquaredScale);

        final UnitOfMeasure kilometreSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "kilometre_squared_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        kilometreSquaredUnit.addStringValue(ENTITY_NAME, "KILOMETRE_SQUARED_UNIT");
        objects.add(kilometreSquaredUnit);

        final Scale kilometreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilometre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .unit(kilometreSquaredUnit)
            .build(); 
        kilometreSquaredScale.addStringValue(ENTITY_NAME, "KILOMETRE_SQUARED_SCALE");
        objects.add(kilometreSquaredScale);

        // Imperial units
        final UnitOfMeasure inchSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "inch_squared_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        inchSquaredUnit.addStringValue(ENTITY_NAME, "INCH_SQUARED_UNIT");
        objects.add(inchSquaredUnit);

        final Scale inchSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(inchSquaredUnit)
            .build();
        inchSquaredScale.addStringValue(ENTITY_NAME, "INCH_SQUARED_SCALE");
        objects.add(inchSquaredScale);

        final UnitOfMeasure footSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "foot_squared_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        footSquaredUnit.addStringValue(ENTITY_NAME, "FOOT_SQUARED_UNIT");
        objects.add(footSquaredUnit);

        final Scale footSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "foot_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(footSquaredUnit)
            .build();
        footSquaredScale.addStringValue(ENTITY_NAME, "FOOT_SQUARED_SCALE");
        objects.add(footSquaredScale);

        final UnitOfMeasure yardSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "yard_squared_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        yardSquaredUnit.addStringValue(ENTITY_NAME, "YARD_SQUARED_UNIT");
        objects.add(yardSquaredUnit);

        final Scale yardSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "yard_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(yardSquaredUnit)
            .build();
        yardSquaredScale.addStringValue(ENTITY_NAME, "YARD_SQUARED_SCALE");
        objects.add(yardSquaredScale);

        final UnitOfMeasure mileSquaredUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "mile_squared_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        mileSquaredUnit.addStringValue(ENTITY_NAME, "MILE_SQUARED_UNIT");
        objects.add(mileSquaredUnit);

        final Scale mileSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mile_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(mileSquaredUnit)
            .build();
        mileSquaredScale.addStringValue(ENTITY_NAME, "MILE_SQUARED_SCALE");
        objects.add(mileSquaredScale);

        final UnitOfMeasure acreUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "acre_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        acreUnit.addStringValue(ENTITY_NAME, "ACRE_UNIT");
        objects.add(acreUnit);

        final Scale acreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "acre_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .unit(acreUnit)
            .build();
        acreScale.addStringValue(ENTITY_NAME, "ACRE_SCALE");
        objects.add(acreScale);
        // End create area units

        // Begin create volume units
        final KindOfPhysicalQuantity kindOfVolume = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_volume"))
            .member_Of(classOfUnits)
            .build();
        kindOfVolume.addStringValue(ENTITY_NAME, "KIND_OF_VOLUME");
        objects.add(kindOfVolume);

        // Metric units
        final UnitOfMeasure centimetreCubedUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "centimetre_cubed_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        centimetreCubedUnit.addStringValue(ENTITY_NAME, "CENTIMETRE_CUBED_UNIT");
        objects.add(centimetreCubedUnit);

        final Scale centimetreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .unit(centimetreCubedUnit)
            .build();
        centimetreCubedScale.addStringValue(ENTITY_NAME, "CENTIMETRE_CUBED_SCALE");
        centimetreCubedScale.addStringValue(equivalentTo, "MILLILITRE");
        objects.add(centimetreCubedScale);

        final UnitOfMeasure decimetreCubedUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "decimetre_cubed_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        decimetreCubedUnit.addStringValue(ENTITY_NAME, "DECIMETRE_CUBED_UNIT");
        objects.add(decimetreCubedUnit);

        final Scale decimetreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "decimetre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .unit(decimetreCubedUnit)
            .build();
        decimetreCubedScale.addStringValue(ENTITY_NAME, "DECIMETRE_CUBED_SCALE");
        decimetreCubedScale.addStringValue(equivalentTo, "LITRE");
        objects.add(decimetreCubedScale);
        
        final UnitOfMeasure metreCubedUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "metre_cubed_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        metreCubedUnit.addStringValue(ENTITY_NAME, "METRE_CUBED_UNIT");
        objects.add(metreCubedUnit);

        final Scale metreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfDerivedSiUnits)
            .build();
        metreCubedScale.addStringValue(ENTITY_NAME, "METRE_CUBED_SCALE");
        metreCubedScale.addStringValue(equivalentTo, "KILOLITRE");
        objects.add(metreCubedScale);

        // Imperial units
        final UnitOfMeasure inchCubedUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "inch_cubed_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        inchCubedUnit.addStringValue(ENTITY_NAME, "INCH_CUBED_UNIT");
        objects.add(inchCubedUnit);

        final Scale inchCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(inchCubedUnit)
            .build();
        inchCubedScale.addStringValue(ENTITY_NAME, "INCH_CUBED_SCALE");
        objects.add(inchCubedScale);

        final UnitOfMeasure feetCubedUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "feet_cubed_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        feetCubedUnit.addStringValue(ENTITY_NAME, "FEET_CUBED_UNIT");
        objects.add(feetCubedUnit);

        final Scale feetCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "feet_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(feetCubedUnit)
            .build();
        feetCubedScale.addStringValue(ENTITY_NAME, "FEET_CUBED_SCALE");
        objects.add(feetCubedScale);

        final UnitOfMeasure fluidOunceUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "fluid_ounce_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        fluidOunceUnit.addStringValue(ENTITY_NAME, "FLUID_OUNCE_UNIT");
        objects.add(fluidOunceUnit);

        final Scale fluidOunceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "fluid_ounce_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(fluidOunceUnit)
            .build();
        fluidOunceScale.addStringValue(ENTITY_NAME, "FLUID_OUNCE_SCALE");
        objects.add(fluidOunceScale);

        final UnitOfMeasure pintUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "pint_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        pintUnit.addStringValue(ENTITY_NAME, "PINT_UNIT");
        objects.add(pintUnit);

        final Scale pintScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "pint_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(pintUnit)
            .build();
        pintScale.addStringValue(ENTITY_NAME, "PINT_SCALE");
        objects.add(pintScale);

        final UnitOfMeasure quartUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "quart_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        quartUnit.addStringValue(ENTITY_NAME, "QUART_UNIT");
        objects.add(quartUnit);

        final Scale quartScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "quart_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(quartUnit)
            .build();
        quartScale.addStringValue(ENTITY_NAME, "QUART_SCALE");
        objects.add(quartScale);

        final UnitOfMeasure gallonUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "gallon_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        gallonUnit.addStringValue(ENTITY_NAME, "GALLON_UNIT");
        objects.add(gallonUnit);

        final Scale gallonScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "gallon_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .unit(gallonUnit)
            .build();
        gallonScale.addStringValue(ENTITY_NAME, "GALLON_SCALE");
        objects.add(gallonScale);
        // End create volume units

        // Begin create mass units
        final KindOfPhysicalQuantity kindOfMass = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_mass"))
            .member_Of(classOfUnits)
            .build();
        kindOfMass.addStringValue(ENTITY_NAME, "KIND_OF_MASS");
        objects.add(kindOfMass);

        // Metric units
        final UnitOfMeasure milligramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "milligram_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        milligramUnit.addStringValue(ENTITY_NAME, "MILLIGRAM_UNIT");
        objects.add(milligramUnit);

        final Scale milligramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "milligram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(milligramUnit)
            .build();
        milligramScale.addStringValue(ENTITY_NAME, "MILLIGRAM_SCALE");
        objects.add(milligramScale);

        final UnitOfMeasure gramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "gram_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        gramUnit.addStringValue(ENTITY_NAME, "GRAM_UNIT");
        objects.add(gramUnit);

        final Scale gramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "gram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(gramUnit)
            .build();
        gramScale.addStringValue(ENTITY_NAME, "GRAM_SCALE");
        objects.add(gramScale);

        final UnitOfMeasure kilogramUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "kilogram_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        kilogramUnit.addStringValue(ENTITY_NAME, "KILOGRAM_UNIT");
        objects.add(kilogramUnit);

        final Scale kilogramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilogram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfBaseSiUnits)
            .unit(kilogramUnit)
            .build();
        kilogramScale.addStringValue(ENTITY_NAME, "KILOGRAM_SCALE");
        objects.add(kilogramScale);

        final UnitOfMeasure tonneUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "tonne_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        tonneUnit.addStringValue(ENTITY_NAME, "TONNE_UNIT");
        objects.add(tonneUnit);

        final Scale tonneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "tonne_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .unit(tonneUnit)
            .build();
        tonneScale.addStringValue(ENTITY_NAME, "TONNE_SCALE");
        objects.add(tonneScale);

        // Imperial units
        final UnitOfMeasure ounceUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "ounce_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        ounceUnit.addStringValue(ENTITY_NAME, "OUNCE_UNIT");
        objects.add(ounceUnit);

        final Scale ounceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "ounce_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(ounceUnit)
            .build();
        ounceScale.addStringValue(ENTITY_NAME, "OUNCE_SCALE");
        objects.add(ounceScale);

        final UnitOfMeasure poundUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "pound_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        poundUnit.addStringValue(ENTITY_NAME, "POUND_UNIT");
        objects.add(poundUnit);

        final Scale poundScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "pound_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(poundUnit)
            .build();
        poundScale.addStringValue(ENTITY_NAME, "POUND_SCALE");
        objects.add(poundScale);

        final UnitOfMeasure stoneUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "stone_unit"))
            .member__Of(classOfImperialUnits)
            .build();
        stoneUnit.addStringValue(ENTITY_NAME, "STONE_UNIT");
        objects.add(stoneUnit);

        final Scale stoneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "stone_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .unit(stoneUnit)
            .build();
        stoneScale.addStringValue(ENTITY_NAME, "STONE_SCALE");
        objects.add(stoneScale);
        // End create mass units
        
        // Begin create light units
        final KindOfPhysicalQuantity kindOfLightIntensity = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_light_intensity"))
            .member_Of(classOfUnits)
            .build();
        kindOfLightIntensity.addStringValue(ENTITY_NAME, "KIND_OF_LIGHT_INTENSITY");
        objects.add(kindOfLightIntensity);

        final UnitOfMeasure candelaUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "candela_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        candelaUnit.addStringValue(ENTITY_NAME, "CANDELA_UNIT");
        objects.add(candelaUnit);

        final Scale candelaScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "candela_scale"))
            .domain_M(kindOfLightIntensity)
            .member__Of(classOfBaseSiUnits)
            .unit(candelaUnit)
            .build();
        candelaScale.addStringValue(ENTITY_NAME, "CANDELA_SCALE");
        objects.add(candelaScale);
        // End create light units

        // Begin create molecular units
        final KindOfPhysicalQuantity kindOfMolecularContents = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_molecular_contents"))
            .member_Of(classOfUnits)
            .build();
        kindOfMolecularContents.addStringValue(ENTITY_NAME, "KIND_OF_MOLECULAR_CONTENTS");
        objects.add(kindOfMolecularContents);

        final UnitOfMeasure moleUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "mole_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        moleUnit.addStringValue(ENTITY_NAME, "MOLE_UNIT");
        objects.add(moleUnit);

        final Scale moleScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mole_scale"))
            .domain_M(kindOfMolecularContents)
            .member__Of(classOfBaseSiUnits)
            .unit(moleUnit)
            .build();
        moleScale.addStringValue(ENTITY_NAME, "MOLE_SCALE");
        objects.add(moleScale);
        // End create molecular units

        // Begin create electrical current units
        final KindOfPhysicalQuantity kindOfElectricCurrent = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_electrical_current"))
            .member_Of(classOfUnits)
            .build();
        kindOfElectricCurrent.addStringValue(ENTITY_NAME, "KIND_OF_ELECTRICAL_CURRENT");
        objects.add(kindOfElectricCurrent);

        final UnitOfMeasure milliampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "milliampere_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        milliampereUnit.addStringValue(ENTITY_NAME, "MILLIAMPERE_UNIT");
        objects.add(milliampereUnit);

        final Scale milliampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "milliampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
            .unit(milliampereUnit)
            .build();
        milliampereScale.addStringValue(ENTITY_NAME, "MILLIAMPERE_SCALE");
        objects.add(milliampereScale);

        final UnitOfMeasure ampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "ampere_unit"))
            .member__Of(classOfBaseSiUnits)
            .build();
        ampereUnit.addStringValue(ENTITY_NAME, "AMPERE_UNIT");
        objects.add(ampereUnit);

        final Scale ampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "ampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfBaseSiUnits)
            .unit(ampereUnit)
            .build();
        ampereScale.addStringValue(ENTITY_NAME, "AMPERE_SCALE");
        objects.add(ampereScale);

        final UnitOfMeasure kiloampereUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "kiloampere_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        kiloampereUnit.addStringValue(ENTITY_NAME, "KILOAMPERE_UNIT");
        objects.add(kiloampereUnit);

        final Scale kiloampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kiloampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
            .unit(kiloampereUnit)
            .build();
        kiloampereScale.addStringValue(ENTITY_NAME, "KILOAMPERE_SCALE");
        objects.add(kiloampereScale);
        // End create electrical current units

        // Begin create electrical potential units
        final KindOfPhysicalQuantity kindOfElectricalPotential = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_electrical_potential"))
            .member_Of(classOfUnits)
            .build();
        kindOfElectricalPotential.addStringValue(ENTITY_NAME, "KIND_OF_ELECTRICAL_POTENTIAL");
        objects.add(kindOfElectricalPotential);

        final UnitOfMeasure millivoltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "millivolt_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        millivoltUnit.addStringValue(ENTITY_NAME, "MILLIVOLT_UNIT");
        objects.add(millivoltUnit);

        final Scale millivoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millivolt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .unit(millivoltUnit)
            .build();
        millivoltScale.addStringValue(ENTITY_NAME, "MILLIVOLT_SCALE");
        objects.add(millivoltScale);

        final UnitOfMeasure voltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "volt_unit"))
            .member__Of(classOfDerivedSiUnits)
            .build();
        voltUnit.addStringValue(ENTITY_NAME, "VOLT_UNIT");
        objects.add(voltUnit);

        final Scale voltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "volt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfDerivedSiUnits)
            .unit(voltUnit)
            .build();
        voltScale.addStringValue(ENTITY_NAME, "VOLT_SCALE");
        objects.add(voltScale);

        final UnitOfMeasure kilovoltUnit = new UnitOfMeasureImpl.Builder(
            new IRI(REF_BASE, "kilovolt_unit"))
            .member__Of(classOfMetricUnits)
            .build();
        kilovoltUnit.addStringValue(ENTITY_NAME, "KILOVOLT_UNIT");
        objects.add(kilovoltUnit);

        final Scale kilovoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilovolt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .unit(kilovoltUnit)
            .build();
        kilovoltScale.addStringValue(ENTITY_NAME, "KILOVOLT_SCALE");
        objects.add(kilovoltScale);
        // End create electrical potential units
        
        // Begin create temperature quantities
        final PhysicalQuantity roomTempCelsius = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "room_temp_celsius"))
            .member_Of_Kind_M(kindOfTemperature)
            .build();
        roomTempCelsius.addStringValue(ENTITY_NAME, "ROOM_TEMP_CELSIUS");
        objects.add(roomTempCelsius);
        
        final IdentificationOfPhysicalQuantity identificationOfRoomTempCelsius = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_room_temp_celsius"))
                .represented_M(roomTempCelsius)
                .uses_M(degreesCelsiusScale)
                .value__M(21.0)
                .build();
        identificationOfRoomTempCelsius.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_ROOM_TEMP_CELSIUS");
        objects.add(identificationOfRoomTempCelsius);
        
        final PhysicalQuantity bodyTempKelvin = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "body_temp_kelvin"))
            .member_Of_Kind_M(kindOfTemperature)
            .build();
        bodyTempKelvin.addStringValue(ENTITY_NAME, "BODY_TEMP_KELVIN");
        objects.add(bodyTempKelvin);

        final IdentificationOfPhysicalQuantity identificationOfBodyTempKelvin = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_body_temp_kelvin"))
                .represented_M(bodyTempKelvin)
                .uses_M(degreesKelvinScale)
                .value__M(310.15)
                .build();
        identificationOfBodyTempKelvin.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_BODY_TEMP_KELVIN");
        objects.add(identificationOfBodyTempKelvin);
        // End create temperature quantities

        // Begin create time quantities
        final PhysicalQuantity averageBlinkTimeMilliseconds = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "average_blink_time_milliseconds"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        averageBlinkTimeMilliseconds.addStringValue(ENTITY_NAME, "AVERAGE_BLINK_TIME_MILLISECONDS");
        objects.add(averageBlinkTimeMilliseconds);

        final IdentificationOfPhysicalQuantity identificationOfAverageBlinkTimeMilliseconds = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_average_blink_time_milliseconds"))
                .represented_M(averageBlinkTimeMilliseconds)
                .uses_M(millisecondScale)
                .value__M(100)
                .build();
        identificationOfAverageBlinkTimeMilliseconds.addStringValue(ENTITY_NAME, 
            "IDENTIFICATION_OF_AVERAGE_BLINK_TIME_MILLISECONDS");
        objects.add(identificationOfAverageBlinkTimeMilliseconds);

        final PhysicalQuantity oneHundredMetreRecordSeconds = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "one_hundred_metre_record_seconds"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        oneHundredMetreRecordSeconds.addStringValue(ENTITY_NAME, "ONE_HUNDRED_METRE_RECORD_SECONDS");
        objects.add(oneHundredMetreRecordSeconds);

        final IdentificationOfPhysicalQuantity identificationOfOneHundredMetreRecordSeconds = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_one_hundred_metre_record_seconds"))
                .represented_M(oneHundredMetreRecordSeconds)
                .uses_M(secondScale)
                .value__M(9.58)
                .build();
        identificationOfOneHundredMetreRecordSeconds.addStringValue(ENTITY_NAME, 
            "IDENTIFICATION_OF_ONE_HUNDRED_METRE_RECORD_SECONDS");
        objects.add(identificationOfOneHundredMetreRecordSeconds);

        final PhysicalQuantity minimumLengthOfFootballGameMinutes = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "minimum_length_of_football_game_minutes"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        minimumLengthOfFootballGameMinutes.addStringValue(ENTITY_NAME, "MINIMUM_LENGTH_OF_FOOTBALL_GAME_MINUTES");
        objects.add(minimumLengthOfFootballGameMinutes);

        final IdentificationOfPhysicalQuantity identificationOfMinimumLengthOfFootballGameMinutes = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_minimum_length_of_football_game_minutes"))
                .represented_M(minimumLengthOfFootballGameMinutes)
                .uses_M(minuteScale)
                .value__M(80)
                .build();
        identificationOfMinimumLengthOfFootballGameMinutes.addStringValue(ENTITY_NAME, 
            "IDENTIFICATION_OF_MINIMUM_LENGTH_OF_FOOTBALL_GAME_MINUTES");
        objects.add(identificationOfMinimumLengthOfFootballGameMinutes);

        final PhysicalQuantity averagePrivatePilotLicenseHours = new PhysicalQuantity.Builder(
            new IRI(USER_BASE, "average_private_pilot_license_hours"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        averagePrivatePilotLicenseHours.addStringValue(ENTITY_NAME, "AVERAGE_PRIVATE_PILOT_LICENSE_HOURS");
        objects.add(averagePrivatePilotLicenseHours);

        final IdentificationOfPhysicalQuantity identificationOfAveragePrivatePilotLicenseHours = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_average_private_pilot_license_hours"))
                .represented_M(averagePrivatePilotLicenseHours)
                .uses_M(hourScale)
                .value__M(60)
                .build();
        identificationOfAveragePrivatePilotLicenseHours.addStringValue(ENTITY_NAME,
            "IDENTIFICATION_OF_AVERAGE_PRIVATE_PILOT_LICENSE_HOURS");
        objects.add(identificationOfAveragePrivatePilotLicenseHours);

        final PhysicalQuantity leapYearDays = new PhysicalQuantity.Builder(
            new IRI(USER_BASE, "leap_year_days"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        leapYearDays.addStringValue(ENTITY_NAME, "LEAP_YEAR_DAYS");
        objects.add(leapYearDays);

        final IdentificationOfPhysicalQuantity identificationOfLeapYearDays = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_leap_year_days"))
                .represented_M(leapYearDays)
                .uses_M(dayScale)
                .value__M(366)
                .build();
        identificationOfLeapYearDays.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_LEAP_YEAR_DAYS");
        objects.add(identificationOfLeapYearDays);

        final PhysicalQuantity schoolYearWeeks = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "school_year_weeks"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        schoolYearWeeks.addStringValue(ENTITY_NAME, "SCHOOL_YEAR_WEEKS");
        objects.add(schoolYearWeeks);

        final IdentificationOfPhysicalQuantity identificationOfSchoolYearWeeks = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_school_year_weeks"))
                .represented_M(schoolYearWeeks)
                .uses_M(weekScale)
                .value__M(39)
                .build();
        identificationOfSchoolYearWeeks.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_SCHOOL_YEAR_WEEKS");
        objects.add(identificationOfSchoolYearWeeks);

        final PhysicalQuantity pregnancyMonths = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "pregnancy_months"))
            .member_Of_Kind_M(monthScale)
            .build();
        pregnancyMonths.addStringValue(ENTITY_NAME, "PREGNANCY_MONTHS");
        objects.add(pregnancyMonths);

        final IdentificationOfPhysicalQuantity identificationOfPregnancyMonths = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_pregnancy_months"))
                .represented_M(pregnancyMonths)
                .uses_M(monthScale)
                .value__M(9)
                .build();
        identificationOfPregnancyMonths.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_PREGNANCY_MONTHS");
        objects.add(identificationOfPregnancyMonths);

        final PhysicalQuantity sixthFormYears = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "sixth_form_years"))
            .member_Of_Kind_M(kindOfTime)
            .build();
        sixthFormYears.addStringValue(ENTITY_NAME, "SIXTH_FORM_YEARS");
        objects.add(sixthFormYears);

        final IdentificationOfPhysicalQuantity identificationOfSixthFormYears = new 
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_sixth_form_years"))
                .represented_M(sixthFormYears)
                .uses_M(yearScale)
                .value_M(2)
                .build();
        identificationOfSixthFormYears.addStringValue(ENTITY_NAME, "IDENTIFICATION_OF_SIXTH_FORM_YEARS");
        objects.add(identificationOfSixthFormYears);
        // End create time quantities
        
        // Begin create angle quantities
        final PhysicalQuantity anglesInCircleRadians = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "angles_in_circle_radians"))
            .member_Of_Kind_M(kindOfAngle)
            .build();
        anglesInCircleRadians.addStringValue(ENTITY_NAME, "ANGLES_IN_CIRCLE_RADIANS");
        objects.add(anglesInCircleRadians);

        final IdentificationOfPhysicalQuantity identificationOfAnglesInCircleRadians = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_angles_in_circle_radians"))
                .represented_M(anglesInCircleRadians)
                .uses_M(radianScale)
                .value_M(6.28)
                .build();
        identificationOfAnglesInCircleRadians.addStringValue(ENTITY_NAME, 
            "IDENTIFICATION_OF_ANGLES_IN_CIRCLES_RADIANS");
        objects.add(identificationOfAnglesInCircleRadians);

        final PhysicalQuantity anglesInCircleDegrees = new PhysicalQuantityImpl.Builder(
            new IRI(USER_BASE, "angles_in_circle_degrees"))
            .member_Of_Kind_M(kindOfAngle)
            .build();
        anglesInCircleDegrees.addStringValue(ENTITY_NAME, "ANGLES_IN_CIRCLE_DEGREES");
        objects.add(anglesInCircleDegrees);

        final IdentificationOfPhysicalQuantity identificationOfAnglesInCircleDegrees = new
            IdentificationOfPhysicalQuantityImpl.Builder(
                new IRI(USER_BASE, "identification_of_angles_in_circle_degrees"))
                .represented_M(anglesInCircleDegrees)
                .uses_M(degreeScale)
                .value_M(360)
                .build();
        identificationOfAnglesInCircleDegrees.addStringValue(ENTITY_NAME,
            "IDENTIFICATION_OF_ANGLES_IN_CIRCLE_DEGREES");
        objects.add(identificationOfAnglesInCircleDegrees);
        // End create angle quantities
        return objects;
    }
                    
}
