import static globals.Globals.*
import static globals.SinteringGlobals.*

import static gregtech.api.unification.material.Materials.*;
import gregtech.api.unification.material.MarkerMaterials;
import static gregtech.api.unification.ore.OrePrefix.dye;

MIXER = recipemap('mixer')
EMSEPARATOR = recipemap('electromagnetic_separator')
CENTRIFUGE = recipemap('centrifuge')
DT = recipemap('distillation_tower')
TBR = recipemap('trickle_bed_reactor')
ROASTER = recipemap('roaster')
//VACUUM_DT = recipemap('vacuum_distillation')
CRYSTALLIZER = recipemap('crystallizer')
EXTRACTOR = recipemap('extractor')
SINTERING = recipemap('sintering_oven')
//COKING = recipemap('coking_tower')
CSTR = recipemap('continuous_stirred_tank_reactor')
CRACKER = recipemap('cracker')
EBF = recipemap('electric_blast_furnace')
//REFORMER = recipemap('catalytic_reformer_recipes')

// TODO: Fluid drills

class Oil {
    String name

    Oil(String name) {
        this.name = name
    }
    
    def getDiluted(int amount) {
        return fluid('diluted_' + this.name) * amount
    }

    def getDesalted(int amount) {
        return fluid('desalted_' + this.name) * amount
    }

    def get(int amount) {
        return fluid(this.name) * amount
    }
}

class OilFraction {
    String name
    Boolean isUpgradable = false
    String upgrade_name = ""

    OilFraction(String name) {
        this.name = name
    }

    OilFraction(String name, String upgrade_name) {
        this.name = name
        this.isUpgradable = true
        this.upgrade_name = upgrade_name
    }

    def getTreatedSulfuric(int amount) {
        return fluid('treated_sulfuric_' + this.name) * amount
    }

    def getSulfuric(int amount) {
        return fluid('sulfuric_' + this.name) * amount
    }

    def getUpgraded(int amount) {
        return fluid(this.upgrade_name) * amount
    }

    def getUpgradedMix(int amount) {
        return fluid('upgraded_' + this.name + '_mix') * amount
    }

    def get(int amount) {
        return fluid(this.name) * amount
    }
}

class OilFractionCrackable extends OilFraction {

    OilFractionCrackable(String name){
        super(name)
    }

    OilFractionCrackable(String name, String upgrade_name){
        super(name, upgrade_name)
    }

    def getLightlyHydro(int amount) {
        return fluid('lightly_hydrocracked_' + this.name) * amount
    }

    def getSeverelyHydro(int amount) {
        return fluid('severely_hydrocracked_' + this.name) * amount
    }

    def getLightlySteam(int amount) {
        return fluid('lightly_steamcracked_' + this.name) * amount
    }

    def getSeverelySteam(int amount) {
        return fluid('severely_steamcracked_' + this.name) * amount
    }

    def getLightlyHydroMix(int amount) {
    return fluid('lightly_hydrocracked_' + this.name + '_mix') * amount
    }

    def getSeverelyHydroMix(int amount) {
        return fluid('severely_hydrocracked_' + this.name + '_mix') * amount
    }

    def getLightlySteamMix(int amount) {
        return fluid('lightly_steamcracked_' + this.name + '_mix') * amount
    }

    def getSeverelySteamMix(int amount) {
        return fluid('severely_steamcracked_' + this.name + '_mix') * amount
    }

}

def fractions = [
    fuel_oil : new OilFraction('fuel_oil', 'diesel'),
    diesel : new OilFraction('diesel', 'kerosene'),
    kerosene : new OilFractionCrackable('kerosene', 'naphtha'),
    naphtha : new OilFractionCrackable('naphtha', 'gasoline'),
    gasoline : new OilFractionCrackable('gasoline'),
    refinery_gas : new OilFraction('refinery_gas'),
    natural_gas : new OilFraction('natural_gas')
]

def oils = [
    oil: new Oil('oil'),
    oil_light: new Oil('oil_light'),
    oil_heavy: new Oil('oil_heavy') 
]



oils.each { _, oil -> {
        MIXER.recipeBuilder()
        .fluidInputs(fluid('water') * 100)
        .fluidInputs(oil.get(1000))
        .fluidOutputs(oil.getDiluted(1000))
        .duration(200)
        .EUt(30)
        .buildAndRegister()
        // TODO: Uncomment when susycore 0.0.8
        // EMSEPARATOR.recipeBuilder()
        // .fluidInputs(oil.getDiluted(1000))
        // .fluidOutputs(fluid('oily_brine') * 100)
        // .fluidOutputs(oil.getDesalted(1000))
        // .duration(160)
        // .EUt(30)
        // .buildAndRegister() 
    }
}

