# QuakeSafe Offline

QuakeSafe Offline is a mesh-based communication application designed for emergency situations where internet and cellular networks are unavailable.

## Features
- **Offline-First**: Uses Nearby Connections, Wi-Fi Direct, and Bluetooth for communication.
- **Mesh Networking**: Multi-hop relay for extended range.
- **Push-to-Talk (PTT)**: Low-latency voice communication.
- **Security**: End-to-End Encryption using AES-GCM and ECDH.
- **Emergency Tools**: Siren, Strobe, and Emergency Guides.

## Installation
1. Clone this repository.
2. Open in Android Studio (Hedgehog or newer recommended).
3. Sync Gradle and build the project.

## Building for Release
To generate the signed APK or AAB:
```bash
./gradlew assembleRelease
./gradlew bundleRelease
```
The outputs will be located at:
- APK: `app/build/outputs/apk/release/app-release.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

## Keystore Info
A sample keystore has been included in the root directory:
- **Path**: `quake_keystore.jks`
- **Alias**: `quakekey`
- **Password**: `quake123`

## Testing Mesh & PTT
1. Install the app on at least two devices.
2. Grant all requested permissions (Location, Bluetooth, Nearby Devices, Microphone).
3. The devices will automatically discover each other and form a mesh.
4. Press and hold the **PUSH TO TALK** button on the Home screen to transmit voice.

## Hardware Note
Software cannot increase the physical RF power of the device. Range can be extended by using more nodes as repeaters or using external Wi-Fi boosters.

## Hotspot Relay
Enable "Hotspot Relay" in settings (Manual mode) to act as a central hub. Note: This will consume more battery.
