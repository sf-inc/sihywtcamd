# Versions Changelog

Currently, every **stable** mod version should work on **1.16.x** Minecraft versions.

* **X versions** are major updates with lots of new content. Very rare
* **x.X versions** are either updates that add content or major bug fixes
* **x.x.X versions** are either small content updates (language translation, new textures, ...) or bug fixes

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