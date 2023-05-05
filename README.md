# Create your terminal the way you want it !

You can make your own feature in kotlin language :
1. Plugins
2. Commands

By default, in app, you can :
- Load / unload addons
- Use custom event system (looks like Spigot event system, `EventRegister.getInstance()`)
- Create custom addons directory (`PluginLoader.createInstance<T>()` or `PluginLoader.getInstance<T>()`)
- Get / change command error message (`App.commandUnknownMessage`)
- Get / change global working directory (`App.workingDir`)

`EventRegister` and `PluginLoader` have been migrated into the [ValLib library](https://github.com/ValentinJDT/ValLib).

## Run the terminal

### Inline command

Basically : `java -jar <jar-file> [working directory]`<br>
You can specify a custom working directory if you don't want to work in current jar directory.
This value can be changed when terminal is running.

### Windows Terminal Profile

```JSON
{
  "commandline": "java -jar EmptyTerminal-<version>.jar [working-directory]",
  "elevate": true, // optional
  "guid": "{7b33327f-3e51-423a-8478-91203864abf8}",
  "hidden": false,
  "name": "EmptyTerminal",
  "startingDirectory": "<define jar directory else you can't load addons>"
}
```
This is an extract of the settings.json of WindowsTerminal located at :<br>
`%LOCALAPPDATA%/Packages/Microsoft.WindowsTerminal_8wekyb3d8bbwe/LocalState`<br>
or<br>
`%LOCALAPPDATA%/Packages/Microsoft.WindowsTerminalPreview_8wekyb3d8bbwe/LocalState`<br>
This directory path can be different if you have another version.

### Please note: this terminal was only designed for Windows
You can change the default working directory function to match with your OS.
