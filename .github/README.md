# Timelapse

Timelapse is an abstraction and plugin, acting similarly to the Hypixel Replay System, that will record certain areas or players, under certain conditions, then send the events back to staff members or players with permission to review recordings.

Currently, the project is still under development. The plugin will be released once a working Spigot plugin has been developed, along with the abstraction in the `common` module.

## Modules

`common` - Abstraction for recording, replaying, storing, etc. along with implementations that make sense for platform-independent code.

`spigot` - Implementation of the Timelapse Spigot plugin for versions 1.8 to 1.18.

*Other implementations are welcomed.*

## Contributions

### License

All code is under the GNU GPLv3 License available in the LICENSE.txt file and at https://www.gnu.org/licenses/gpl-3.0.en.html.

Contributions to source code must include the copying notice at the beginning of each source file in multi-line comments:
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

### Branches

`master` - Most recent and tested source code.

`stable` - Releases determined to be stable and significant.

`fix/<short description>` - Unfinished bug fixes for reported issues.

`feat/<short description>` - Unfinished feature or unit test implementations or changes.

`docs/<short description>` - Unfinished documentation changes.

### Versioning

Timelapse uses Semantic Versioning, which is available at https://semver.org.

Until pushed to `master`, the version should not be updated.

### Commits

Timelapse uses Conventional Commits, which is available at https://www.conventionalcommits.org/en/v1.0.0/. 

Commits to any branch other than `master` and `stable` do not need to include the commit prefix (e.g. `docs:` or `feat:`). Once pushed to `master`, the branch will be squash-merged and include the commit prefix in the merge commit.