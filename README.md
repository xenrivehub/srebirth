# SRebirth

SRebirth is an Rebirth plugin for Minecraft servers, designed to enhance gameplay by allowing players to reset their progress in exchange for rewards. It features a shop system, GUI panel interface, and admin commands for managing player rebirth levels and points.

## Features

- Rebirth level and point management
- Shop system
- GUI panel interface
- Admin commands
- PlaceholderAPI integration

## Installation

1. Install [Vault](https://www.spigotmc.org/resources/vault.34315/) and [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) plugins on your server.
2. Place the `SRebirth.jar` file into your `plugins` directory.
3. Restart your server or use the `/reload` command.

## Commands

- `/rebirth` — Opens the Rebirth Delivery Panel.
- `/arebirth reload` — Reloads the configuration files.
- `/arebirth givelevel <name> <level>` — Gives Rebirth Level to a player.
- `/arebirth givepoints <name> <points>` — Gives Rebirth Points to a player.
- `/arebirth setpoints <name> <points>` — Sets a player's Rebirth Points.
- `/arebirth setlevel <name> <level>` — Sets a player's Rebirth Level.
- `/arebirth resetpoints <name>` — Resets a player's Rebirth Level and Points.

## Configuration

- `plugin.yml` — Plugin metadata and commands.
- `messages.yml` — Messages and language file.
- `worth.yml` — Material worth values.
- `gui.yml` — GUI panel configuration.
- `shop.yml` — Shop items and prices.
- `data.yml` — Player data storage.

## Permissions
- `srebirth.admin` — Access to admin commands.
- `srebirth.use` — Access to main Rebirth GUI

## Contributing

To contribute, please submit a pull request or open an issue.

## License

This project is licensed under the [MIT License](LICENSE).

---

For more information, contact me on discord `xenrive`