DT.recipeBuilder()
.fluidInputs(oils.oil.getDesalted(1000))
.fluidOutputs(fluid('sulfuric_oil_residue') * 150)
.fluidOutputs(fractions.fuel_oil.getSulfuric(150))
.fluidOutputs(fractions.diesel.getSulfuric(200))
.fluidOutputs(fractions.kerosene.getSulfuric(100))
.fluidOutputs(fractions.naphtha.getSulfuric(200))
.fluidOutputs(fractions.gasoline.getSulfuric(100))
.fluidOutputs(fractions.refinery_gas.getSulfuric(500))
.duration(600)
.EUt(30)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(oils.oil_light.getDesalted(1000))
.fluidOutputs(fluid('sulfuric_oil_residue') * 50)
.fluidOutputs(fractions.fuel_oil.getSulfuric(100))
.fluidOutputs(fractions.diesel.getSulfuric(150))
.fluidOutputs(fractions.kerosene.getSulfuric(100))
.fluidOutputs(fractions.naphtha.getSulfuric(250))
.fluidOutputs(fractions.gasoline.getSulfuric(150))
.fluidOutputs(fractions.refinery_gas.getSulfuric(1000))
.duration(600)
.EUt(30)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(oils.oil_heavy.getDesalted(1000))
.fluidOutputs(fluid('sulfuric_oil_residue') * 300)
.fluidOutputs(fractions.fuel_oil.getSulfuric(200))
.fluidOutputs(fractions.diesel.getSulfuric(150))
.fluidOutputs(fractions.kerosene.getSulfuric(100))
.fluidOutputs(fractions.naphtha.getSulfuric(100))
.fluidOutputs(fractions.gasoline.getSulfuric(50))
.fluidOutputs(fractions.refinery_gas.getSulfuric(250))
.duration(600)
.EUt(30)
.buildAndRegister()

// Oily Brine Processing

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('oily_brine') * 100)
.fluidOutputs(fluid('brine') * 100)
.fluidOutputs(fluid('desalted_oil') * 50)
.duration(260)
.EUt(30)
.buildAndRegister()

fractions.each { _, fraction -> {
        FBR.recipeBuilder()
        .fluidInputs(fraction.getSulfuric(180))
        .fluidInputs(fluid('hot_hp_hydrogen') * 45)
        //.notConsumable(ore('dustAlumina'))
        .fluidOutputs(fraction.getTreatedSulfuric(180))
        .duration(240)
        .EUt(30)
        .buildAndRegister()

        DT.recipeBuilder()
        .fluidInputs(fraction.getTreatedSulfuric(1000))
        .fluidOutputs(fraction.get(1000))
        .fluidOutputs(fluid('sour_gas') * 250)
        .duration(400)
        .EUt(30)
        .buildAndRegister()
    }
}

// Sour gas Processing

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('sour_gas') * 3000)
.fluidInputs(fluid('ethanolamine_mix') * 1000)
.fluidOutputs(fluid('hydrogen') * 1000)
.fluidOutputs(fluid('rich_amine') * 1000)
.duration(120)
.EUt(120)
.buildAndRegister()

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('rich_amine') * 1000)
.fluidOutputs(fluid('hydrogen_sulfide') * 1000)
.fluidOutputs(fluid('ethanolamine_mix') * 1000)
.duration(160)
.EUt(120)
.buildAndRegister()

ROASTER.recipeBuilder()
.fluidInputs(fluid('hydrogen_sulfide') * 3000)
.fluidInputs(fluid('oxygen') * 3000)
.fluidOutputs(fluid('uncatalyzed_sulfurous_gases') * 3000)
.outputs(ore('dustSulfur').first())
.duration(300)
.EUt(30)
.buildAndRegister()

