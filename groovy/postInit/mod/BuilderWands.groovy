// Stone Wand
crafting.addShaped('betterbuilderswands:wandstone', item('betterbuilderswands:wandstone'), [
    [null, null, ore('cobblestone')],
    [null, ore('stickWood'), null],
    [ore('stickWood'), null, null]
])

// Iron Wand
crafting.addShaped('betterbuilderswands:wandiron', item('betterbuilderswands:wandiron'), [
    [null, null, ore('plateIron')],
    [null, ore('stickIron'), null],
    [ore('stickIron'), null, null]
])

 // Diamond Wand
crafting.addShaped('betterbuilderswands:wanddiamond', item('betterbuilderswands:wanddiamond'), [
[null, ore('string'), ore('plateDiamond')],
[null, ore('stickSteel'), ore('string')],
[ore('stickSteel'), null, null]
])

// Unbreakable Wand
crafting.replaceShaped('betterbuilderswands:wandunbreakable', item('betterbuilderswands:wandunbreakable'), [
[null, metaitem('duct_tape'), ore('blockObsidian')],
[null, ore('stickSteel'), metaitem('duct_tape')],
[ore('stickSteel'), null, null]
])
