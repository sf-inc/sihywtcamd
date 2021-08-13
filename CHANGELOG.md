# Versions Changelog

Currently, every **stable** mod version should work on **1.16.x** Minecraft versions.

* **X versions** are major updates with lots of new content. Very rare
* **x.X versions** are either updates that add content or major bug fixes
* **x.x.X versions** are either small content updates (language translation, new textures, ...) or bug fixes

## v1.5.1

### Mobs

* Most hostile mobs are now hostile to villagers: same than previous version but excludes creepers as 
  they can't be targeted by iron golems

## v1.5

### Cave spider

* REALLY fixed crash when using the 1.18 preview datapacks, for the cave spider spawn in negative
  Y positions.

### Mobs

* Most hostile mobs are now hostile to villagers: includes spiders (only at night), skeletons, strays, 
  creepers and witches. Also includes zombies, husks, drowned, vindicators, pillagers... but was 
  already the case

## v1.4.1

### Cave spider

* Fixed the spawn light level: now spawn if light <= to 7
* Fixed crash when using the 1.18 preview datapacks, for the cave spider spawn in negative 
  Y positions. Note that they may not spawn at all with the official datapack, as it override 
  mob spawns for every biomes!
  
### Enderman

* When you get the blindness effect, you are now sure that you aggro the enderman
* You will now get the effect only when you aggro it, after staring at it. So it's now a bit 
  easier to deal with them, as you can stare at them to stop them without getting blindness 
  effect every time. You won't get the effect if you aggro them without staring at them

## v1.4

### Patrols

* Vindicators can now spawn in patrols. They have 10 to 20% chance to spawn instead of a 
  pillager (depending local difficulty). 
* If it didn't spawn a vindicator, it has 5 to 10% chance to spawn a ravager. The ravager 
  may spawn with a pillager riding it or not.

### Cave spiders

* Cave spider can spawn naturally in caves. The deeper they try to spawn, the most 
  possible it is for them to really spawn. So they should be more rare at higher Y 
  positions. They can spawn in group of 1 to 3.
* Cave spider jockey can spawn with a chance of 1/50 (can't spawn from a cave spider 
  spawner!)

### Configs

* Configs are now in different menus to find what you want more quickly. They are
  separated by associated dimension
* You can now set the number of golden armor you need to be safe with piglins
* You can now choose which types of zombie are affected by the zombie buff. You can 
  choose between NONE, ZOMBIE_ONLY, NO_DROWNED, NO_PIGLIN, and ALL

## v1.3.1

### Zombies

* Not immune to explosions anymore
* Not immune to bypassing armor damages: magic (potions), fall damage, ...
* Takes 1 damage from player under conditions, or half damage from mobs under those, true damage if not (includes iron golems)

## v1.3

### Zombie

* Fixed damage so that they can get 0 damage instead of 1 if the damage was initially lesser than 1
* Take true damage from iron golems

## v1.2

* Mod is now fully configurable: you can (de)activate only features you want **(no exception)**

### Magma cube

* Stay vanilla! Changes for slimes are not anymore applied to them

### Ghast

* Reduce their health: now it's 3x their normal health (which is already enough)

## v1.1

* Mod is now fully configurable: you can (de)activate only features you want
* One exception is the spawn mob modification: witches and guardians. Impossible to deactivate for now

### Zombie

* Zombie takes regular damage if attacked while they are on fire

### Husk

* Same than before rewritten so that they look on fire but are immune to on fire damage

### Phantom

* Is repelled by brighter light: above 10 luminance level

### Piglin

* Are hostile to players who don't wear full golden armor

### Mobs

* Mobs that have a flee entity goal don't flee if the entity has less than half health
* So this applies to skeletons, witch, creeper, ...
* This also apply to phantoms even though it's not a flee goal

## v1.0

### Zombie

* Only takes 1 damage from regular attacks: smite needed
* This change also applies to zombie villager, zombified piglin, zombie horse, drowned, husk and giant

#### Husk

* Is no longer affected by Flame and Fire aspect enchantments. Still hurt by fire

#### Drowned

* Spawn more often with a trident, 15% as in Bedrock version (was 6.66%)
* Increased velocity in water

### Skeleton

* Runs away from the player
* This change also applies to stray

#### Stray

* Deal slowness 2 instead of 1

### Enderman

* Player gets blindness (7.5 seconds) when staring at an enderman

### Spider

* Can spawn other weaker spiders on death, their number depends on local difficulty
* 1/10 can spawn weaker spiders. 3-5 on lowest difficulty, 6-8 on highest difficulty
* Weaker spiders are babies that deal half damage and have half health
* This change doesn't apply to cave spider

### Slime

* Can spawn with 1 more size depending on local difficulty
* Can fuse with another same-sized slime on collision if their size is small or normal (1 or 2)

### Guardian

* Can spawn naturally (rarely, like dolphins)

### Phantom

* Phases through blocks (and can see through) but is repelled by light
* Translucent texture

### Wither skeleton

* Can spawn baby (1/5 spawn rate)

### Blaze

* Sets entities on fire on collision

### Ghast

* Has 4 times their health

### Piglin

* Can spawn riding a hoglin, spawn rate depending on local difficulty

### Pillager

* Can (1/4 spawn rate) have a speed bonus depending on local difficulty (up to 69%)
* Increased chance for enchants on his crossbow

### Evoker

* Immune to arrows
* Has 1.5 times their health

### Vindicator

* Can (1/5 spawn rate) have a speed bonus depending on local difficulty (up to 42%)

### Witch

* Flees the player
* Slightly increased spawn chance

### Shulker

* Blindness (5 seconds) when struck by a projectile

### Wither

* Has more health (400 instead of 300, Bedrock has 600)
* Spawns (once) 3-4 wither skeletons when half health (number depends on local difficulty) as in Bedrock