ROASTER.recipeBuilder()
.fluidInputs(fluid('hydrogen_sulfide') * 3000)
.fluidInputs(fluid('air') * 9000)
.fluidOutputs(fluid('uncatalyzed_sulfurous_gases') * 3000)
.outputs(ore('dustSulfur').first())
.duration(300)
.EUt(30)
.buildAndRegister()
//TODO: fbr doesnt accept item output
// FBR.recipeBuilder()
// .fluidInputs(fluid('uncatalyzed_sulfurous_gases') * 3000)
// .fluidOutputs(fluid('steam') * 2000)
// .outputs(ore('dustSulfur').first() * 2)
// .duration(300)
// .EUt(30)
// .buildAndRegister()

// Natural Gas Processing

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('crude_natural_gas') * 1000)
.fluidOutputs(fluid('oil') * 50)
.fluidOutputs(fluid('sulfuric_natural_gas') * 1000)
.duration(400)
.EUt(30)
.buildAndRegister()

// Sulfuric Oil Residue Processing
//TODO: uncomment when susycore 0.0.8
// VACUUM_DT.recipeBuilder()
// .fluidInputs(fluid('sulfuric_oil_residue') * 1000)
// .fluidOutputs(fractions.fuel_oil.getSulfuric(200))
// .fluidOutputs(fractions.diesel.getSulfuric(200))
// .fluidOutputs(fractions.kerosene.getSulfuric(150))
// .fluidOutputs(fractions.naphtha.getSulfuric(100))
// .fluidOutputs(fluid('lubricating_oil') * 500)
// .fluidOutputs(fluid('slack_wax') * 350)
// .outputs(metaitem('bituminous_residue'))

// Lubricating Oil processing

MIXER.recipeBuilder()
.fluidInputs(fluid('lubricating_oil') * 250)
.inputs(ore('dustRedstone'))
.fluidOutputs(fluid('lubricant') * 500)
.duration(80)
.EUt(7)
.buildAndRegister()

MIXER.recipeBuilder()
.fluidInputs(fluid('lubricating_oil') * 250)
.inputs(ore('dustTalc'))
.fluidOutputs(fluid('lubricant') * 500)
.duration(80)
.EUt(7)
.buildAndRegister()

MIXER.recipeBuilder()
.fluidInputs(fluid('lubricating_oil') * 250)
.inputs(ore('dustSoapstone'))
.fluidOutputs(fluid('lubricant') * 500)
.duration(80)
.EUt(7)
.buildAndRegister()

// Slack wax Processing

CRYSTALLIZER.recipeBuilder()
.fluidInputs(fluid('slack_wax') * 1000)
.fluidOutputs(fluid('desalted_oil') * 150)
.outputs(metaitem('paraffin_wax') * 2)
.duration(600)
.EUt(30)
.buildAndRegister()

crafting.addShaped("treated_wood_planks_paraffin", item('gregtech:planks', 1) * 8, [
    [ore('plankWood'),ore('plankWood'),ore('plankWood')],
    [ore('plankWood'),metaitem('paraffin_wax'),ore('plankWood')],
    [ore('plankWood'),ore('plankWood'),ore('plankWood')]
])

EXTRACTOR.recipeBuilder()
.inputs(metaitem('paraffin_wax'))
.notConsumable(circuit(0))
.fluidOutputs(fluid('lubricating_oil') * 250)
.duration(200)
.EUt(30)
.buildAndRegister()

EXTRACTOR.recipeBuilder()
.inputs(metaitem('paraffin_wax'))
.notConsumable(circuit(0))
.fluidOutputs(fluid('lubricating_oil') * 1000)
.duration(200)
.EUt(30)
.buildAndRegister()

EXTRACTOR.recipeBuilder()
.inputs(metaitem('paraffin_wax'))
.notConsumable(circuit(1))
.fluidOutputs(fluid('resin') * 1000)
.duration(200)
.EUt(30)
.buildAndRegister()

// Bituminous Residue Processing

// Bituminous Residue -> Asphalt

for (fuel in sintering_fuels) {

    if (fuel.isPlasma) {

        SINTERING.recipeBuilder()
        .inputs(metaitem('bituminous_residue'))
        .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
        .outputs(metaitem('bitumen'))
        .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
        .duration(fuel.duration)
        .EUt(Globals.voltAmps[3])
        .buildAndRegister()

    } else {

        for (comburent in sintering_comburents) {

            SINTERING.recipeBuilder()
            .inputs(metaitem('bituminous_residue'))
            .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
            .fluidInputs(fluid(comburent.name) * comburent.amountRequired)
            .outputs(metaitem('bitumen'))
            .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
            .duration(fuel.duration + comburent.duration)
            .EUt(Globals.voltAmps[0])
            .buildAndRegister()

        }
    }

}

