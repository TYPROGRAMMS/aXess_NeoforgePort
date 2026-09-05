# Port status

## Target
Minecraft 1.21.1 / NeoForge 21.1.235 / Java 21.

## Migrated
- Forge Gradle build -> NeoGradle/UserDev
- `mods.toml` -> `neoforge.mods.toml`
- Forge event bus -> NeoForge mod/game buses
- Deferred registration -> NeoForge `DeferredRegister`/`DeferredHolder`
- Forge menu type extension -> NeoForge `IMenuTypeExtension` / `IContainerFactory`
- Forge networking `SimpleChannel` -> NeoForge custom payloads / `PayloadRegistrar`
- Forge packet distribution -> NeoForge `PacketDistributor`
- Forge client item extension -> NeoForge `IClientItemExtensions`
- Forge slider -> NeoForge `ExtendedSlider`
- Forge item-handler package -> NeoForge item handlers
- 1.21 block entity save/load signatures and item handler serialization
- old item NBT access -> `DataComponents.CUSTOM_DATA` helper

## Deliberately excluded
The old Forge data-generation Java providers are excluded from compilation. They are build-time
only and were based on the old provider API. The runtime assets/data from the original project
remain in `src/main/resources`.

## Verification limitation
This environment cannot resolve external Maven/Gradle hosts, so no real Gradle 1.21.1 compile
could be run here. The ZIP is therefore a source-level port prepared for IntelliJ/Gradle, not a
claim of a verified clean build.
