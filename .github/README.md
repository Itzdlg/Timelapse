# Timelapse

Timelapse is an abstraction and plugin, similar to the Hypixel Replay System, that will record certain areas or players, under certain conditions, then send the events back to staff members or players with permission to review recordings.

Currently, the project is still under development. The plugin will be released once a working Spigot plugin has been developed, along with the abstraction in the `common` module.

## Modules

`common` - Abstraction for recording, replaying, storing, etc. along with implementations that make sense for platform-independent code.

`spigot` - Implementation of the Timelapse Spigot plugin for versions 1.8 to 1.18.

*Other implementations are welcomed*

## Contributions

All code is under the GNU GPLv3 License available in the LICENSE.txt file and at https://www.gnu.org/licenses/gpl-3.0.en.html.

Contributions to source code must include the copying notice at the beginning of each source file in comments:
```java
/*
 * This file is part of Timelapse.
 *
 * Timelapse is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software
 * Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Timelapse is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with Timelapse in LICENSE.txt. If not, see
 *      <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */
```

Javadoc `@author` tags should be exactly `Timelapse Team and contributors`, if included.

### Branches

`master` - Most recent, tested, source.

`stable` - Releases determined to be stable (major versions).

`fix/<short description>` - Actively worked-on fixes to source code.

`feat/<short description>` - Actively worked-on features to source code.

`docs/<short description>` - Actively worked-on documentation changes.

### Versioning

Until pushed to `master`, the version should not be updated. Timelapse uses Semantic Versioning, which is available at https://semver.org.