MIXER.recipeBuilder()
.fluidInputs(fluid('propane') * 1000)
.inputs(metaitem('bitumen'))
.fluidOutputs(fluid('dissolved_bitumen') * 1000)
.duration(200)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('dissolved_bitumen') * 1000)
.fluidOutputs(fluid('oil') * 100)
.outputs(ore('dustAsphalt').first())
.duration(300)
.EUt(Globals.voltAmps[0])
.buildAndRegister()

// Bituminous Residue -> Coke

// COKING.recipeBuilder()
// .fluidInputs(fluid('steam') * 1000)
// .fluidInputs(fluid('high_pressure_water') * 1000)
// .fluidOutputs(fluid('desalted_oil') * 150)
// .outputs(ore('dustGreenCoke').first())
// .duration(600)
// .EUt(Globals.voltAmps[1] * 2)
// .buildAndRegister()

for (fuel in sintering_fuels) {

    if (fuel.isPlasma) {

        SINTERING.recipeBuilder()
        .inputs(ore('dustGreenCoke'))
        .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
        .outputs(ore('dustCoke').first())
        .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
        .duration(fuel.duration)
        .EUt(Globals.voltAmps[3])
        .buildAndRegister()

    } else {

        for (comburent in sintering_comburents) {

            SINTERING.recipeBuilder()
            .inputs(ore('dustGreenCoke'))
            .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
            .fluidInputs(fluid(comburent.name) * comburent.amountRequired)
            .outputs(ore('dustCoke').first())
            .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
            .duration(fuel.duration + comburent.duration)
            .EUt(Globals.voltAmps[0])
            .buildAndRegister()

        }
    }

}

// Cracking: Ethane, Propane Butane

ROASTER.recipeBuilder()
.fluidInputs(fluid('ethane') * 1000)
.fluidInputs(fluid('steam') * 1000)
.fluidOutputs(fluid('steamcracked_ethane') * 1000)
.duration(300)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

ROASTER.recipeBuilder()
.fluidInputs(fluid('propane') * 1000)
.fluidInputs(fluid('steam') * 1000)
.fluidOutputs(fluid('steamcracked_propane') * 1000)
.duration(300)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

ROASTER.recipeBuilder()
.fluidInputs(fluid('butane') * 1000)
.fluidInputs(fluid('steam') * 1000)
.fluidOutputs(fluid('steamcracked_butane') * 1000)
.duration(300)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('steamcracked_ethane') * 1000)
.fluidOutputs(fluid('butadiene') * 50)
.fluidOutputs(fluid('propene') * 50)
.fluidOutputs(fluid('ethylene') * 800)
.fluidOutputs(fluid('methane') * 50)
.fluidOutputs(fluid('hydrogen') * 50)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('steamcracked_propane') * 1000)
.fluidOutputs(fluid('butadiene') * 50)
.fluidOutputs(fluid('propene') * 150)
.fluidOutputs(fluid('ethylene') * 500)
.fluidOutputs(fluid('methane') * 150)
.fluidOutputs(fluid('hydrogen') * 150)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('steamcracked_butane') * 1000)
.fluidOutputs(fluid('butadiene') * 150)
.fluidOutputs(fluid('propene') * 250)
.fluidOutputs(fluid('ethylene') * 350)
.fluidOutputs(fluid('methane') * 150)
.fluidOutputs(fluid('hydrogen') * 100)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Natural / Refinery -> Gasoline

CSTR.recipeBuilder()
.fluidInputs(fluid('sulfuric_acid') * 250)
.fluidInputs(fluid('natural_gas') * 50)
.fluidOutputs(fluid('alkylated_natural_gas') * 300)
.duration(5)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

CSTR.recipeBuilder()
.fluidInputs(fluid('sulfuric_acid') * 250)
.fluidInputs(fluid('refinery_gas') * 50)
.fluidOutputs(fluid('alkylated_refinery_gas') * 300)
.duration(5)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('alkylated_natural_gas') * 3000)
.fluidOutputs(fluid('sulfuric_acid') * 2500)
.fluidOutputs(fluid('gasoline') * 500)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('alkylated_refinery_gas') * 3000)
.fluidOutputs(fluid('sulfuric_acid') * 2500)
.fluidOutputs(fluid('gasoline') * 500)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Natural Gas Distillation

