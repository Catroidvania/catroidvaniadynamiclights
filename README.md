# catroidvaniadynamiclights

a basic client side dynamic lights mod for reindev

requires [foxloader](https://github.com/Fox2Code/FoxLoader)

loosely based on [farns bta dynamic light mod](https://github.com/FarnGitHub/BTA-Dynamic-Light-Mod)

---

handheld items emit lights

![a glowing minecraft player holding a torch](images/helditems.png)

dropped item emit lights
![a lava bucket dropped on the ground](images/droppeditem.png)
![several dropped items on the ground](images/droppeditems.png)

flaming mobs emit lights
![a spider on fire](images/flamingmob.png)

lit tnt and exploding creepers glow
![a lit piece of minecraft tnt](images/littnt.png)
![a picture of a minecraft creeper](images/litcreeper.png)

---

most blocks that give off light also give off light as dropped and held items
some entities such as fireballs and blazes also emit light

## config options

- dropped item lights: enable item entities emitting light
- entity lights: enable lights for:
  - lit tnt, exploding creepers, thrown sticky torches, molotovs, dynamite, fireballs, and lightning
- handheld lights: enable lighting from the currently held player item
- on fire lights: enable lights for entities lit on fire, requires entity lights to be enabled
- always lit underwater: when false, the certain items will stop emitting light in water:
  - torches, lava buckets, molotovs, dynamite, and fire charges
- max entity distance: entities outside the max distance will no longer emit light, useful for reducing lag 
  - off: dont scan for entities (does not affect handheld lighting)
  - short: 16 blocks
  - normal: 32 blocks
  - far: 64 blocks
  - unlimited: no limit
- light updates: how frequently the dynamic lighting is updated
  - minimal: 2 per second
  - decreased: 5 per second (choppier lighting for fast moving entities)
  - smooth: 10 per second
  - smoothest: 20 per second (one per tick)

---

this project is still in development so there may be bugs