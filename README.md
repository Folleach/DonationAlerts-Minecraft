# DonationAlerts-Minecraft

A modification that allows you to receive events from **DonationAlerts**

**Welcome to the 2.0.0 version of the mod!**  
There are some new features appears!

Supports:
- Donation
- Twitch Bits
- Subscriptions to YouTube and Twitch

Allows to:
- Send message to the chat
- Execute any Minecraft commands (include mods)

You can download this from: [Modrinth](https://modrinth.com/mod/donation-alerts-integrate), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/donation-alerts-integrate) or [Releases](https://github.com/Folleach/DonationAlerts-Minecraft/releases)

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

The settings file is in `.minecraft/donation-alerts-integrate/settings.yaml`  
The file format is yaml, example:

```yaml
disableSettingsUpdateMessage: false
disableWelcomeMessage: false
triggers:
- name: default
  description: example trigger
  isActive: true
  sensitives:
  - properties:
      type: daintegrate/sensitive/donate
      value:
        from: 5
        to: 10
        currency: USD
  handlers:
  - delay: 0 # Any comment you can type here!
    properties:
      type: daintegrate/handler/message
      value:
        message: Hello, <donation_username>! This is an example message for all events from Donation Alerts
  - delay: 10
    properties:
      type: daintegrate/handler/command
      value:
        command: time set day # The sun is rising
- name: Subscribtion
  isActive: true
  sensitives:
  - properties:
      type: daintegrate/sensitive/subscribe
      value:
        type: YouTubeSubscription
  handlers:
  - properties:
      type: daintegrate/handler/message
      value:
        message: <donation_username> just subscribe to YouTube!
```

`triggers` is a list of events that can trigger by each message from donation alerts

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

| Type                                | Description                     |
| ----------------------------------- | ------------------------------- |
| `daintegrate/sensitive/always`      | Activates for any event         |
| `daintegrate/sensitive/donate`      | Activates for specific donation |
| `daintegrate/sensitive/subscribe`   | Activates for subscribe         |
| `daintegrate/sensitive/twitch/bits` | Activates for specific bits     |

#### Always

###### Parameters
Has no parameters

###### Example
```yaml
sensitives:
- properties:
    type: daintegrate/sensitive/always
```

#### Donate

Activates if the amount falls within the specified range (`from` <= amount <= `to`).  
And the currency matches.

###### Parameters

| Name       | Type     | Description |
| ---------- | -------- | ----------- |
| `from`     | number   | Left side number  |
| `to`       | number   | Right side number |
| `currency` | text     | Currency in the [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) code |

###### Example

```yaml
sensitives:
- properties:
    type: daintegrate/sensitive/donate
    value:
      from: 5
      to: 10
      currency: USD
```

#### Subscribe

Activates if the `type` is matches

###### Parameters

| Name       | Type     | Description |
| ---------- | -------- | ----------- |
| `type`     | enum     | Type to match, see below for available types |

###### Available types

- YouTubeSubscription
- TwitchSubscription
- TwitchFreeFollow
- TwitchGiftSubscription
- TwitchPrimeSubscription

###### Example

```yaml
sensitives:
- properties:
    type: daintegrate/sensitive/subscribe
    value:
      type: YouTubeSubscription
```


#### Twitch bits

Activates if the amount of Twitch bits falls within the specified range (`from` <= amount <= `to`).  

###### Parameters

| Name       | Type     | Description |
| ---------- | -------- | ----------- |
| `from`     | number   | Left side of the Twitch bits amount  |
| `to`       | number   | Right side of the Twitch bits amount |

###### Example

```yaml
sensitives:
- properties:
    type: daintegrate/sensitive/twitch/bits
    value:
      from: 0
      to: 2000
```

### Handlers

Handlers for the trigger.  
Perform actions when the trigger is activated.  

| Type                          | Description                                  |
| ----------------------------- | -------------------------------------------- |
| `daintegrate/handler/message` | Adds a message to your chat                  |
| `daintegrate/handler/command` | Sends a command to the server on your behalf |

###### Delay

Each handler, in addition to the properties object, has a `delay` property, it indicates after how many ticks the command will be executed.  
By default, 1 second in minecraft is 20 ticks

###### Replace patterns

In commands and messages, you can specify patterns that will be replaced with the corresponding values.

```
<donation_message>
<donation_amount>
<donation_currency>
<donation_username>
<minecraft_playername>
```

#### Message

Adds a message to your chat

###### Parameters

| Name       | Type     | Description |
| ---------- | -------- | ----------- |
| `message`  | text     | A message which will be added to the chat |

###### Example

```yaml
handlers:
- delay: 10
  properties:
    type: daintegrate/handler/message
    value:
      message: Hello! It's day time!
```

#### Command

Sends a command to the server on your behalf

###### Parameters

| Name       | Type     | Description |
| ---------- | -------- | ----------- |
| `command`  | text     | A command which will be send to the server (**without** slash `/`) |

###### Example

```yaml
handlers:
- delay: 10
  properties:
    type: daintegrate/handler/command
    value:
      command: time set day
```

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
        "settings.yaml",
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
10. Create welcome message with short guide  
    Example: [fabric_1.20/ClientEntryPoint.java](Modifications/fabric_1.20/src/main/java/net/folleach/daintegrate/fabric/ClientEntryPoint.java#L183-L204)

## Required and used:
MinecraftForge: https://github.com/MinecraftForge/MinecraftForge  
Socket.io: https://github.com/socketio/socket.io-client-java  
JSON-java: https://github.com/stleary/JSON-java  