DT.recipeBuilder()
.fluidInputs(fluid('natural_gas') * 1000)
.fluidOutputs(fluid('butane') * 100)
.fluidOutputs(fluid('propane') * 100)
.fluidOutputs(fluid('ethane') * 100)
.fluidOutputs(fluid('methane') * 750)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('liquid_natural_gas') * 1000)
.fluidOutputs(fluid('butane') * 100)
.fluidOutputs(fluid('propane') * 100)
.fluidOutputs(fluid('ethane') * 100)
.fluidOutputs(fluid('methane') * 750)
.fluidOutputs(fluid('helium') * 20)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Refinery Gas Distillation

DT.recipeBuilder()
.fluidInputs(fluid('refinery_gas') * 1000)
.fluidOutputs(fluid('butane') * 100)
.fluidOutputs(fluid('propane') * 100)
.fluidOutputs(fluid('ethane') * 100)
.fluidOutputs(fluid('methane') * 750)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Kerosene, naphtha and gasoline cracking

//TODO: Cracking Catalyst

// Cracking

fractions.each { _, fraction -> {

        if (fraction instanceof OilFractionCrackable) {

            CRACKER.recipeBuilder()
            .fluidInputs(fraction.get(1000))
            .fluidInputs(fluid('hot_hp_hydrogen') * 1000)
            .notConsumable(circuit(0))
            //.inputs(ore('dustCrackingCatalyst'))
            .fluidOutputs(fraction.getLightlyHydroMix(1000))
            .duration(260)
            .EUt(Globals.voltAmps[1] * 2)
            .buildAndRegister()
            
            CRACKER.recipeBuilder()
            .fluidInputs(fraction.get(1000))
            .fluidInputs(fluid('hot_hp_hydrogen') * 1000)
            .notConsumable(circuit(1))
            //.inputs(ore('dustCrackingCatalyst'))
            .fluidOutputs(fraction.getSeverelyHydroMix(1000))
            .duration(260)
            .EUt(Globals.voltAmps[1] * 2)
            .buildAndRegister()
            
            CRACKER.recipeBuilder()
            .fluidInputs(fraction.get(1000))
            .fluidInputs(fluid('steam') * 1000)
            .notConsumable(circuit(0))
            //.inputs(ore('dustCrackingCatalyst'))
            .fluidOutputs(fraction.getLightlySteamMix(1000))
            .duration(260)
            .EUt(Globals.voltAmps[1] * 2)
            .buildAndRegister()
            
            CRACKER.recipeBuilder()
            .fluidInputs(fraction.get(1000))
            .fluidInputs(fluid('steam') * 1000)
            .notConsumable(circuit(1))
            //.inputs(ore('dustCrackingCatalyst'))
            .fluidOutputs(fraction.getSeverelySteamMix(1000))
            .duration(260)
            .EUt(Globals.voltAmps[1] * 2)
            .buildAndRegister()

            CENTRIFUGE.recipeBuilder()
            .fluidInputs(fraction.getLightlyHydroMix(1000))
            .fluidOutputs(fraction.getLightlyHydro(1000))
            .outputs(metaitem('spent_cracking_catalyst'))
            .duration(160)
            .EUt(Globals.voltAmps[1])
            .buildAndRegister()
            
            CENTRIFUGE.recipeBuilder()
            .fluidInputs(fraction.getSeverelyHydroMix(1000))
            .fluidOutputs(fraction.getSeverelyHydro(1000))
            .outputs(metaitem('spent_cracking_catalyst'))
            .duration(160)
            .EUt(Globals.voltAmps[1])
            .buildAndRegister()
            
            CENTRIFUGE.recipeBuilder()
            .fluidInputs(fraction.getLightlySteamMix(1000))
            .fluidOutputs(fraction.getLightlySteam(1000))
            .outputs(metaitem('spent_cracking_catalyst'))
            .duration(160)
            .EUt(Globals.voltAmps[1])
            .buildAndRegister()
            
            CENTRIFUGE.recipeBuilder()
            .fluidInputs(fraction.getSeverelySteamMix(1000))
            .fluidOutputs(fraction.getSeverelySteam(1000))
            .outputs(metaitem('spent_cracking_catalyst'))
            .duration(160)
            .EUt(Globals.voltAmps[1])
            .buildAndRegister()
            
        }

    }

}

