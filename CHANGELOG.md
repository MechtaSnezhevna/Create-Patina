# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## Unreleased

### Added

#### Interactions

- **Polishing oxidized/waxed blocks by hand**: holding `sandpaper` in your hand and using it on items, just like polishing rough rose quartz.

### Fixed

#### Interactions

- **Item Drain tool interactions**: right-clicking a weathering item drain with an `axe` or `sandpaper` now scrapes/de-waxes the drain block, and using `honeycomb` waxes it, instead of the right-click being consumed with no effect.

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
