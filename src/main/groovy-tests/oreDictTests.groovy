// Note that just like groovy's default, removing Ore Dictionaries that apply to all metas of a item
// is quite buggy.

// Get all Ore Dicts of a Stack (Pink Wool)
println(item('minecraft:wool', 6).getAllOreDicts())

// Remove all Ore Dicts of a Stack (Lapis Block)
item('minecraft:lapis_block').removeAllOreDicts()

// Remove all Ore Dicts of a Stack (Gray Wool)
// The 'wool' oreDict is only sometimes removed, because it applies to all wool items.
item('minecraft:wool', 7).removeAllOreDicts()