EBF.recipeBuilder()
.fluidInputs(fluid('oxygen') * 1000)
.inputs(metaitem('spent_cracking_catalyst'))
.fluidOutputs(fluid('flue_gas') * 1000)
//.outputs(ore('dustCrackingCatalyst').first())
.duration(200)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Cracked Distillation

// Kerosene

DT.recipeBuilder()
.fluidInputs(fractions.kerosene.getLightlyHydro(1000))
.fluidOutputs(fluid('gasoline') * 600)
.fluidOutputs(fluid('naphtha') * 100)
.fluidOutputs(fluid('butane') * 100)
.fluidOutputs(fluid('propane') * 100)
.fluidOutputs(fluid('ethane') * 75)
.fluidOutputs(fluid('methane') * 75)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.kerosene.getSeverelyHydro(1000))
.fluidOutputs(fluid('gasoline') * 200)
.fluidOutputs(fluid('naphtha') * 250)
.fluidOutputs(fluid('butane') * 300)
.fluidOutputs(fluid('propane') * 300)
.fluidOutputs(fluid('ethane') * 175)
.fluidOutputs(fluid('methane') * 175)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.kerosene.getLightlySteam(1000))
.fluidOutputs(fluid('gasoline') * 300)
.fluidOutputs(fluid('naphtha') * 50)
.fluidOutputs(fluid('toluene') * 25)
.fluidOutputs(fluid('benzene') * 125)
.fluidOutputs(fluid('butene') * 25)
.fluidOutputs(fluid('butadiene') * 15)
.fluidOutputs(fluid('propane') * 3)
.fluidOutputs(fluid('propene') * 30)
.fluidOutputs(fluid('ethane') * 5)
.fluidOutputs(fluid('ethylene') * 50)
.fluidOutputs(fluid('methane') * 50)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.kerosene.getSeverelySteam(1000))
.fluidOutputs(fluid('gasoline') * 100)
.fluidOutputs(fluid('naphtha') * 125)
.fluidOutputs(fluid('toluene') * 80)
.fluidOutputs(fluid('benzene') * 125)
.fluidOutputs(fluid('butene') * 80)
.fluidOutputs(fluid('butadiene') * 50)
.fluidOutputs(fluid('propane') * 10)
.fluidOutputs(fluid('propene') * 100)
.fluidOutputs(fluid('ethane') * 15)
.fluidOutputs(fluid('ethylene') * 150)
.fluidOutputs(fluid('methane') * 150)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Naphtha :concernium:

DT.recipeBuilder()
.fluidInputs(fractions.naphtha.getLightlyHydro(1000))
.fluidOutputs(fluid('butane') * 800)
.fluidOutputs(fluid('propane') * 300)
.fluidOutputs(fluid('ethane') * 250)
.fluidOutputs(fluid('methane') * 250)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.naphtha.getSeverelyHydro(1000))
.fluidOutputs(fluid('butane') * 125)
.fluidOutputs(fluid('propane') * 125)
.fluidOutputs(fluid('ethane') * 1500)
.fluidOutputs(fluid('methane') * 1500)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.naphtha.getLightlySteam(1000))
.fluidOutputs(fluid('kerosene') * 75)
.fluidOutputs(fluid('gasoline') * 150)
.fluidOutputs(fluid('toluene') * 40)
.fluidOutputs(fluid('benzene') * 150)
.fluidOutputs(fluid('butene') * 80)
.fluidOutputs(fluid('butadiene') * 150)
.fluidOutputs(fluid('propane') * 15)
.fluidOutputs(fluid('propene') * 200)
.fluidOutputs(fluid('ethane') * 35)
.fluidOutputs(fluid('ethylene') * 200)
.fluidOutputs(fluid('methane') * 200)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.naphtha.getSeverelySteam(1000))
.fluidOutputs(fluid('kerosene') * 25)
.fluidOutputs(fluid('gasoline') * 50)
.fluidOutputs(fluid('toluene') * 20)
.fluidOutputs(fluid('benzene') * 100)
.fluidOutputs(fluid('butene') * 50)
.fluidOutputs(fluid('butadiene') * 50)
.fluidOutputs(fluid('propane') * 15)
.fluidOutputs(fluid('propene') * 300)
.fluidOutputs(fluid('ethane') * 65)
.fluidOutputs(fluid('ethylene') * 500)
.fluidOutputs(fluid('methane') * 500)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

// Gasoline 

