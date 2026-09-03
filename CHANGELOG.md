# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Added

#### Configs

- **Oxidize Whole Fluid Tank**: If true, natural weathering advances the entire fluid tank multiblock at once, so large tanks no longer split into separate weathered pieces.
- **Weather Whole Fluid Tank with Tools**: If true, waxing, de-waxing or scraping a fluid tank block with a honeycomb, axe or sandpaper (by hand or with a deployer) applies to the entire tank multiblock at once, so large tanks never split apart.

### Changed

#### Items

- **Patina Clock is now craftable in Survival**: assemble it with a `Deployer` (Sequenced Assembly) by installing a `Precision Mechanism`, `Copper Sheet` and `Honeycomb` onto a `Clock`. It now has `256` durability (1 point per applied state change, not consumed in Creative) and no longer shows the enchantment glint.

#### Interactions

- **Patina Clock adjusts whole fluid tanks**: when the **Weather Whole Fluid Tank with Tools** config is enabled, advancing a large tank or setting it to any weathering/waxed state with the Patina Clock applies to the entire tank multiblock at once, so the tank no longer splits apart.

## 1.1.1 - 2026-09-02

### Added

#### Blocks

- **Weathering Stock Link** & **Weathering Display Link**: Thanks for reminding us that there are copper coils on them. (Stock Ticker **won't** be added as there's a glass cover outside the coil)

#### Interactions

- **Polishing oxidized/waxed blocks by hand**: holding `sandpaper` in your hand and using it on items, just like polishing rough rose quartz.
- **Wearable backtank weathering**: wearable backtank items support the full weathering cycle — hand/deployer sandpaper polishing, honeycomb/honey waxing, axe/sandpaper de-waxing and scraping, water/honey spout filling, and fan washing — with the stored air carried over. These interactions apply only to the wearable item form.

### Fixed

#### Interactions

- **Item Drain tool interactions**: right-clicking a weathering item drain with an `axe` or `sandpaper` now scrapes/de-waxes the drain block, and using `honeycomb` waxes it, instead of the right-click being consumed with no effect.
- **Backtank air kept when processing**: stored air now survives every backtank processing step (polishing, waxing, water/honey filling, fan washing) instead of resetting to empty.

## 1.1.0 - 2026-08-14

### Added

#### Items

- **Patina Clock**: (**CREATIVE GAMEMODE ONLY**) `Right-click` to advance an unwaxed block to the next weathering stage, `hold right-click` to select any weathering stage via a dashboard interface.

#### Recipes

- **Turning blocks into their next weathering stage** by `filling with water` or `bulk washing`.
- **Waxing blocks** by `filling with honey` or `bulk waxing (fan with honey fluid)`.
- **Unwaxing/Scraping blocks** by `using a deployer with an axe or sandpaper`.

#### Config

- **Collapse Similar Blocks in JEI**: If true, weathering variants will **not** be displayed separately. Hover your mouse over an unaffected item to browse its variants.

## 1.0.0 - 2026-07-31

First release of the mod!

### Added

#### Blocks & Items

- Weathering variants for all copper blocks of Create

#### Config

- **Enable Portable Fluid Interface Cross-Matching**: If true, portable fluid interfaces with different weathering states can be matched to each other.
