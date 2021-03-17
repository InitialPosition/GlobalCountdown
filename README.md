# GlobalCountdown
_"Because everything ends eventually"_

## Content
1. Introduction
2. Functions
3. Installation
4. Config

## Introduction
GlobalCountdown adds an expiration date to your Minecraft server.
A countdown is displayed for all players. Once the time reaches zero, every player will be banned.

## Functions
- Custom ban message and time
- Thats it this plugin doesn't have that many features

## Installation
To install the plugin, head over to [RELEASES](https://github.com/InitialPosition/GlobalCountdown/releases), download the latest `.jar` file and place it into the `plugins` folder of your Spigot installation.
The plugin will generate a config file on first startup. It is recommended to edit this file and to either reload or restart the server afterwards.

## Config
The following parameters exist in the config file and can be changed.

| Parameter | Usage | Accepted input |
| :-------- | ----- | -------------- |
| confEndingTime | The date at which to ban all players | A UNIX timestamp |
| confBanReason | The message to show players once time ends | String |
