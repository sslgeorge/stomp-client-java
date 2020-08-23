# Stomp Client Java

A lightwight implementation of stomp application protocol for android java. 
Uses OkHttp3 underneath to implement websocket. TODO: Implement Websocket Provider

## Installation
The library is only available on Maven Central for the time being



Use the package manager [pip](https://pip.pypa.io/en/stable/) to install foobar.

```xml
<dependency>
    <groupId>org.gamjee</groupId>
    <artifactId>stomp-client-java</artifactId>
    <version>1.0</version>
</dependency>
```

## Basic Usage

```kotlin
val stompSession = StompClient.Builder
    .url("ws://10.0.2.2:7000/socket")
    .build()
```

A stomp server will always return a `COMPLETED` command when initial connection is requested, it is best practice to wait for that connection to be established before sending more data for processing or subscribing to an event.

To monitor the connection states, implement a `StompSessionListener` and add during build

```kotlin
val stompSession: StompSession = StompClient.Builder
    .setSessionStateListener(sessionListener)
    .url("ws://10.0.2.2:7000/socket")
    .build()
```

## StompSessionListener

Implement the `onChange` method of the `StompSessionListener` interface to get the different states of the stomp seession.

```kotlin
private val sessionListener = object : StompSessionListener {
    override fun onChange(state: SessionState) {
        when(state) {
           SessionState.CONNECTED -> onSocketConnected()
       }
   }
}
```

## Send message to topic
To send request to a stomp sever you can do as shown below


```kotlin
stompSession.send("/app/greetings", "ping") 
```


## Subscribe to topic
The subscribe function can take a handler to watch for changes in the event data


```kotlin
val subId = stompSession.subscribe("/topics/greetings", object: StompFrameHandler {
     override fun getPayloadType(): Class<*> {
         return User::class.java
     }

     override fun onMessage(payload: Any, headers: Collection<StompHeader>) {
         var user = payload as User
     }
})
```





## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)