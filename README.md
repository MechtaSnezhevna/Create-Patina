<p align="center"><img src="https://raw.githubusercontent.com/MechtaSnezhevna/Create-Patina/refs/heads/1.21.1/src/main/resources/logo.png" alt="Logo" width="128"></p>
<h1 align="center">Create: Patina</h1>

A Create addon introducing oxidized variants and oxidizing mechanisms of copper items & blocks.

**Read this in other languages:**
[English](README.md) | [简体中文](docs/i18n/README.zh-CN.md)

**What's new? See** [Changelog](CHANGELOG.md)

## Features

### New Blocks

Exposed, weathered, oxidized, waxed... 7 variants for **EVERY** Create block that contains copper components.

Therefore, the weathering function of vanilla Minecraft also applies to those blocks.

This provides a realistic and immersive machine-building experience!

### New Items

- **Patina Clock**: `Right-click` to advance an unwaxed block to the next weathering stage, `hold right-click` to select any weathering stage via a dashboard interface. It has `256` durability (each state change costs 1 point, not consumed in Creative) and is craftable in Survival.

### New Recipes

- **Turning blocks into their next weathering stage** by `filling with water` or `bulk washing`.
- **Waxing blocks** by `filling with honey` or `bulk waxing (fan with honey fluid)`.
- **Unwaxing/Scraping blocks** by `using a deployer with an axe or sandpaper` or holding `sandpaper` in your hand and using it on them, just like polishing rough rose quartz.
- **Assembling the Patina Clock** by Sequenced Assembly: a `Deployer` installs a `Precision Mechanism`, `Copper Sheet` and `Honeycomb` onto a `Clock`.

### New Tags

Eight item tags classify every weathering copper part by its exact stage:

- `createpatina:unaffected`, `createpatina:exposed`, `createpatina:weathered`, `createpatina:oxidized` - unwaxed stages.
- `createpatina:waxed`, `createpatina:waxed_exposed`, `createpatina:waxed_weathered`, `createpatina:waxed_oxidized` - their waxed counterparts.

Vanilla copper blocks, Create's copper blocks and all Patina weathering variants are included.

## Configs

- **Collapse Similar Blocks in JEI**: If true, weathering variants will **not** be displayed separately. Hover your mouse over an unaffected item to browse its variants.
- **Enable Portable Fluid Interface Cross-Matching**: If true, portable fluid interfaces with different weathering states can be matched to each other.
- **Oxidize Whole Fluid Tank**: If true, natural weathering advances the entire fluid tank multiblock at once, so large tanks no longer split into separate weathered pieces.
- **Weather Whole Fluid Tank with Tools**: If true, waxing, de-waxing or scraping a fluid tank block with a honeycomb, axe or sandpaper (by hand or with a deployer) applies to the entire tank multiblock at once, so large tanks stay intact. Adjusting a fluid tank's weathering state with the Patina Clock applies to the whole multiblock as well.
- **Enable Random-Tick Weathering**: If false, unaffected copper blocks from Create and the weathering variants added by this mod no longer oxidize on random ticks, so their weathering state can only be changed with tools.
