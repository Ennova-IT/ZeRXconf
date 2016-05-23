# ZeRXconf
This library has been built in order to provide a Reactive wrapper around the Android [Network Service Discovery][nsd] API and the [JmDNS implementation][jmdns] for older devices.  

![Compatibility: Android 2.3+](https://img.shields.io/badge/compatibility-Android%202.3%2B-green.svg)
![Version: 1.0.3](https://img.shields.io/badge/version-1.0.3-green.svg)
![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ZeRXConf-green.svg?style=true)](https://android-arsenal.com/details/1/3220)
## What is Zeroconf
From [Wikipedia][wikipedia-zeroconf]:
> Zero-configuration networking (**zeroconf**) is a set of technologies that automatically creates a usable computer network based on the Internet Protocol Suite (TCP/IP) when computers or network peripherals are interconnected. It does not require manual operator intervention or special configuration servers.  
**Zeroconf** is built on three core technologies: assignment of numeric network addresses for networked devices, automatic distribution and resolution of computer hostnames, and automatic location of network services, such as printing devices. Without zeroconf, a network administrator must set up services, such as Dynamic Host Configuration Protocol (DHCP) and Domain Name System (DNS), or configure each computer's network settings manually.

You might have heard more about the [Bonjour Service][bonjour] from Apple, that is the custom implementation of **Zeroconf protocol**.

As an example, you can see Zeroconf working in the Chromecast environment, where you don't specify the address of the device, but it gets found easily by the apps. *Zeroconf is how it happens*.

# Importing the library
## Gradle
Add the ```JitPack``` repository to the root ```build.gradle``` file of your project
```groovy
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Import the library in your app's module ```build.gradle```
```groovy
dependencies {
    ...
    compile 'com.github.Ennova-IT:ZeRXconf:1.0.3'
}
```

## Maven
Add the ```JitPack``` repository to your build file
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Add the dependency
```xml
<dependency>
    <groupId>com.github.Ennova-IT</groupId>
    <artifactId>ZeRXconf</artifactId>
    <version>1.0.2</version>
</dependency>
```

# How to use ZeRXconf
>All the calls made by this library are written so that **the chain is executed on a separate thread** and the result of the computation is returned back on the main thread


**Note**: the calls to the methods included in the ```ZeRXconf``` class return a ```Subscription``` that awaits for the unsubscription in order to kill the advertising or the discovery service. As a surplus, we wired in the logic needed to be compatible to the [RxLifecycle][rxlifecycle] library so that, if you ```compose``` the ```Observable``` with the ```Activity``` or ```Fragment``` events, it will tear down the service correctly as well.

## Model
All the methods of this library will emit object of the ```NetworkServiceDiscoveryInfo``` class, that is a wrapper for both the ```NsdServiceInfo``` provided by athe Android API and the ```ServiceInfo``` that comes with the JmDNS library.
## Advertising your own service
When you need to start advertising your service, you need to provide the factory method with a few parameters, so that the API can build up the request.

>Since the Android implementation can be used only from **Android 4.1**, for previous versions of the system it will be automatically used the JmDNS implementation. Unfortunately, if you need to pass custom attributes to the service, Android supports it only from version **5.1 (API 21)** so, in that case, the library will revert to the JmDNS implementation in order to give the desired behaviour.

*Declaration:*  

```java
public static Observable<NetworkServiceDiscoveryInfo> advertise(@NonNull Context context,
				@NonNull String serviceName, @NonNull String serviceLayer, int servicePort,
				@Nullable Map<String, String> attributes)
```

*Usage:*

```java
Subscription s = ZeRXconf.advertise(context, "ZeRXconf", "_http._tcp.", 1234, getAttributes())
                .subscribe(onNext, onError);
```

## Discovering all the available services
>As per API limitation, listing all the services available in the current network will not allow the components to resolve them. That means that you will have to call the ```startDiscovery(Context, String)``` on the specific protocol for having the data resolved correctly. If, when doing so you receive an exception from the ```DiscoveryListener```, make sure you closed the previous ```Subscription``` before starting a new one.

*Declaration:*  

```java
public static Observable<NetworkServiceDiscoveryInfo> startDiscovery(@NonNull Context context)
```

*Usage:*

```java
Subscription s = ZeRXconf.startDiscovery(context).subscribe(onNext, onError);
```

## Discovering all the available services of a single kind
If you need to discover all the devices advertising a specific protocol, you shall use the ```startDiscovery(Context, String)``` factory method as this one will allow the resolution of the services and the item received will be ready to be consumed by your logic.

>As per design choice, this factory method will throw a ```NsdException``` if protocol equals either the ```"_services._dns-sd._udp"``` value or the ```ALL_AVAILABLE_SERVICES```. This a wanted behaviour that can alert you before you ship the product with an unwanted behaviour in it.  
**This method will not throw any other type of exception but it will notifiy you of errors while discovering the services with the usual ```onError``` callback**

*Declaration:*  

```java
public static Observable<NetworkServiceDiscoveryInfo> startDiscovery(@NonNull Context context,
				@NonNull String protocol) throws NsdException
```

*Usage:*

```java
Subscription s = ZeRXconf.startDiscovery(context, "_http._tcp.").subscribe(onNext, onError);
```

# License

*Copyright (c) 2015 Ennova S.r.l.*

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[nsd]: <http://developer.android.com/training/connect-devices-wirelessly/nsd.html>
[jmdns]: <https://github.com/jmdns/jmdns>
[wikipedia-zeroconf]: <https://en.wikipedia.org/wiki/Zero-configuration_networking>
[bonjour]: <http://www.apple.com/support/bonjour/>
[rxlifecycle]: <https://github.com/trello/RxLifecycle>
