# Versions Changelog

* **X versions** are major updates with lots of new content. Very rare
* **x.X versions** are either updates that add content or major bug fixes
* **x.x.X versions** are either small content updates (language translation, new textures, ...) or bug fixes

## v1.11.1
* Translation files are now sent to client, which avoid missing translation
  when the mod is server-side only for advancements
* Add and update multiple Chinese translation files (thanls to @salmoooon)
* Blazes have a reduced follow range from 48 to 32
* Mobs have a doubled "follow range"
  * The follow range attribute stays the same, but is kinda split
  into a follow range and aggro range
  * The aggro range stays the same, using the follow range value
  * The new "follow range" (when a mob is already aggro) is doubled to avoid fleeing
  too easily
  * This new value is capped to 48
* Zombified Piglins have a 9% chance to spawn as "brute"
  * When they do, they hold a golden axe instead of a golden sword
  * They also aggro players on collision (but don't call others, unless you attack them)
  * Add an advancement for colliding a brute variant
* Magma Cubes turn into Slimes when in water in the Overworld
  * Add an advancement for it
* Slimes turn into Magma Cubes when in lava in the Nether
  * Add an advancement for it
* Slimes can merge only after a short delay to avoid instant fusion
* Add an advancement for deflecting a firework rocket from a pillatrooper
* The advancement for killing an illusioner was updated to require the player to be invisible
* Advancements have now vanilla parents, instead of being in a new tab
* Some advancements have a new frame, some are hidden and some have rewards

## v1.11
* Move features requiring the mod on client to an [extension mod](https://github.com/sf-inc/sihywtcamd_extensions)
  * Baby variants of mobs (spiders and skeletons)
  * Baby skeletons shooting valve of arrows (as there won't be babies anymore)
  * Spiders shooting messy cobwebs
  * Cosmetics changes
  * Associated advancements
* Fix baby zombie towers causing worldgen locks
* Fix spiders condition not checking trial spawner
* Spiders stop shooting if their target is in cobweb
* Increase and make duration configurable for effects of creepers explosions
* Pillagers speed bonus is removed
* Pillagers can spawn as "Pillatrooper"
  * They can fly up in the air with a firework
  * When flying down, they shoot quicker valve of arrows
  * The first arrow shot is guaranteed to be a firework, then it has 50% chance to be an arrow again
  * They have 20% chance to spawn with this ability

## v1.10.5 (backport version)
* Intermediary version for the last backport of new features for 1.20.1
* Include v1.11 changes of the mod (except pillatroopers), without the split of the mod
* Does not include the changes making the mod joinable by vanilla client from v1.10.4,
  skeleton and zombie horsemen changes

## v1.10.4
### Tweaks
* Fix zombie being set to baby too often (introduced in 1.21 port when testing the port)
* Make the mod joinable by vanilla client if some configs are disabled.
  These are the web projectiles and every baby configs.
* Remove the slowness 2 for strays, as there are already frozen arrows to increase difficulty
* Replace the netherite sword with an iron sword for wither skeletons

### Skeleton horsemen
* Replace the baby zombie with the bogged
* Replace the regular skeleton with a wither skeleton holding a bow
* Give them style with armor trims on their armor

### Zombie horsemen
* Replace zombified piglin with a zombie villager with a diamond chestplate
* Give them style with armor trims on their armor

## v1.10.3
* Fix cave spider natural spawn ignoring light level
* Fix zombie tower not always zombie (introduced in 1.21 port when testing the port)

## v1.10.2
* Fix crash on load when not having CC-API on 1.21

## v1.10.1
* Fix server freeze with multiple rider (#52)
* Fix compatibility issue with Mob Variant (#51)
* Fix wither health not updated when summoned with block pattern
* Remove wither skeleton fire resistance feature, which was redundant
  with vanilla since 1.19.4
* Overall refactor to improve mod compatibility, should avoid most crash
  from mod incompatibility

## v1.10

### Important changes
* Skeletons can spawn baby
* Baby skeletons shoot valve of arrows: 3 quick arrows, less charged and precise
* Skeletons from skeleton horse trap are replaced with a Skeleton, a spectral Skeleton, a baby Skeleton, and a Stray
* Strays can spawn in any cold biome, replacing part of the regular Skeletons
* Add zombie horse traps, similar to skeleton horse traps. They spawn a Zombie, a Husk, a Drowned, and a Zombified
  Piglin, all riding Zombie Horses.
* Zombie horses and skeleton horses have increased movement speed when ridden my mobs
* Mobs riding others path find with the accumulated dimension to avoid going through blocks
* Undead mobs ignore other undead mobs attacks
* Undead mobs heal with damage they make (no longer just Zombies)
* Zombies can call reinforcement of the same type, instead of a regular zombie

### Small changes
* Silverfish infesting everywhere really readded this time (I somehow managed to not call the function)
* Make Phantoms way more common in End Cities
* Larvae speed bonus is no longer just random, meaning it has more chance to be greater
* Enderman blindness attack, Slime merging ability, and Spider spawning babies are not mob-specific anymore, meaning
  modded versions of these mobs can have these features
* Fix Slime bigger size applying to Magma Cubes since v1.8
* Refactor Overworld general config into its own category General
* Refactor Spiders changes into general spiders, spider and cave spider

## v1.9.1
* Fix chunk generation crash with other mods (issue #49)

## v1.9
* Port to 1.20.4
* Baby zombies can tower up to 5 baby zombies *(4 on top)*
* Phantoms can spawn in end cities
* A phantom can spawn when an ender crystal is destroyed
* Make enderman blindness attack random, as well as its duration
* Endermen can target nearby players when a player is close to the dragon when sitting
* Ender crystals respawn at ender dragon's health threshold
* Fix shulker bullets not always giving levitation

### Advancements
* Add advancements
* Add root advancement: kill or be killed by someone
* Add spyglass at baby zombie tower advancements
* Add entering messy cobweb advancement
* Add spawning baby spiders advancements
* Add killing illusioner advancement
* Add full golden armor advancement
* Add killing a phantom in the end advancement

## v1.8.2
* Fix client side visual bug that could cause infinite 0s effect on creeper explosion
* Add chinese translation
* Remove mobs debug name from release

## v1.8.1
* Port to 1.20.2

## v1.8

### Important changes
* Better config panel for mobs, with each mob having its own list.  
  Make sure to update your config file again.
* Enderman gives blindness on attack, instead of staring
* Shulker projectiles has 33% chance to cancel levitation, instead of giving blindness
* Endermites teleport target on attack, similarly to chorus fruits
* Cave spiders has 10% chance to spawn 1-3 to 3-5 babies on death.  
  Spiders have an increased chance to spawn babies of 15%, but spawn one less baby than before, from 2-4 to 5-7.
* Baby spiders give poison for less time than adults. The duration of the poison is reduced by 5, falling from 7s/15s
  (normal/hard) to 1.4s/3s
* Readd infested everywhere option. Now use the same feature as in mountains exactly to avoid datapacks generation.
* Fix mob spawn changes not working anymore from 1.19 versions
* Zombies have attributes variations and updated base values
  * Zombies knockback resistance is updated and randomized. It can be
  greatly increased if it spawns as an "unstoppable" zombie.
  * Zombies spawn reinforcement is increased quite a lot and randomized. It
  can be greatly increased if it spawns as a "caller" zombie.
  * "tank" and "runner" zombies variants are also added, with increased
  health and speed respectively.
  * These zombie variants can't be applied to baby zombies.
  * Brainless, leader and siege are replaced by these new settings.

### Small changes
* Wither skeletons can drop their netherite sword, as rarely as heads
* Frozen arrows freeze entities on damage only. Blocking now protects you from freezing
* Update cave spider natural spawn condition. The deeper, the more common, starting
  y=64 and below
* Baby spiders don't drop loop anymore. They still drop experience though
* Tweak big slime chance to spawn, so that it can spawn even with lower difficulty
* Creeper can die from charged creeper explosion, to be able to drop their head again
* Baby spiders have now an adapted attack reach

### Technical changes
* Adds Cardinal Component API as an internal dependency
* Wither entity use CC API to store if it reached half health
* Arrow entity use CC API to store if it is frozen

## v1.7.5
* Update to 1.20.1
* Fix guardian jockey condition (babies would always ride guardians on 1.19.4)
* Fix riding on spawn when generating chunk (issue #38)
  * Compatibility with structures mod like "Repurposed Structures" or
  "Unstructured" is back

## v1.7.4
* Update to 1.19.4
* Removed drowned better animation option, as it has been
  fixed on this version

## v1.7.3
* Update to 1.19.3
* Port implies removing a feature: silverfish everywhere.
  This should be put in datapacks instead. As I don't know
  if I can load it with a config, this might be a permanent
  removal.

### Cosmetic
* Adds more translucent textures, like for phantoms
  * translucent allays
  * translucent ghasts
  * translucent vexes

### Wither
* Make the weather stormy until they die
* Summon an explosion when half health, as when it spawns

## v1.7.2
* Fix cobwebs replacing blocks (issue #34)
* Increase guardian natural spawn: now also spawn in warm ocean (but rarer)
* Guardian beam sound is played only when attacking players to avoid sound spam

## v1.7.1
* Fix incompatibility issue (with OnSoulFire mod, but surely others)

## v1.7

### Configs
* Complete refacto of categories, and subcategories
* A mob can be found in its most significant category only (ex: endermite in end, instead of arthropod)

### Arthropod

* Don't get fall damage
* Larvae (silverfish and endermite) spawn with a bonus speed multiplying their speed up to 1.5 depending on local 
  difficulty
* Silverfishes are infesting the whole overworld (more than in mountains), and so mountains are even more infested than 
  before

### End
* Chorus plants are infested with endermites

### Nether
* Magma cubes set on fire on collision, fire duration depends on size. No fire if blocking
* Blazes don't set on fire if their fire is not active and if their target is blocking

### Overworld
* Vexes spawn naturally in dark forest
* Vexes die if their summoner dies

### Skeleton
* Have 5% chance to spawn with spectral arrows only
* Know a bit how to swim
* Strays shoot freezing arrows
* Wither skeletons are fire resistant (but not immune to lava, same than husks buffed a bit)
* Wither skeletons can spawn holding a bow if adult (25% chance), so they will shoot flame arrows
* Wither skeletons carry a netherite sword instead of a stone one

### Zombies
* Baby drowned can spawn riding a guardian (10 to 20% chance)
* Better swimming animation
* Old zombie changes replaced (no more zombie buff):
  * Steal life on attack
  * Have a natural knockback resistance
  * Leader zombies are split into leader and siege ones, with different bonuses
  * Can spawn as a brainless zombie, that won't attack unless you attack them
    * Leader: 5-10% spawn chance, increased health and spawn reinforcement 
    * Siege: 10-15% spawn chance, can break doors
    * Brainless: 25-20% spawn chance, no target goal
    * Normal ones: 60-55% spawn chance

## v1.6

### Creeper

* Don't get damage from explosions. Instead, they ignite
* Its explosion deals mining fatigue and weakness effects, duration depending on your distance with the creeper

### Illusioner

* Have 40% chance to spawn in mansions when an evoker does
* If it spawns, it has 80% chance to replace the associated evoker and 20% to spawn with it

### Phantom

* Fixed texture bug that removed red flash on hit

### Slime

* Can merge only once to avoid easy farm and lags
* Merging plays a sound and creates particles

### Spider

* Can spit webs at targets that spawn a messy cobweb on collision with an entity
* Baby spiders can't spit webs
* Messy cobweb can't be placed on water, so you're immune on water
* Messy cobweb despawns after 5 to 10s and don't drop strings

## v1.5.2

### Configs

* Configs have been reorganized for more clarity: created sub-categories
* Zombie buff is more configurable: you can choose the percentage of damage dealt from players and from mobs 
  (separately) to zombies under the specified conditions

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