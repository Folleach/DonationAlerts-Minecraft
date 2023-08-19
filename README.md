# DonationAlerts-Minecraft
Modification can interact with your chat as well as with the rest of the world through commands.

You can download it here: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/donation-alerts-integrate)

## Setup

### Setup your token

There are several ways to set your token here.

#### With minecraft command

1. Copy the token to the clipboard from DonationAlerts service
2. Execute command in your minecraft chat
   ```
   /da set
   ```
   **NO NEED** to insert a token into the chat! The token will be taken from your clipboard.

#### With file

1. Open the file  
   Windows: `C:\Users\<your user>\.donation-alerts-token`  
   Linux: `~/.donation-alerts-token`
2. Paste your token to the file
3. Save

### Connect to the Donation Alerts

To connect to Donation Alerts, use the command in Minecraft
```
/da connect
```

To check connection status you can use command
```
/da status
```

And there is the command to disconnect
```
/da disconnect
```

## Configuration

The settings file is in `.minecraft/donation-alerts-integrate/settings.json`.  
The file format is json, example:

```json
{
  "triggers": [
    {
      "name": "default",
      "description": "Just default trigger",
      "isActive": true,
      "sensitive": [
        {
          "properties": {
            "type": "daintegratew/sensitive/always"
          }
        }
      ],
      "handlers": [
        {
          "properties": {
            "type": "daintegratew/handler/message",
            "value": {
              "message": "Hello! It's day time! No reload"
            }
          },
          "delay": 20
        }
      ]
    }
  ]
}
```

`.triggers` is a list of events that can trigger by each message from donation alerts

| Property    | Description            |
| ----------- | ---------------------- |
| name        | Just a name of trigger |
| description | Description of trigger |
| isActive    | Enables or disables the trigger.<br/> If `isActive = false`, the trigger will not be activated |
| sensitive   | Conditions for trigger activation<br/>see #Sensitive for learn more |
| handlers    | Handlers inside the trigger, execute any registered commands.<br/>You can create your own mod that will add a custom handler<br/>See #Handlers for lean more |

### Sensitive

Conditions for trigger activation.  
The DonationAlertsIntegrate provides multiple sensitive

#### Always

Activates the trigger always

##### Id
`daintegrate/sensitive/always`.  

##### Parameters
Has no parameters.  

##### Example
```json
{
    "properties": {
        "type": "daintegratew/sensitive/always"
    }
}
```

#### Range

Activates if the amount falls within the specified range.  
`from` <= amount <= `to`

#### Id
`daintegrate/sensitive/donation/range`

#### Parameters

| Name | Description |
| ---- | ----------- |
| from | Left amount side |
| to   | Right amount side |
| currency | Currency in the [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) code |

#### Example

```json
{
    "properties": {
        "type": "daintegrate/sensitive/donation/range",
        "value": {
            "from": 0,
            "to": 100,
            "currency": "USD"
        }
    }
}
```

### Handlers

Handlers for the trigger.  
Perform actions when the trigger is activated

## Sub-modifications

// todo: add information abount sub modifications

## Contribute
If you want to contribute, you can:
- Translate or edit translation in the modification [here](src/main/resources/assets/daintegratew/lang) 
- See [here](https://github.com/Folleach/DonationAlerts-Minecraft/projects/1) what you can do

### TODO for adding new Minecraft version

1. Create a new module in `Modifications`, example: "fabric_1.20"
2. Add module to `Modifications/settings.gradle`
3. Create a integrate machine
   ```java
   var cfgSource = DonationAlertsIntegrateFactory.create(
        "donation-alerts-integrate",
        "settings.json",
        LOGGER::info);   
   ```
4. Create basic handlers
   - MessageHandler
   - CommandHandler
5. Register basic handlers
   ```java
   DonationAlertsIntegrate.configure(Constants.ModId, Constants.ModUrl)
        .registerHandler(new MessageHandler())
        .registerHandler(new CommandHandler());
   ```
6. Add information abount update settings file
   ```java
   cfgSource.addListener(settings -> { });
   ```
   **Consider the setting that allows to disable this message!**
7. Create client side commands
   - /da set
   - /da connect
   - /da status
   - /da disconnect
8. Create `DonationAlertsClient`
   ```java
   var listener = new DonationAlertsEventListener(new EventProcessor(), DonationAlertsIntegrate.getEventListeners());
   var client = new DonationAlertsClient(Constants.DonationAlertsEventServer, listener);
   ```
9. Call `cfgSource.startListening()` as late as possible.  
   For start watcher on the config file

## Required and used:
MinecraftForge: https://github.com/MinecraftForge/MinecraftForge  
Socket.io: https://github.com/socketio/socket.io-client-java  
JSON-java: https://github.com/stleary/JSON-java  
