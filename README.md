# PerformCmd-Folia

A lightweight Minecraft plugin for Folia servers that allows safe command execution as other players with proper permission controls.

## ✨ Features

- 🔒 **Thread-safe** - Built with `FoliaLib` for proper Folia compatibility
- 👑 **Dual execution modes** - Run commands as OP or regular player
- 🎯 **Player selectors** - Full support for Minecraft selector syntax
- 📝 **Command logging** - All executions are logged for audit trails

## 🚀 Quick Start

1. Download the latest release
2. Drop into your `plugins` folder
3. Restart server
4. Ready to use!

## 📖 Usage

```bash
# Execute command as OP
/perfcmd asop <player> <command>

# Execute command as regular player
/perfcmd asplayer <player> <command>
```

### Examples

```bash
/perfcmd asop Steve gamemode creative
/perfcmd asplayer @a say Hello everyone!
/perfcmd asop Alex tp 0 100 0
```

## 🔐 Permissions

| Permission                 | Default | Description                       |
| -------------------------- | ------- | --------------------------------- |
| `perfcmd.use`              | `op`    | Base permission to use the plugin |
| `perfcmd.command.asop`     | `op`    | Allow OP command execution        |
| `perfcmd.command.asplayer` | `true`  | Allow player command execution    |

---

⚠️ **Security Note**: Use `asop` commands carefully and only grant permissions to trusted administrators.
