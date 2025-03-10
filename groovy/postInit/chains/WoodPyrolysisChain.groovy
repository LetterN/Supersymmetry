import static globals.Globals.*

import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;

def PYROLYSE_OVEN = recipemap('pyrolyse_oven');
def DISTILLATION_TOWER = recipemap('distillation_tower');

//TODO: UNCOMMENT WHEN SUSYCORE 0.0.8 IS OUT
/*
PYROLYSE_OVEN.recipeBuilder()
        .inputs(ore('logWood') * 16)
        .outputs(item('minecraft:coal', 1) * 8)
        .fluidOutputs(fluid('syngas') * 3000)
        .fluidOutputs(fluid('wood_vinegar') * 2000)
        .fluidOutputs(fluid('creosote') * 2000)
        .duration(320)
        .EUt(64)
        .buildAndRegister()

DISTILLATION_TOWER.recipeBuilder()
        .fluidInputs(fluid('creosote') * 1000)
        .outputs(metaitem('bitumen'))
        .fluidOutputs(fluid('phenol') * 100)
        .fluidOutputs(fluid('cresol') * 200)
        .fluidOutputs(fluid('guaiacol') * 250)
        .fluidOutputs(fluid('xylenol') * 50)
        .fluidOutputs(fluid('creosol') * 400)
        .duration(200)
        .EUt(48)
        .buildAndRegister()

DISTILLATION_TOWER.recipeBuilder()
        .fluidInputs(fluid('wood_vinegar') * 1000)
        .fluidOutputs(fluid('water') * 700)
        .fluidOutputs(fluid('acetic_acid') * 100)
        .fluidOutputs(fluid('acetone') * 100)
        .fluidOutputs(fluid('methanol') * 100)
        .duration(200)
        .EUt(24)
        .buildAndRegister()

 */