DT.recipeBuilder()
.fluidInputs(fractions.gasoline.getLightlyHydro(1000))
.fluidOutputs(fluid('naphtha') * 800)
.fluidOutputs(fluid('butane') * 150)
.fluidOutputs(fluid('propane') * 200)
.fluidOutputs(fluid('ethane') * 125)
.fluidOutputs(fluid('methane') * 125)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.gasoline.getSeverelyHydro(1000))
.fluidOutputs(fluid('naphtha') * 200)
.fluidOutputs(fluid('butane') * 125)
.fluidOutputs(fluid('propane') * 125)
.fluidOutputs(fluid('ethane') * 1500)
.fluidOutputs(fluid('methane') * 1500)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.gasoline.getLightlySteam(1000))
.fluidOutputs(fluid('kerosene') * 150)
.fluidOutputs(fluid('naphtha') * 400)
.fluidOutputs(fluid('toluene') * 40)
.fluidOutputs(fluid('benzene') * 200)
.fluidOutputs(fluid('butene') * 75)
.fluidOutputs(fluid('butadiene') * 60)
.fluidOutputs(fluid('propane') * 20)
.fluidOutputs(fluid('propene') * 150)
.fluidOutputs(fluid('ethane') * 10)
.fluidOutputs(fluid('ethylene') * 50)
.fluidOutputs(fluid('methane') * 50)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fractions.gasoline.getSeverelySteam(1000))
.fluidOutputs(fluid('kerosene') * 50)
.fluidOutputs(fluid('naphtha') * 100)
.fluidOutputs(fluid('toluene') * 30)
.fluidOutputs(fluid('benzene') * 150)
.fluidOutputs(fluid('butene') * 65)
.fluidOutputs(fluid('butadiene') * 50)
.fluidOutputs(fluid('propane') * 50)
.fluidOutputs(fluid('propene') * 250)
.fluidOutputs(fluid('ethane') * 50)
.fluidOutputs(fluid('ethylene') * 250)
.fluidOutputs(fluid('methane') * 250)
.duration(600)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

fractions.each { _, fraction -> {

        if (fraction.isUpgradable) {

            CRACKER.recipeBuilder()
            .fluidInputs(fraction.get(1000))
            .notConsumable(circuit(2))
            //.inputs(ore('dustCrackingCatalyst'))
            .fluidOutputs(fraction.getUpgradedMix(1000))
            .duration(260)
            .EUt(Globals.voltAmps[1] * 2)
            .buildAndRegister()

            CENTRIFUGE.recipeBuilder()
            .fluidInputs(fraction.getUpgradedMix(1000))
            .fluidOutputs(fraction.getUpgraded(1000))
            .outputs(metaitem('spent_cracking_catalyst'))
            .duration(160)
            .EUt(Globals.voltAmps[1])
            .buildAndRegister()

        }

    }

}

// BTEX
//TODO: Susycore 0.0.8
// REFORMER.recipeBuilder()
// .fluidInputs(fluid('naphtha') * 1000)
// .fluidInputs(fluid('hot_hp_hydrogen') * 1000)
// .notConsumable(ore('catalystBedPlatinum'))
// .fluidOutputs(fluid('naphtha_reformate') * 1000)
// .duration(300)
// .EUt(Globals.voltAmps[1] * 2)
// .buildAndRegister()
//TODO: gaming look here please
CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('naphtha_reformate') * 1000)
.fluidInputs(fluid('furfural') * 50)
//.fluidOutputs(fluid('naphtha') * 500) Do we really want naphtha back? You could convert all your naphtha into btex, while in reality
.fluidOutputs(fluid('btex_extract') * 500)
.duration(200)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

CENTRIFUGE.recipeBuilder()
.fluidInputs(fluid('btex_extract') * 1000)
.fluidInputs(fluid('steam') * 1000)
.fluidOutputs(fluid('furfural') * 50)
.fluidOutputs(fluid('btex') * 1000)
.duration(200)
.EUt(Globals.voltAmps[1])
.buildAndRegister()

DT.recipeBuilder()
.fluidInputs(fluid('btex') * 1000)
.fluidOutputs(fluid('xylene') * 400)
.fluidOutputs(fluid('benzene') * 150)
.fluidOutputs(fluid('toluene') * 400)
.fluidOutputs(fluid('ethylbenzene') * 50)
.duration(260)
.EUt(Globals.voltAmps[1] * 2)
.buildAndRegister()

