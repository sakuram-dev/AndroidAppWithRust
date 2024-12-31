# AndroidAppWithRust

This project is an Android application that integrates Rust for performance-critical operations. The app demonstrates a performance comparison between Kotlin and Rust for prime number checking.

## Project Structure
- `android/`: Contains the Android project files.
- `rust/`: Contains the Rust library files.

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- [Rust](https://www.rust-lang.org/tools/install)
- Android NDK (installed via Android Studio)

### Building the Project

1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd <repository-directory>
2. Set for cross compile in .cargo/config file
   ```toml
   [target.aarch64-linux-android]
   ar = "<path-to-ndk>/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android-ar"
   linker = "<path-to-ndk>/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang"
3. Build the Rust library for Android:
   ```sh
   cd rust
   cargo build --target aarch64-linux-android --release
   ```
4. Copy the generated library to the Android project:
   ```sh
    cp target/aarch64-linux-android/release/librust_lib.so ../app/src/main/jniLibs/arm64-v8a/
    ```
5. Open the Android project in Android Studio and build the app.