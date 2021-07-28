package uk.sylinsic.custom;

import static uk.gov.gchq.hqdm.iri.HQDM.ENTITY_NAME;
import static uk.gov.gchq.hqdm.iri.HQDM.HQDM;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.REF_BASE;

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
import uk.gov.gchq.hqdm.model.KindOfPhysicalQuantity;
import uk.gov.gchq.hqdm.model.Scale;
import uk.gov.gchq.hqdm.model.Thing;
import uk.gov.gchq.hqdm.model.impl.ClassOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.KindOfPhysicalQuantityImpl;
import uk.gov.gchq.hqdm.model.impl.ScaleImpl;


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

        // Begin create temperature units
        final KindOfPhysicalQuantity kindOfTemperature = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_temperature"))
            .member_Of(classOfUnits)
            .build();
        kindOfTemperature.addStringValue(ENTITY_NAME, "KIND_OF_TEMPERATURE");
        objects.add(kindOfTemperature);

        final Scale degreesCelsiusScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degrees_celsius_scale"))
            .domain_M(kindOfTemperature)
            .member__Of(classOfDerivedSiUnits)
            .build();
        degreesCelsiusScale.addStringValue(ENTITY_NAME, "DEGREES_CELSIUS_SCALE");
        objects.add(degreesCelsiusScale);

        final Scale degreesKelvinScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degrees_kelvin_scale"))
            .domain_M(kindOfTemperature)
            .member__Of(classOfBaseSiUnits)
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

        final Scale millisecondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millisecond_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        millisecondScale.addStringValue(ENTITY_NAME, "MILLISECOND_SCALE");
        objects.add(millisecondScale);

        final Scale secondScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "second_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfBaseSiUnits)
            .build();
        secondScale.addStringValue(ENTITY_NAME, "SECOND_SCALE");
        objects.add(secondScale);

        final Scale minuteScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "minute_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        minuteScale.addStringValue(ENTITY_NAME, "MINUTE_SCALE");
        objects.add(minuteScale);

        final Scale hourScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "hour_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        hourScale.addStringValue(ENTITY_NAME, "HOUR_SCALE");
        objects.add(hourScale);

        final Scale dayScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "day_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        dayScale.addStringValue(ENTITY_NAME, "DAY_SCALE");
        objects.add(dayScale);
        
        final Scale weekScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "week_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        weekScale.addStringValue(ENTITY_NAME, "WEEK_SCALE");
        objects.add(weekScale);

        final Scale monthScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "month_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
            .build();
        monthScale.addStringValue(ENTITY_NAME, "MONTH_SCALE");
        objects.add(monthScale);

        final Scale yearScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "year_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfMetricUnits)
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

        final Scale radianScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "radian_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfDerivedSiUnits)
            .build();
        radianScale.addStringValue(ENTITY_NAME, "RADIAN_SCALE");
        objects.add(radianScale);

        final Scale degreeScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "degree_scale"))
            .domain_M(kindOfTime)
            .member__Of(classOfImperialUnits)
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
        final Scale millimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millimetre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .build();
        millimetreScale.addStringValue(ENTITY_NAME, "MILLIMETRE_SCALE");
        objects.add(millimetreScale);
        
        final Scale centimetreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .build();
        centimetreScale.addStringValue(ENTITY_NAME, "CENTIMETRE_SCALE");
        objects.add(centimetreScale);
        
        final Scale metreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfBaseSiUnits)
            .build();
        metreScale.addStringValue(ENTITY_NAME, "METRE_SCALE");
        objects.add(metreScale);
        
        final Scale kilometreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilometre_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfMetricUnits)
            .build();
        kilometreScale.addStringValue(ENTITY_NAME, "KILOMETRE_SCALE");
        objects.add(kilometreScale);

        // Imperial measurements
        final Scale inchScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .build();
        inchScale.addStringValue(ENTITY_NAME, "INCH_SCALE");
        objects.add(inchScale);

        final Scale footScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "foot_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .build();
        footScale.addStringValue(ENTITY_NAME, "FOOT_SCALE");
        objects.add(footScale);

        final Scale yardScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "yard_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .build();
        yardScale.addStringValue(ENTITY_NAME, "YARD_SCALE");
        objects.add(yardScale);

        final Scale mileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mile_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
            .build();
        mileScale.addStringValue(ENTITY_NAME, "MILE_SCALE");
        objects.add(mileScale);

        final Scale nauticalMileScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "nautical_mile_scale"))
            .domain_M(kindOfDistance)
            .member__Of(classOfImperialUnits)
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
        final Scale millimetreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millimetre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .build(); 
        millimetreSquaredScale.addStringValue(ENTITY_NAME, "MILLIMETRE_SQUARED_SCALE");
        objects.add(millimetreSquaredScale);

        final Scale centimetreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .build(); 
        centimetreSquaredScale.addStringValue(ENTITY_NAME, "CENTIMETRE_SQUARED_SCALE");
        objects.add(centimetreSquaredScale);

        final Scale metreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfDerivedSiUnits)
            .build(); 
        metreSquaredScale.addStringValue(ENTITY_NAME, "METRE_SQUARED_SCALE");
        objects.add(metreSquaredScale);

        final Scale kilometreSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilometre_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfMetricUnits)
            .build(); 
        kilometreSquaredScale.addStringValue(ENTITY_NAME, "KILOMETRE_SQUARED_SCALE");
        objects.add(kilometreSquaredScale);

        // Imperial units
        final Scale inchSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .build();
        inchSquaredScale.addStringValue(ENTITY_NAME, "INCH_SQUARED_SCALE");
        objects.add(inchSquaredScale);

        final Scale footSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "foot_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .build();
        footSquaredScale.addStringValue(ENTITY_NAME, "FOOT_SQUARED_SCALE");
        objects.add(footSquaredScale);

        final Scale yardSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "yard_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .build();
        yardSquaredScale.addStringValue(ENTITY_NAME, "YARD_SQUARED_SCALE");
        objects.add(yardSquaredScale);

        final Scale mileSquaredScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mile_squared_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
            .build();
        mileSquaredScale.addStringValue(ENTITY_NAME, "MILE_SQUARED_SCALE");
        objects.add(mileSquaredScale);

        final Scale acreScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "acre_scale"))
            .domain_M(kindOfArea)
            .member__Of(classOfImperialUnits)
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
        final Scale centimetreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "centimetre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .build();
        centimetreCubedScale.addStringValue(ENTITY_NAME, "CENTIMETRE_CUBED_SCALE");
        centimetreCubedScale.addStringValue(equivalentTo, "MILLILITRE");
        objects.add(centimetreCubedScale);

        final Scale decimetreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "decimetre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfMetricUnits)
            .build();
        decimetreCubedScale.addStringValue(ENTITY_NAME, "DECIMETRE_CUBED_SCALE");
        decimetreCubedScale.addStringValue(equivalentTo, "LITRE");
        objects.add(decimetreCubedScale);
        
        final Scale metreCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "metre_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfDerivedSiUnits)
            .build();
        metreCubedScale.addStringValue(ENTITY_NAME, "METRE_CUBED_SCALE");
        metreCubedScale.addStringValue(equivalentTo, "KILOLITRE");
        objects.add(metreCubedScale);

        // Imperial units
        final Scale inchCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "inch_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .build();
        inchCubedScale.addStringValue(ENTITY_NAME, "INCH_CUBED_SCALE");
        objects.add(inchCubedScale);

        final Scale feetCubedScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "feet_cubed_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .build();
        feetCubedScale.addStringValue(ENTITY_NAME, "FEET_CUBED_SCALE");
        objects.add(feetCubedScale);

        final Scale fluidOunceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "fluid_ounce_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .build();
        fluidOunceScale.addStringValue(ENTITY_NAME, "FLUID_OUNCE_SCALE");
        objects.add(fluidOunceScale);

        final Scale pintScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "pint_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .build();
        pintScale.addStringValue(ENTITY_NAME, "PINT_SCALE");
        objects.add(pintScale);

        final Scale quartScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "quart_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
            .build();
        quartScale.addStringValue(ENTITY_NAME, "QUART_SCALE");
        objects.add(quartScale);

        final Scale gallonScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "gallon_scale"))
            .domain_M(kindOfVolume)
            .member__Of(classOfImperialUnits)
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
        final Scale milligramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "milligram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .build();
        milligramScale.addStringValue(ENTITY_NAME, "MILLIGRAM_SCALE");
        objects.add(milligramScale);

        final Scale gramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "gram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .build();
        gramScale.addStringValue(ENTITY_NAME, "GRAM_SCALE");
        objects.add(gramScale);

        final Scale kilogramScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilogram_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfBaseSiUnits)
            .build();
        kilogramScale.addStringValue(ENTITY_NAME, "KILOGRAM_SCALE");
        objects.add(kilogramScale);

        final Scale tonneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "tonne_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfMetricUnits)
            .build();
        tonneScale.addStringValue(ENTITY_NAME, "TONNE_SCALE");
        objects.add(tonneScale);

        // Imperial units
        final Scale ounceScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "ounce_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .build();
        ounceScale.addStringValue(ENTITY_NAME, "OUNCE_SCALE");
        objects.add(ounceScale);

        final Scale poundScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "pound_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
            .build();
        poundScale.addStringValue(ENTITY_NAME, "POUND_SCALE");
        objects.add(poundScale);

        final Scale stoneScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "stone_scale"))
            .domain_M(kindOfMass)
            .member__Of(classOfImperialUnits)
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

        final Scale candelaScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "candela_scale"))
            .domain_M(kindOfLightIntensity)
            .member__Of(classOfBaseSiUnits)
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

        final Scale moleScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "mole_scale"))
            .domain_M(kindOfMolecularContents)
            .member__Of(classOfBaseSiUnits)
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

        final Scale milliampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "milliampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
            .build();
        milliampereScale.addStringValue(ENTITY_NAME, "MILLIAMPERE_SCALE");
        objects.add(milliampereScale);

        final Scale ampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "ampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfBaseSiUnits)
            .build();
        ampereScale.addStringValue(ENTITY_NAME, "AMPERE_SCALE");
        objects.add(ampereScale);

        final Scale kiloampereScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kiloampere_scale"))
            .domain_M(kindOfElectricCurrent)
            .member__Of(classOfMetricUnits)
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

        final Scale millivoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "millivolt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .build();
        millivoltScale.addStringValue(ENTITY_NAME, "MILLIVOLT_SCALE");
        objects.add(millivoltScale);

        final Scale voltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "volt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfDerivedSiUnits)
            .build();
        voltScale.addStringValue(ENTITY_NAME, "VOLT_SCALE");
        objects.add(voltScale);

        final Scale kilovoltScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "kilovolt_scale"))
            .domain_M(kindOfElectricalPotential)
            .member__Of(classOfMetricUnits)
            .build();
        kilovoltScale.addStringValue(ENTITY_NAME, "KILOVOLT_SCALE");
        objects.add(kilovoltScale);
        // End create electrical potential units

        // Begin create arbitrary units
        final KindOfPhysicalQuantity kindOfArbitraryUnit = new KindOfPhysicalQuantityImpl.Builder(
            new IRI(REF_BASE, "kind_of_arbitrary_unit"))
            .member_Of(classOfUnits)
            .build();
        kindOfArbitraryUnit.addStringValue(ENTITY_NAME, "KIND_OF_ARBITRARY_UNIT");
        objects.add(kindOfArbitraryUnit);

        final Scale unitlessScale = new ScaleImpl.Builder(
            new IRI(REF_BASE, "unitless_scale"))
            .domain_M(kindOfArbitraryUnit)
            .member__Of(classOfUnits)
            .build();
        unitlessScale.addStringValue(ENTITY_NAME, "UNITLESS_SCALE");
        objects.add(unitlessScale);
        // End create arbitrary units
        return objects;
    }
                    
}
